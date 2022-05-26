FROM  mcr.microsoft.com/playwright:focal
WORKDIR /home/da
ARG VERSION
RUN apt-get update && apt-get -y install openjdk-11-jdk-headless && rm -rf /var/lib/apt
COPY target/dataAgents-${VERSION}-jar-with-dependencies.jar	app.jar
COPY config config
COPY dataSet.json dataSet.json
ENV JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
ENTRYPOINT ["java","-jar","app.jar"]