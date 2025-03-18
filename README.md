# Spring Boot IAM Service

This document provides a detailed guide on setting up and using the **Spring Boot IAM Service**, a user and client identity management system built using **Spring Boot**.

## 🚀 Features

- **User Management**
  - Create, update, and delete users
  - Role-based access control (RBAC)
  - Multi-tenancy support
- **Client Management**
  - Register and manage clients
  - Enable/disable client accounts
- **User Authentication & Access Tokens**
  - User login with access token generation (JWT)
  - Secure authentication via Keycloak
- **Client Authentication & Access Tokens**
  - Client credentials authentication for API access
  - Secure token validation

## 📥 Installation

### Clone the repository

```bash
git clone https://github.com/arisculala/springboot-iam.git
cd springboot-iam
```

### Build and Run the Application

1. **Ensure you have the required dependencies installed**

   - Java 17+
   - Maven 3+
   - PostgreSQL (or your preferred database)
   - Keycloak (for authentication)

2. **Configure the application**

   - Update `application.yml` with your database and Keycloak settings.

3. **Run the application**

```bash
mvn clean install
mvn spring-boot:run
```

## 🛠 API Endpoints

### 🔹 User Management

| Method  | Endpoint                                       | Description          |
| ------- | ---------------------------------------------- | -------------------- |
| `PUT`   | `/api/v1/users`                                | Create a new user    |
| `GET`   | `/api/v1/users/{userId}`                       | Get user by ID       |
| `GET`   | `/api/v1/users/email?email={email}`            | Get user by email    |
| `GET`   | `/api/v1/users/username?username={username}`   | Get user by username |
| `PATCH` | `/api/v1/users/{userId}/disable?disabled=true` | Enable/disable user  |
| `PATCH` | `/api/v1/users/{userId}/password`              | Update user password |

### 🔹 Client Management

| Method  | Endpoint                                           | Description                |
| ------- | -------------------------------------------------- | -------------------------- |
| `PUT`   | `/api/v1/clients/register`                         | Register a new client      |
| `GET`   | `/api/v1/clients/{clientId}`                       | Get client details by ID   |
| `GET`   | `/api/v1/clients`                                  | Get all registered clients |
| `PATCH` | `/api/v1/clients/{clientId}/disable?disabled=true` | Enable/disable client      |

### 🔹 Authentication & Access Tokens

| Method | Endpoint                   | Description                       |
| ------ | -------------------------- | --------------------------------- |
| `POST` | `/api/v1/auth/login`       | User login (returns access token) |
| `POST` | `/api/v1/clients/validate` | Validate client credentials       |

## 🛡 Security & Authentication

- Uses **Keycloak** for authentication and token management.
- Implements **JWT** for secure access tokens.
- Enforces **role-based access control (RBAC)**.
- Supports **multi-tenancy** for organizations.

## 🏗 Future Enhancements

- Implement OAuth2 authorization flows.
- Add support for password reset and MFA.
- Extend logging and monitoring capabilities.

## 📄 License

This project is licensed under the **MIT License**.

---

Enjoy using **Spring IAM Service**! 🚀 If you have any questions, feel free to reach out. 😊
