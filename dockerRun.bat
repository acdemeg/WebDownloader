gradle -x test build
docker run -d --rm --name webd -p 8000:8080 -v .\build\libs:/server openjdk:17 java -jar /server/WebDownloader.jar
