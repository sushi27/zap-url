#!/bin/bash

echo "Stopping existing containers..."
docker-compose down

echo "Building Java application with Maven..."
# Assuming you're using Maven for your Spring Boot app
mvn clean package -DskipTests

echo "Rebuilding and starting Docker containers..."
docker-compose up --build -d

echo "Containers started. Checking status..."
docker-compose ps

echo "Viewing logs from application container..."
docker-compose logs -f app