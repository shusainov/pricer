FROM openjdk:11-jdk-slim
WORKDIR /home/dc
ARG VERSION
COPY target/datacollector-${VERSION}.jar app.jar
COPY config config
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]