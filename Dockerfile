FROM openjdk:latest
WORKDIR /server
COPY "./build/libs/WebDownloader-1.0.0.jar" /server