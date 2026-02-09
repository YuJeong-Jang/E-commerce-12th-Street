# 1단계: 빌드 환경
FROM gradle:8.5-jdk17 AS build-env
ARG SERVICE_NAME
COPY . /app
WORKDIR /app

RUN sed -i 's/\r$//' ./gradlew
RUN chmod +x ./gradlew

# [수정] 에러 메시지에 나온 프로젝트 이름(:streets-member 등)을 그대로 사용함
RUN ./gradlew :${SERVICE_NAME}:build -x test

# 2단계: Tomcat 준비
FROM eclipse-temurin:11-jdk-alpine AS builder
ENV TOMCAT_VERSION=9.0.71
ENV CATALINA_HOME=/usr/local/tomcat
RUN apk add --no-cache wget tar sed binutils \
 && mkdir $CATALINA_HOME \
 && wget https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz \
 && tar -xvf /tmp/tomcat.tar.gz -C $CATALINA_HOME --strip-components=1 \
 && rm -rf /tmp/tomcat.tar.gz $CATALINA_HOME/webapps/*

RUN $JAVA_HOME/bin/jlink \
    --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.sql,jdk.unsupported,java.xml,java.rmi,java.management,java.desktop,java.naming,java.security.sasl,java.security.jgss,java.instrument \
    --output /custom-jre --compress=2 --strip-debug --no-header-files --no-man-pages

# 3단계: 최종 실행 이미지
FROM alpine:latest
ARG SERVICE_NAME
RUN apk add --no-cache libstdc++ libgcc gcompat
ENV JAVA_HOME=/usr/lib/jvm/java-11-custom
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH

COPY --from=builder /custom-jre $JAVA_HOME
COPY --from=builder $CATALINA_HOME $CATALINA_HOME

# [수정] 빌드된 폴더가 어디든 상관없이 .war 파일을 찾아서 ROOT.war로 복사함
# 보통 /app/streets/member-service/build/libs/ 안에 생성됨
COPY --from=build-env /app/**/build/libs/*.war $CATALINA_HOME/webapps/ROOT.war

RUN chmod +x $JAVA_HOME/bin/java
EXPOSE 8080
CMD ["catalina.sh", "run"]