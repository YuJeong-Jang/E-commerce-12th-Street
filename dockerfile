FROM eclipse-temurin:11-jdk-alpine AS builder

ARG SERVICE_NAME

ENV TOMCAT_VERSION=9.0.71
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

# [수정] apt-get 대신 apk 사용 (Java는 이미 설치되어 있으므로 생략)
RUN apk add --no-cache wget tar sed binutils \
 && mkdir $CATALINA_HOME \
 && wget https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz \
 && tar -xvf /tmp/tomcat.tar.gz -C $CATALINA_HOME --strip-components=1 \
 && sed -i 's|appBase="webapps"|appBase="/webapps/streets/prod"|g' $CATALINA_HOME/conf/server.xml \
 && sed -i '/autoDeploy="true">/a <Context path="" docBase="streets.war" reloadable="true" />' $CATALINA_HOME/conf/server.xml \
 && rm -rf /tmp/apache-tomcat-${TOMCAT_VERSION} \
 && rm -rf /tmp/tomcat.tar.gz \
 && rm -rf $CATALINA_HOME/webapps/ROOT \
           $CATALINA_HOME/webapps/examples \
           $CATALINA_HOME/webapps/docs \
           $CATALINA_HOME/webapps/manager

# jlink로 경량화된 JRE 생성
RUN $JAVA_HOME/bin/jlink \
    --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.sql,jdk.unsupported,java.xml,java.rmi,java.management,java.desktop,java.naming,java.security.sasl,java.security.jgss,java.instrument \
    --output /custom-jre \
    --compress=2 --strip-debug --no-header-files --no-man-pages

# 2단계 - 실행 이미지 (기존과 동일)
FROM alpine:latest

ARG SERVICE_NAME

RUN apk add --no-cache libstdc++ libgcc gcompat

ENV JAVA_HOME=/usr/lib/jvm/java-11-custom
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
ENV LD_LIBRARY_PATH=$JAVA_HOME/lib/server
ENV CATALINA_PTD=$CATALINA_HOME/temp/tomcat.pid

COPY --from=builder /custom-jre $JAVA_HOME
COPY --from=builder $CATALINA_HOME $CATALINA_HOME

# WAR 파일 복사
COPY ./${SERVICE_NAME}/build/libs/${SERVICE_NAME}.war /webapps/streets/prod/streets.war

RUN chmod +x $JAVA_HOME/bin/java

EXPOSE 8080

CMD ["catalina.sh", "run"]
