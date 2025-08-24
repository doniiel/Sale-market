# Sale Market Application

This is a Spring Boot-based e-commerce application for managing users, products, categories, orders, and payments. It provides a RESTful API with JWT-based authentication and integrates with a PostgreSQL database. API documentation is available via Swagger UI.

## Technologies Used

- **Java 17**: Programming language.
- **Spring Boot 3.5.5**: Framework for building the application.
- **Spring Data JPA**: For database operations and ORM.
- **Spring Security**: For authentication and authorization with JWT.
- **JJWT (0.11.5)**: For JSON Web Token generation and validation.
- **PostgreSQL**: Relational database for persistent storage.
- **Liquibase**: For database schema management and migrations.
- **Springdoc OpenAPI (2.8.5)**: For generating API documentation with Swagger UI.
- **MapStruct (1.5.5)**: For mapping between DTOs and entities.
- **Lombok**: To reduce boilerplate code.
- **Docker**: For containerization of the application and database.
- **Docker Compose**: For orchestrating multi-container setup.
- **Gradle**: Build tool for dependency management and project building.

## Prerequisites

To run this application, ensure you have the following installed:

- **Docker**: For running the application and database in containers.
- **Docker Compose**: For orchestrating the containers.
- **Java 17**: If running the application without Docker.
- **Gradle** (optional): If building the application locally.

## How to Run the Application

### Using Docker Compose (Recommended)

1. **Clone the Repository**:
   ```bash
   git clone git@github.com:doniiel/Sale-market.git
   cd sale-market
   ```

2. **Build and Run with Docker Compose**:
   Ensure Docker and Docker Compose are installed. Run the following command in the project root directory (where `docker-compose.yml` is located):
   ```bash
   docker-compose up --build
   ```
   This will:
   - Build the Spring Boot application image using Gradle.
   - Start a PostgreSQL container (`sale-market-postgres`) and the application container (`sale_app`).
   - Map port `8080` for the application and `5432` for PostgreSQL.

3. **Access the Application**:
   - The API will be available at `http://localhost:8080`.
   - Swagger UI for API documentation can be accessed at `http://localhost:8080/swagger-ui/index.html`.

4. **Stop the Application**:
   To stop and remove the containers:
   ```bash
   docker-compose down
   ```
   To also remove the PostgreSQL data volume:
   ```bash
   docker-compose down -v
   ```

5. **Access Swagger UI**:
   Open `http://localhost:8080/swagger-ui/index.html` to explore the API documentation and test endpoints.
