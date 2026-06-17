# 🛒 E-commerce API

REST API for e-commerce management built with **Java + Spring Boot**, following **Clean Code** principles (Robert C. Martin) and layered architecture best practices.

## 📋 About the project

This API provides full management of **products**, **clients**, and **reviews**, with robust validation, centralized exception handling, and unit test coverage.

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **JUnit 5 + Mockito**
- **Docker**
- **Maven**

## 🏗️ Architecture

The project follows a clear separation of concerns:

```
src/main/java/com/crud/ecommerce/
├── business/
│   ├── service/         # Business rules and orchestration
│   ├── validation/       # Input validation
│   ├── mapper/            # DTO <-> entity conversion
│   ├── util/              # Reusable utilities
│   └── constants/         # Validation constants
├── controller/             # REST endpoints
├── dto/
│   ├── resquest/           # Input DTOs (Create/Update)
│   └── response/           # Output DTOs
├── exception/               # Custom exceptions
├── infrastructure/
│   ├── entity/              # JPA entities
│   └── repository/          # Data access interfaces
└── config/                   # Global configuration (GlobalExceptionHandler)
```

## 📦 Features

### Products
- Listing with dynamic sorting
- Find by ID
- Create, update, and delete
- Validation for name, description, price, and stock
- Deletion blocked when linked reviews exist

### Clients
- Full registration with profile (CPF, RG, address)
- Partial updates
- Validation for name, email, phone, CPF, RG, and address
- Deletion blocked when linked reviews exist

### Reviews
- Linked to both product and client
- Rating from 1 to 5 stars
- Title and comment with length validation

## ⚙️ Configuration

### Environment variables

Copy the example file and fill in your own values:

```bash
cp .env.example .env
```

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=db_ecommerce
DB_USERNAME=postgres
DB_PASSWORD=

DB_POOL_MIN=5
DB_POOL_MAX=10

DDL_AUTO=update
SHOW_SQL=true

SERVER_PORT=8080
```

### Running locally

```bash
./mvnw spring-boot:run
```

### Running with Docker

```bash
docker build -t ecommerce-app .
docker run -p 8080:8080 --env-file .env ecommerce-app
```

Or with Docker Compose (API + PostgreSQL):

```bash
docker-compose up
```

## 🧪 Tests

The project includes unit tests for the **validation**, **mapper**, and **service** layers, using JUnit 5 and Mockito.

```bash
./mvnw test
```

## 📐 Error handling

The API uses a centralized `GlobalExceptionHandler`, returning standardized responses:

```json
{
  "status": 400,
  "message": "Invalid request!",
  "errors": [
    "Invalid name: use only letters and spaces (3 to 100 characters)."
  ],
  "success": false,
  "timestamp": "2026-06-17T12:00:00"
}
```

| Exception | HTTP Status |
|---|---|
| `NotFoundException` | 404 |
| `BadRequestException` | 400 |
| `ConflictException` | 409 |
| `Exception` (generic) | 500 |

## 📄 Request examples

### Create product
```json
POST /products
{
  "name": "Brazil Jersey - World Cup 2026",
  "description": "Official Brazilian national team jersey for the 2026 World Cup.",
  "price": 299.99,
  "stock": 500
}
```

### Create client
```json
POST /clients
{
  "name": "João Pedro Silva",
  "email": "joao.pedro@email.com",
  "phone": "(11) 99999-1234",
  "cpf": "123.456.789-09",
  "rg": "12.345.678-9",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "complement": "Apto 45",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310-100"
  }
}
```

### Create review
```json
POST /reviews
{
  "productId": 1,
  "clientId": 1,
  "rating": 5,
  "title": "Amazing product!",
  "comment": "Great quality, arrived fast and well packaged."
}
```

## 🧱 Principles applied

- **SRP (Single Responsibility Principle)** — each class has a single responsibility
- **DRY (Don't Repeat Yourself)** — shared logic via interfaces (`AddressField`) and generic utilities (`EntityFinderUtils`, `EntityOperationUtils`)
- **Clean Code** — descriptive names, small functions, low coupling

## 👤 Author

Built by **João Pedro** as a hands-on study project in Spring Boot, applying Clean Code concepts and layered architecture.