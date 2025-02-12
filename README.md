# Accounts Payable API

This project implements a REST API for a simple accounts payable system using Java, Spring Boot, and PostgreSQL, following Domain Driven Design principles.  It provides CRUD operations for accounts, payment status updates, data retrieval, and CSV import functionality.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Building the Application](#building-the-application)
    - [Running with Docker Compose](#running-with-docker-compose)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Data Import (CSV)](#data-import-csv)
- [Error Handling](#error-handling)
- [Database Schema](#database-schema)
- [Deployment](#deployment)
- [Future Enhancements](#future-enhancements)


## Features

* **CRUD operations** for accounts payable (Create, Read, Update, Delete).
* **Payment status update**.
* **Data retrieval** with filtering and pagination.
* **CSV import** of accounts.
* **Authentication** using JWT.
* **Domain Driven Design** implementation.
* **Database migrations** with Flyway.
* **Containerization** with Docker and Docker Compose.
* **Comprehensive API documentation** using Springdoc OpenAPI (Swagger).
* **Unit Tests**.

## Technologies

* **Java:** 17 or higher
* **Spring Boot:** 3.x
* **PostgreSQL:** Database
* **Flyway:** Database migration tool
* **Docker:** Containerization platform
* **Docker Compose:** Orchestration tool for multi-container Docker applications
* **JPA/Hibernate:** ORM framework
* **Spring Security:** Security framework
* **JWT (JSON Web Token):** Authentication
* **OpenCSV:** CSV processing library
* **Springdoc OpenAPI:** API documentation
* **Maven:** Build tool
* **Lombok:** Reduces boilerplate code (optional)

## Getting Started

### Prerequisites

* Java 17 or higher
* Maven 3.6 or higher
* Docker and Docker Compose
* PostgreSQL (can be run within Docker)

### Building the Application

1. Clone the repository: `git clone <repository_url>`
2. Navigate to the project directory: `cd accounts-payable-api`
3. Build the application using Maven: `mvn clean package`

### Running with Docker Compose

1. Navigate to the project directory: `cd accounts-payable-api`
2. Run Docker Compose: `docker-compose up -d`
3. The application will be accessible at `http://localhost:8080`
4. The database will be accessible at `localhost:5432` (if you expose the port)

## API Endpoints

The API documentation is available at `http://localhost:8080/swagger-ui.html` after the application starts.  It provides interactive access to all endpoints.  A summary of the key endpoints is below:

* **POST /api/accounts:** Create a new account.
* **GET /api/accounts:** Get a paginated list of accounts (with optional filters).
* **GET /api/accounts/{id}:** Get an account by ID.
* **PUT /api/accounts/{id}:** Update an account.
* **PATCH /api/accounts/{id}/status:** Update the status of an account.
* **GET /api/accounts/total-paid:** Get the total amount paid within a period.
* **DELETE /api/accounts/{id}:** Delete an account.
* **POST /api/accounts/import:** Import accounts from a CSV file.
* **POST /api/auth/authenticate:** Authenticate a user and receive a JWT.
* **POST /api/auth/register:** Register a new user.

## Authentication

The API uses JWT (JSON Web Token) for authentication.  You need to register a user using the `/api/auth/register` endpoint to obtain a JWT.  This JWT must be included in the `Authorization` header of subsequent requests (e.g., `Authorization: Bearer <token>`).

## Data Import (CSV)

The `/api/accounts/import` endpoint allows importing accounts from a CSV file.  The file should be uploaded as a multipart form data with the `file` parameter.  The CSV file should contain the following columns:

* `amount` (required)
* `dueDate` (required)
* `paymentDate` (optional)
* `description` (optional)
* `status` (optional)

**An example CSV file (`accounts_import_example.csv`) is provided in the `src/main/resources/data` directory.** This file demonstrates the expected format and can be used as a template for importing accounts.  It's crucial to adhere to this format for successful import.


The API will return a CSV file with the import results, including the status (SUCCESS/ERROR) and any error messages for each record.

## Error Handling

The API uses a centralized error handling mechanism.  Error responses are returned in JSON format with a timestamp, status code, and error message.  Validation errors are also handled and returned in a similar format.

## Database Schema

The database schema is managed by Flyway.  The `db/migration` directory contains the SQL scripts for creating and updating the database tables. The main table is named `account` and includes the fields described in the challenge.

## Deployment

The application can be deployed as a Docker container.  The provided `Dockerfile` and `docker-compose.yml` files simplify the deployment process.

## Future Enhancements

* Implement more robust error handling and logging.
* Add more comprehensive unit and integration tests.
* Implement input validation for CSV import.
* Consider adding caching for frequently accessed data.
* Implement security best practices.
* Explore asynchronous processing for CSV import.
* Add metrics and monitoring.