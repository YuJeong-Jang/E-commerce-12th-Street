FROM ubuntu:22.04 AS builder
  
# Set environment variables
ENV TOMCAT_VERSION=9.0.71
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

# Install and configure Tomcat.
RUN apt-get update && apt-get install -y openjdk-11-jdk sed wget tar binutils \
 && mkdir $CATALINA_HOME \
 && wget https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz \
 && tar -xvf /tmp/tomcat.tar.gz -C $CATALINA_HOME --strip-components=1 \
 &&  sed -i 's|appBase="webapps"|appBase="/webapps/streets/prod"|g' $CATALINA_HOME/conf/server.xml \
 && sed -i '/autoDeploy="true">/a <Context path="" docBase="streets.war" reloadable="true" />' $CATALINA_HOME/conf/server.xml \
 && rm -rf /tmp/apache-tomcat-${TOMCAT_VERSION} \
 && rm -rf /tmp/tomcat.tar.gz \
 && rm -rf $CATALINA_HOME/webapps/ROOT \
           $CATALINA_HOME/webapps/examples \
           $CATALINA_HOME/webapps/docs \
           $CATALINA_HOME/webapps/manager

RUN $JAVA_HOME/bin/jlink --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.sql,jdk.unsupported,java.xml,java.rmi,java.management,java.desktop,java.naming,java.security.sasl,java.security.jgss,java.instrument \
    --output /custom-jre \
    --compress=2 --strip-debug --no-header-files --no-man-pages

#2단계 - 1단계의 결과값만 가져와서 집어넣음
FROM alpine:latest

RUN apk add --no-cache libstdc++ libgcc gcompat

ENV JAVA_HOME=/usr/lib/jvm/java-11-custom
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
ENV LD_LIBRARY_PATH=$JAVA_HOME/lib/server
ENV CATALINA_PTD=$CATALINA_HOME/temp/tomcat.pid

#맨 위 from에서 copy까지의 '결과값'만 가져오라는 뜻
#캐시도 가져오지 말고 build한 최종본 가져오라는 뜻
# -> 이게 바로 multi stage build 전략
COPY --from=builder /custom-jre $JAVA_HOME
COPY --from=builder $CATALINA_HOME $CATALINA_HOME
COPY ./streets9.war /webapps/streets/prod/streets.war

RUN chmod +x $JAVA_HOME/bin/java

EXPOSE 8080

CMD ["catalina.sh", "run"]
