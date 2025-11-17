# === 1단계: 빌더 (Builder Stage) ===
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY settings.gradle ./settings.gradle
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY build.gradle ./build.gradle
COPY gradle.properties ./gradle.properties

RUN ./gradlew dependencies --quiet

COPY src ./src

RUN ./gradlew shadowJar


# === 2단계: JRE 빌더 (JRE Builder Stage) ===
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

WORKDIR /app

COPY --from=builder /app/build/libs/*-all.jar app.jar

RUN $JAVA_HOME/bin/jdeps \
    --print-module-deps \
    --ignore-missing-deps \
    -q \
    --multi-release 17 \
    app.jar > /tmp/modules.list && \
    echo "java.sql,java.naming" >> /tmp/modules.list

RUN $JAVA_HOME/bin/jlink \
    --add-modules $(cat /tmp/modules.list | tr '\n' ',') \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    --output /custom-jre


# === 3단계: 최종 (Final Stage) ===
FROM alpine:latest AS final

RUN apk add --no-cache tzdata

WORKDIR /app

COPY --from=jre-builder /custom-jre /opt/java/openjdk-minimal
COPY --from=builder /app/build/libs/*-all.jar app.jar

ENV JAVA_HOME=/opt/java/openjdk-minimal
ENV PATH="$JAVA_HOME/bin:$PATH"

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
