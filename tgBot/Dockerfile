FROM openjdk:11-jdk-slim
WORKDIR /home/tg
ARG VERSION
COPY target/tgBot-${VERSION}.jar app.jar
COPY config config
ENTRYPOINT ["java","-jar","app.jar"]