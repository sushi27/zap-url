#!/bin/bash

echo "Stopping all containers..."
docker-compose down

echo "Removing any dangling containers and networks..."
docker system prune -f

echo "Building Java application with Maven..."
mvn clean package -DskipTests

echo "Rebuilding and starting Docker containers..."
docker-compose up --build -d --scale app=2

echo "Waiting for containers to initialize (15 seconds)..."
sleep 15

echo "Containers status:"
docker-compose ps

echo "Fetching logs from ALL app instances..."
for container in $(docker ps --filter "name=zap_url_app" --format "{{.Names}}"); do
  echo -e "\nLogs for $container:"
  docker logs $container --tail=50  # Show last 50 lines per instance
done

echo -e "\nTailing live logs (Ctrl+C to exit)..."
docker-compose logs -f app  # Stream combined logs