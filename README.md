# BandSync API

BandSync API is the backend service for the BandSync platform, a system designed to manage musical groups, artists, songs, setlists, events, roles, permissions, and user collaboration.

The application exposes a REST API built with Java and Spring Boot, and is designed to support both web and mobile clients by centralizing business logic, authentication, media storage, notifications, and persistence.

## Table of contents

- [BandSync API](#bandsync-api)
  - [Table of contents](#table-of-contents)
  - [Overview](#overview)
  - [Main Features](#main-features)
    - [Authentication and authorization](#authentication-and-authorization)
    - [Band and user management](#band-and-user-management)
    - [Content and media management](#content-and-media-management)
    - [Communication services](#communication-services)
    - [Operational capabilities](#operational-capabilities)
  - [Technology Stack](#technology-stack)
  - [Application Architecture](#application-architecture)
  - [Project Structure](#project-structure)
  - [Prerequisites](#prerequisites)
  - [Environment Configuration](#environment-configuration)
  - [Database and Persistence](#database-and-persistence)
  - [Build and Run](#build-and-run)
    - [1. Clone the repository](#1-clone-the-repository)
    - [2. Configure environment variables](#2-configure-environment-variables)
    - [3. Build the project](#3-build-the-project)
    - [4. Run the application](#4-run-the-application)
  - [Request and Response Examples](#request-and-response-examples)
    - [1. Register a user](#1-register-a-user)
    - [2. Login and obtain JWT token](#2-login-and-obtain-jwt-token)
    - [3. Get a list of musical bands](#3-get-a-list-of-musical-bands)
    - [4. Error response example](#4-error-response-example)
    - [Notes for API consumers](#notes-for-api-consumers)
  - [Health and Monitoring](#health-and-monitoring)
  - [Security Model](#security-model)
  - [Typical Use Cases](#typical-use-cases)
  - [Testing](#testing)
  - [Notes](#notes)
  - [Contact](#contact)

## Overview

BandSync is oriented to the management of music-related communities and artistic organizations. The platform allows:

- Managing users and user profiles
- Creating and organizing musical bands
- Assigning roles and permissions to members
- Managing artists, songs, repertoires, and setlists
- Creating events and scheduling related activities
- Sending email-based notifications and password recovery flows
- Uploading and storing media files in AWS S3
- Securing the API with JWT and Spring Security
- Supporting auditability and internal workflows for band operations

## Main Features

### Authentication and authorization

- JWT-based authentication
- Role and permission-based access control
- Two-factor authentication support
- Password reset token flow
- CORS configuration for frontend clients

### Band and user management

- Registration and profile management
- Musical band creation and administration
- Membership management across bands
- Roles assigned to users within the platform
- Default musical genres and default roles configuration

### Content and media management

- Artists catalog
- Songs and repertoires
- Setlists and relation with songs
- Event management
- Uploaded media assets stored in S3 buckets
- Media folders separated by users, logos, and songs

### Communication services

- Email delivery integration with SMTP
- Notifications for account and platform activities
- Frontend URL configuration for redirect flows

### Operational capabilities

- Actuator endpoints for health and metrics
- Cache layer with Caffeine
- Connection pool tuning for database access
- Rate limiting support for service protection
- Async processing support for heavier operations

## Technology Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Hibernate
- MySQL
- JWT (jjwt)
- Lombok
- MapStruct
- Thymeleaf
- Apache POI for spreadsheet export
- AWS SDK S3
- Caffeine Cache
- Mockito/Spring Boot test stack
- Maven

## Application Architecture

```mermaid
flowchart LR
    Client[Frontend / Mobile Client] --> API[Spring Boot API]
    API --> Security[Spring Security + JWT]
    API --> JPA[MySQL + JPA/Hibernate]
    API --> S3[AWS S3 Storage]
    API --> Mail[SMTP Email Service]
    API --> Cache[Caffeine Cache]
    API --> Actuator[Health / Metrics]
```

The backend is organized around a layered architecture:

- Controllers: expose REST endpoints
- Services: implement business logic
- Repositories: data access through JPA repositories
- Models: persistence entities
- Exceptions: centralized error handling
- Security layer: authentication, authorization, and request filtering

## Project Structure

```text
bandsyncapi/
├── database/
│   ├── initialData.sql
│   ├── tables.sql
│   └── migrations/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .env
├── HELP.md
└── README.md
```

## Prerequisites

Before running the project, ensure that you have:

- Java 25 or compatible JDK
- Maven 3.9+
- MySQL running locally or in a remote environment
- AWS credentials and S3 bucket configured
- SMTP email service credentials
- A configured `.env` file or equivalent environment variables

## Environment Configuration

The application reads its configuration from environment variables, and also imports `.env` automatically via Spring Boot.

The following variables are used in the project:

| Variable                     | Description                       |
| ---------------------------- | --------------------------------- |
| `PORT`                       | HTTP server port                  |
| `APP_NAME`                   | Application name                  |
| `DB_URL`                     | MySQL JDBC URL                    |
| `DB_USER`                    | MySQL username                    |
| `DB_PASSWORD`                | MySQL password                    |
| `SECRET_KEY_JWT`             | Secret used for JWT signing       |
| `AWS_ACCESS_KEY_ID`          | AWS access key                    |
| `AWS_SECRET_ACCESS_KEY`      | AWS secret key                    |
| `AWS_REGION`                 | AWS region                        |
| `AWS_BUCKET_NAME`            | Main S3 bucket                    |
| `AWS_BUCKET_USERS_DIRECTORY` | Users assets path                 |
| `AWS_BUCKET_LOGOS_DIRECTORY` | Logo assets path                  |
| `AWS_BUCKET_SONGS_DIRECTORY` | Song/media path                   |
| `MAIL_HOST`                  | SMTP host                         |
| `MAIL_PORT`                  | SMTP port                         |
| `MAIL_USERNAME`              | SMTP username                     |
| `MAIL_PASSWORD`              | SMTP password                     |
| `MAIL_AUTH`                  | SMTP auth flag                    |
| `MAIL_STARTTLS_ENABLE`       | STARTTLS activation               |
| `WEB_SITE_URL`               | Frontend base URL                 |
| `WEB_SITE_MUSICALBANDS_PATH` | Band-related frontend route path  |
| `MASTER_SECRET_KEY`          | Internal application secret       |
| `DEFAULT_MUSICAL_GENRES`     | Default genres list               |
| `DEFAULT_MUSICAL_ROLES`      | Default user roles                |
| `ALLOWED_ORIGINS`            | Allowed frontend origins for CORS |
| `APP_EMAIL`                  | Default application email         |

Example `.env` structure:

```env
PORT=8080
APP_NAME=BandSyncAPI
DB_URL=jdbc:mysql://localhost:3306/bandsync
DB_USER=root
DB_PASSWORD=your_password
SECRET_KEY_JWT=your_jwt_secret
AWS_ACCESS_KEY_ID=your_aws_key
AWS_SECRET_ACCESS_KEY=your_aws_secret
AWS_REGION=us-east-2
AWS_BUCKET_NAME=your-bucket
AWS_BUCKET_USERS_DIRECTORY=bandsync/dev/users
AWS_BUCKET_LOGOS_DIRECTORY=bandsync/dev/logos
AWS_BUCKET_SONGS_DIRECTORY=bandsync/dev/songs
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_email_password
MAIL_AUTH=true
MAIL_STARTTLS_ENABLE=true
WEB_SITE_URL=http://localhost:3000
WEB_SITE_MUSICALBANDS_PATH=musicalbands
MASTER_SECRET_KEY=your_master_secret
DEFAULT_MUSICAL_GENRES=Pop,Rock,Jazz,Blues
DEFAULT_MUSICAL_ROLES=Guitarrista,Bajista,Cantante
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
APP_EMAIL=your_email@gmail.com
```

## Database and Persistence

The project uses MySQL with JPA/Hibernate and stores the data model in the package structure under the API domain models.

The repository includes:

- SQL scripts under `database/`
- Initial seed data scripts
- Migration scripts under `database/migrations/`

This is useful for initializing schema and default data, especially during local or staging environment setup.

## Build and Run

### 1. Clone the repository

```bash
git clone https://github.com/CesarBaez28/bandsyncapi.git
cd bandsyncapi
```

### 2. Configure environment variables

Create a `.env` file or export variables in your shell before starting the application.

### 3. Build the project

```bash
./mvnw clean install
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

or run the packaged jar:

```bash
java -jar target/bandsyncapi-0.0.1-SNAPSHOT.jar
```

The default server port is configured from the `PORT` environment variable.

## Request and Response Examples

Below are practical examples of how the API can be consumed. These examples assume the backend is running locally on `http://localhost:8080`.

All responses follow the same envelope defined by `ApiResponse<T>`:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { "...": "..." },
  "errors": null,
  "timestamp": "2026-10-02T09:59:48.019571"
}
```

- `success`: indicates whether the request was processed successfully.
- `message`: a human-readable message for the operation result.
- `data`: the successful payload returned by the endpoint.
- `errors`: optional error payload; it is `null` on success.
- `timestamp`: server date/time of the response.

When a response is paginated, the `data` field contains a `PagedData<T>` object instead of a raw list:

```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "content": [
      { "id": 1, "name": "CebaMusic" }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "hasNext": false,
    "hasPrevious": false
  },
  "errors": null,
  "timestamp": "2026-10-02T09:59:48.019571"
}
```

### 1. Register a user

Request:

```http
POST /api/v1/users/register HTTP/1.1
Content-Type: application/json
Host: localhost:8080

{
  "name": "Cesar",
  "lastName": "Baez",
  "email": "cesar@example.com",
  "password": "StrongPass123!",
  "username": "cesarbaez"
}
```

Example response:

```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "name": "Cesar",
    "lastName": "Baez",
    "email": "cesar@example.com",
    "username": "cesarbaez"
  },
  "errors": null,
  "timestamp": "2026-10-02T09:59:48.019571"
}
```

### 2. Login and obtain JWT token

Request:

```http
POST /api/v1/users/login HTTP/1.1
Content-Type: application/json
Host: localhost:8080

{
  "email": "cesar@example.com",
  "password": "StrongPass123!"
}
```

Example response:

```json
{
  "success": true,
  "message": "User authenticated successfully",
  "data": {
    "id": 1,
    "username": "cesarbaez",
    "email": "cesar@example.com",
    "firstName": "Cesar",
    "lastName": "Baez",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.signature",
    "status": true
  },
  "errors": null,
  "timestamp": "2026-10-02T09:59:48.019571"
}
```

Use the token in the `Authorization` header for protected endpoints:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.signature
```

### 3. Get a list of musical bands

Request:

```http
GET /api/v1/musical-bands HTTP/1.1
Authorization: Bearer <token>
Host: localhost:8080
```

Example response:

```json
{
  "success": true,
  "message": "Musical bands retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "musical band",
        "description": "Band for live and studio sessions",
        "createdAt": "2026-10-02T12:30:00"
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "hasNext": false,
    "hasPrevious": false
  },
  "errors": null,
  "timestamp": "2026-10-02T09:59:48.019571"
}
```

### 4. Error response example

Request:

```http
GET /api/v1/users/999 HTTP/1.1
Authorization: Bearer <token>
Host: localhost:8080
```

Example response:

```json
{
  "success": false,
  "message": "User not found",
  "data": null,
  "errors": {
    "code": 404,
    "details": "The requested user does not exist"
  },
  "timestamp": "2026-10-02T15:20:30Z"
}
```

### Notes for API consumers

- All protected endpoints require a valid JWT token.
- Since each band has independent permissions, you must specify a header named `X-MUSICAL-BAND-ID` with the musical band's ID as the value. This is only necessary when performing operations for a specific musical band.
- The API returns JSON responses.
- Most POST/PUT requests use `Content-Type: application/json`.
- For file uploads, media endpoints usually use multipart form-data.

## Health and Monitoring

The application exposes actuator endpoints such as:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`

This makes it suitable for local diagnostics and production health checks.

## Security Model

The API is protected with:

- Spring Security
- JWT validation for authenticated requests
- CORS restrictions
- Role-based permission checks
- Password reset and email-based recovery flows
- Two-factor authentication support

## Typical Use Cases

The API is designed to support:

- Band membership management
- Artist and repertoire organization
- Event planning and scheduling
- Music-related content management
- Internal role administration and permissions
- User collaboration within artistic groups

## Testing

The project includes Spring Boot test dependencies and repository/controller tests under `src/test/java`.

Run the full test suite with:

```bash
./mvnw test
```

## Notes

- The project is backend-focused and expects a frontend or mobile client to consume the REST API.
- Storage of files and media is done through AWS S3.
- Database configuration is externalized through environment variables.
- The project is designed for modular extension, making it easy to add more endpoints or service capabilities.


## Contact

For project questions, contributions, or collaboration requests, use the repository associated with the project and its GitHub organization metadata.

---

BandSync API is intended to serve as the core backend for artist and music-business workflows, combining security, data management, media handling, and orchestration in one application.
