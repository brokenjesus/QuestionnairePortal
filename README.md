# Questionnaire Portal - Backend API

A Spring Boot backend application for managing questionnaires with JWT authentication, WebSocket support, and email notifications.

## Features

- **User Authentication** (JWT)
- **Questionnaire Management** (CRUD operations)
- **Real-time Updates** (WebSocket)
- **Email Notifications**
- **Swagger API Documentation**
- **PostgreSQL Database**
- **Liquibase Migration**

## Prerequisites

- Java 17+
- PostgreSQL 15+
- Maven 3.8+
- SMTP server (Gmail configured in this example)

## Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-repo/questionnaire-portal.git
   cd questionnaire-portal
   ```

2. **Database Setup**

   - Create a PostgreSQL database named `questionnaire_portal`
   - Update database credentials in `application.properties`:
   
     ```properties
     spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
     spring.datasource.username=${DB_USERNAME}
     spring.datasource.password=${DB_PASSWORD}
     ```

3. **Configure Email**

   - Update SMTP settings in `application.properties`:
   
     ```properties
     spring.mail.username=${MAIL_USERNAME}
     spring.mail.password=${MAIL_PASSWORD}
     ```

4. **Configure JWT secret**

   - Update JWT secret in `application.properties`:
   
     ```properties
     jwt.token.secret=${JWT_SECRET}
     ```

5. **Build and Run**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## API Documentation

Swagger UI is available at:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Environment Variables

You can override these via environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── by/lupach/questionnaireportal/
│   │       ├── configs/       # Configuration classes
│   │       ├── controllers/   # REST controllers
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── exceptions/    # Custom exceptions
│   │       ├── filters/       # JWT filter
│   │       ├── handlers/      # Handlers
│   │       ├── models/        # Entity classes
│   │       ├── repositories/  # JPA repositories
│   │       ├── security/      # Security related
│   │       └── services/      # Business logic
│   └── resources/
│       ├── db/                # Liquibase migrations
│       └── application.properties
```

## Security

- JWT authentication
- Password encryption (BCrypt)
