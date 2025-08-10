# MDtmp

MDtmp is a temporary, AI-powered markdown sharing platform. It allows users to create, publish, and share markdown content that automatically expires after a specified time.

This project is built using a microservices architecture, with dedicated services for handling storage, metadata, content aggregation, expiration, and AI-based enhancements.

## Architecture
MDtmp is composed of several independent services that communicate with each other through an API Gateway and a Kafka messaging queue.

*   **API Gateway**: The single entry point for all client requests. It routes traffic to the appropriate backend service.
*   **Frontend**: A React-based application providing the user interface and the markdown editor.
*   **Storage Service**: Manages the storage of markdown content in a MinIO S3 bucket.
*   **Metadata Service**: Handles the storage and retrieval of publication metadata (title, author, expiration) in a PostgreSQL database.
*   **Aggregator Service**: Fetches and combines data from the Storage and Metadata services to present a complete publication to the user.
*   **Enhancement Service**: Integrates with an Ollama-powered AI model to provide content enhancement capabilities.
*   **Expirer Service**: A background service that uses Redis and Kafka to track and manage the expiration of publications, deleting content and updating metadata when the time-to-live (TTL) is reached.

### Data Flow for Publishing
1.  A user submits a new markdown document from the **Frontend**.
2.  The **API Gateway** routes the request to the **Storage Service**.
3.  The **Storage Service** saves the raw markdown content to MinIO and publishes a `publication-upload` event to Kafka.
4.  Both the **Metadata Service** and **Expirer Service** consume this event.
5.  The **Metadata Service** stores the publication's metadata (title, author, hash, TTL) in PostgreSQL.
6.  The **Expirer Service** adds an entry to Redis with the publication's hash and its expiration timestamp.

## Technology Stack

| Component         | Technology                                                                                                                                                                |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Frontend**      | [React](https://react.dev/), [Vite](https://vitejs.dev/), [Tiptap](https://tiptap.dev/), [Axios](https://axios-http.com/)                                                 |
| **Backend**       | [Java 17](https://www.oracle.com/java/), [Spring Boot 3](https://spring.io/projects/spring-boot), [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) |
| **AI**            | [Spring AI](https://spring.io/projects/spring-ai), [Ollama](https://ollama.com/)                                                                                          |
| **Database**      | [PostgreSQL](https://www.postgresql.org/)                                                                                                                                 |
| **File Storage**  | [MinIO](https://min.io/) (S3 Compatible)                                                                                                                                  |
| **Messaging**     | [Apache Kafka](https://kafka.apache.org/)                                                                                                                                 |
| **Caching**       | [Redis](https://redis.io/)                                                                                                                                                |
| **Orchestration** | [Docker](https://www.docker.com/), [Docker Compose](https://docs.docker.com/compose/)                                                                                     |

## Getting Started

### Prerequisites

* Java 17+
* Docker and Docker Compose
* [Ollama](https://ollama.com/) (for the Enhancement service)

### 1. Run Infrastructure

All required infrastructure services (PostgreSQL, MinIO, Kafka, Redis) can be started using Docker Compose.

```bash
cd infrastructure
docker-compose up -d
```

### 2. Run Backend Services

Each backend service is a separate Spring Boot application. Run them from their respective directories. It's recommended to run them in the following order.

```bash
# In separate terminal sessions:

# Storage Service
cd storage
./mvnw spring-boot:run

# Metadata Service
cd metadata
./mvnw spring-boot:run

# Aggregator Service
cd aggregator
./mvnw spring-boot:run

# Expirer Service
cd expirer
./mvnw spring-boot:run

# Enhancement Service (requires Ollama to be running)
cd enhancement
./mvnw spring-boot:run

# API Gateway
cd gateway
./mvnw spring-boot:run
```

### 3. Run Frontend

The frontend is a React application built with Vite.

```bash
cd frontend
npm install
npm run dev
```

The application will be available at `http://localhost:5173`.

## API Documentation

Each microservice exposes its own OpenAPI specification. The API Gateway aggregates these specifications into a single Swagger UI instance.

Once all services are running, you can access the consolidated API documentation at:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**