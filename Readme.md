# WebDownloader

![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/acdemeg/WebDownloader/build-workflow.yml?logo=gradle&labelColor=292f35)
[![GitHub release (with filter)](https://img.shields.io/github/v/release/acdemeg/WebDownloader?logo=github&labelColor=292f35)](https://github.com/acdemeg/WebDownloader/releases)
![GitHub Release Date - Published_At](https://img.shields.io/github/release-date/acdemeg/WebDownloader?color=8B8C7A&labelColor=292f35)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/acdemeg/WebDownloader?color=DC143C&labelColor=292f35)
![GitHub repo size](https://img.shields.io/github/repo-size/acdemeg/WebDownloader?color=008B8B&labelColor=292f35)
![GitHub all releases](https://img.shields.io/github/downloads/acdemeg/WebDownloader/total?color=DB7093&labelColor=292f35)

Project to aim persisting remote web resources on local storage
also creating site mirror, graph-map of site, estimate of size
web-site(static resources) etc. Based on WGET utility.

## DEMO
http://devproject.site:8002

## DockerHub
https://hub.docker.com/repository/docker/acdemeg/web-downloader/general

## Requirements
1. Java 21 or higher
2. Gradle 8.5 or higher
3. Docker, Docker-compose
4. Linux, Wget package

## Description
### Eng:
The application is designed to create copies (mirrors) of websites and download them as a zip-archive. After unpacking the archive, open the index.html file in your browser to browse the site locally.
### Rus:
Приложение предназначено для создания копий(зеркал) web-сайтов и их скачивания в виде zip-архива. После распаковки архива откройте файл index.html в вашем браузере для локального простомтра сайта.
