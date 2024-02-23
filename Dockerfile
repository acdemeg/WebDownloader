FROM openjdk:21
RUN microdnf install wget
RUN microdnf install zip
WORKDIR /server
COPY "./build/libs/WebDownloader.jar" /server
