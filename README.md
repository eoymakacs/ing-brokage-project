# Brokerage API Project

This project is a backend REST API developed with **Spring Boot**, designed for managing customers, assets, and orders in a brokerage system. 
The application supports secure authentication using JWT tokens and role-based authorization for `CUSTOMER` and `ADMIN` users.

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- Spring Boot-compatible IDE (e.g., Eclipse, IntelliJ)
- Postman (for API testing)

---

## Running the Application

1. **Clone the repository**:
   git clone https://github.com/<your-username>/ing-brokage-project.git
   cd ing-brokage-project

2. **Run the application** as a Spring Boot app:

   From your IDE: Right-click IngBrokageProjectApplication.java → Run as → Spring Boot App.

   Or via terminal:
   mvn spring-boot:run

3. The server will start on:
   http://localhost:8080

### Running Unit Tests
The following unit test file is used to test the functionality:
src/test/java/org/ing/test/AuthenticationControllerTest.java

Run this test from your IDE or via terminal: mvn test

### Authentication & Authorization

**Public Endpoints**
These endpoints can be accessed without authentication:

- POST /auth/signup – Register a new user
- POST /auth/login – Login and receive a JWT token

**Protected Endpoints**
All other API endpoints require a valid JWT token in the Authorization header:

Authorization: Bearer <your-token-here>

Tokens are provided in the response of a successful /auth/login request.

### API Testing with Postman
Start the application.

Use Postman to:

POST /auth/signup – Register a user (use role CUSTOMER or ADMIN)

POST /auth/login – Login and retrieve your JWT token

Add the token to your Postman headers:

Key: Authorization

Value: Bearer <your-token>

Call protected endpoints like /users, /assets, or /orders.
