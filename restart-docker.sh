#!/bin/bash

echo "Stopping all containers..."
docker-compose down

echo "Removing any dangling containers and networks..."
docker system prune -f

echo "Building Java application with Maven..."
mvn clean package -DskipTests

echo "Rebuilding and starting Docker containers..."
docker-compose up --build -d

echo "Waiting for containers to initialize (15 seconds)..."
sleep 15

echo "Containers status:"
docker-compose ps

echo "Redis container logs:"
docker logs zap_url_redis

echo "Testing Redis connection from inside the app container..."
docker exec zap_url_app sh -c "nc -zv redis 6379 || echo 'Connection failed'"

echo "Application logs:"
docker-compose logs -f app