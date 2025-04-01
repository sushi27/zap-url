
# Zap URL - Scalable URL Shortener

Zap URL is a scalable and efficient URL shortening service designed to handle high-performance workloads. It is built using **Spring Boot** APIs, **PostgreSQL** for database management, **Redis** for caching, and **Nginx** for load balancing. The service has been optimized to handle high traffic loads and provide a seamless user experience.

## Features

- **High-Performance Service:** Developed using Spring Boot APIs and PostgreSQL, capable of handling 1000+ requests.
- **Caching and Rate Limiting:**
  - Implemented Redis caching, reducing database queries by 70% and improving response time by 50%.
  - Added IP-based throttling (10 requests/min per user) for rate limiting, ensuring fair usage and preventing abuse.
- **Load Balancing and Scalability:** 
  - Nginx-based load balancing for distributing incoming traffic efficiently across instances.
  - Dockerized microservice architecture that scales to 10+ concurrent instances to ensure seamless performance under high traffic loads.

## Architecture Overview

- **Backend:** Spring Boot (Java)
- **Database:** PostgreSQL (for storing shortened URLs and metadata)
- **Caching:** Redis (for fast URL lookups)
- **Load Balancer:** Nginx (for distributing incoming traffic)
- **Containerization:** Docker
- **Scaling:** Dockerized microservices, scaling to 10+ instances under high traffic loads.

## Setup Instructions

### Prerequisites

- Java 11 or higher
- Docker
- PostgreSQL, Redis, Nginx Docker containers

### Local Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/sushi27/zap-url.git
   cd zap-url
   ```

2. Build and run the project with Docker Compose:
   ```bash
   chmod +x restart-docker.sh
   ./restart-docker.sh
    ```

   This will start all the necessary containers (PostgreSQL, Redis, Nginx) and the Spring Boot application.

3. Access the application:
   - API endpoint: `http://localhost`

### Endpoints

#### Shorten URL
- **POST** `/shorten`
  - **Request Body:**
    ```json
    {
      "longUrl": "http://example.com"
    }
    ```
  - **Response:**
    ```json
    {
      "shortUrl": "http://localhost/xyz123"
    }
    ```

#### Redirect to Original URL
- **GET** `/{shortenedUrl}`
  - **Response:** Redirects to the original URL.

## Scaling and Load Balancing

The project uses **Nginx** for load balancing and **Docker** for containerization, allowing the service to scale across 10+ concurrent instances and ensuring high availability.

## Contributing

Feel free to contribute to this project. To submit changes:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
