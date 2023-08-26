FROM openjdk:latest
RUN microdnf install wget
RUN microdnf install zip
WORKDIR /server
COPY "./build/libs/WebDownloader-1.0.0.jar" /server
