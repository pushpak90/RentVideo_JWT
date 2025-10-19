# RentVideo

**Simple RESTful Video Rental Service** — Spring Boot + MySQL

---

## Project Overview

RentVideo is a simplified video rental REST API built with Spring Boot. It demonstrates typical backend features: authentication & authorization (JWT), role-based access control (CUSTOMER, ADMIN), user registration/login with BCrypt password hashing, video management, and rental lifecycle (rent / return). The app persists data in MySQL using Spring Data JPA and uses Lombok to reduce boilerplate.

This repository is set up with Gradle (`gradlew`) and targets **Java 21**.

---

## Key Features

- User registration and login (email + password). Passwords hashed with BCrypt.
- JWT-based stateless authentication.
- Two roles: `CUSTOMER` (default) and `ADMIN`.
- Public endpoints: registration, login.
- Private endpoints: viewing videos, renting/returning videos.
- Admin-only endpoints: create/update/delete videos.
- Rental rules: each user may have **up to 2 active rentals**; attempting to rent a 3rd video will return an error.

---

## Tech Stack / Dependencies

- Java 21
- Spring Boot (Web, Security, Data JPA)
- MySQL (Connector/J driver)
- Lombok
- Gradle (wrapper `gradlew`)
- Spring JWT (implementation included in `Security` package)

Gradle dependencies (example):
```
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'mysql:mysql-connector-java'
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

---

## Project Structure

```
rentvideo/
├── src/main/java/com/crio/rentvideo
│   ├── Controller
│   ├── Dto
│   ├── Entity
│   ├── Repository
│   ├── Security
│   └── Service
├── src/main/resources
│   ├── application.properties
│   └── templates (if needed)
└── build.gradle
```

---

## Database Schema (conceptual)

### User
- `id` (PK)
- `email` (unique)
- `password` (BCrypt hashed)
- `first_name`
- `last_name`
- `role` (enum: `CUSTOMER`, `ADMIN`)

### Video
- `id` (PK)
- `title`
- `director`
- `genre`
- `available` (boolean)

### Rental
- `id` (PK)
- `user_id` (FK -> User)
- `video_id` (FK -> Video)
- `rented_at` (timestamp)
- `returned_at` (nullable timestamp)
- `status` (enum: `ACTIVE`, `RETURNED`)

Notes:
- When a user rents a video, a `Rental` row is created with `status=ACTIVE` and `rented_at` set. The related `Video.available` flips to `false`.
- Returning sets `returned_at`, `status=RETURNED`, and `Video.available` back to `true`.
- Business rule enforces `COUNT(active rentals for user) < 2` before allowing a new rental.

---

## Configuration (`application.properties` example)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentvideo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT properties
app.jwt.secret=ReplaceThisWithASecretKey
app.jwt.expiration-ms=86400000

# Server
server.port=8080
```

> **Security note:** Do not commit real secrets. Use environment variables or a secrets manager for `app.jwt.secret` and DB credentials.

---

## Getting Started (local)

### Prerequisites
- Java 21 installed
- MySQL running (create database `rentvideo`) or change DB name in `application.properties`
- `gradlew` is included in the project (use `./gradlew` on Unix/macOS, `gradlew.bat` on Windows)

### Steps
1. Create the database:
   ```sql
   CREATE DATABASE rentvideo;
   ```
2. Configure `src/main/resources/application.properties` with your MySQL credentials and JWT secret.
3. Build and run with Gradle:
   - Unix/macOS: `./gradlew bootRun`
   - Windows: `gradlew.bat bootRun`
4. The API will be available at `http://localhost:8080/`.

---

## API Endpoints (suggested)

### Public
- `POST /api/auth/register` — register a new user
  - Body: `{ "email": "a@b.com", "password": "pass123", "firstName": "John", "lastName": "Doe", "role": "CUSTOMER" }`
  - `role` optional; defaults to `CUSTOMER`

- `POST /api/auth/login` — login and receive JWT
  - Basic Auth or JSON credentials (depending on implementation). Returns `{ "token": "Bearer ..." }`

### Videos (public read)
- `GET /api/videos` — list all videos (only `available=true` or include filter)
- `GET /api/videos/{id}` — get details for a video

### Videos (ADMIN only)
- `POST /api/videos` — create video
- `PUT /api/videos/{id}` — update video
- `DELETE /api/videos/{id}` — delete video

### Rentals (authenticated users)
- `POST /api/rentals/rent` — rent a video
  - Body: `{ "videoId": 10 }` — server checks user active rentals < 2 and `video.available==true`.
- `POST /api/rentals/return` — return a video
  - Body: `{ "rentalId": 15 }` or `{ "videoId": 10 }` depending on endpoint design.
- `GET /api/rentals` — list current user's rentals

---

## Security Design Notes

* **Authentication:**

  * Uses **JWT (JSON Web Token)** for stateless authentication.
  * Tokens are issued during login and must be included in the `Authorization: Bearer <token>` header for all protected endpoints.
  * Implemented using a custom `JwtFilter` that validates tokens before processing secured requests.

* **Authorization:**

  * Configured using **Spring Security** with role-based access control.
  * Public endpoints (`/api/auth/**`) are accessible to everyone (registration & login).
  * Video-related endpoints are restricted by role:

    * `GET /api/videos/**` — accessible to both **CUSTOMER** and **ADMIN**.
    * `POST /api/video/**`, `PUT /api/video/**`, `DELETE /api/video/**` — restricted to **ADMIN** only.
  * Rental-related endpoints (`/api/rentals/**`) — accessible to both **CUSTOMER** and **ADMIN**.
  * All other endpoints require authentication.

* **Session Management:**

  * Configured as **stateless** using `SessionCreationPolicy.STATELESS`, ensuring no session data is stored server-side.

* **Password Security:**

  * Passwords are securely hashed using `BCryptPasswordEncoder` before being stored in the database.

* **Annotations Used:**

  * `@EnableWebSecurity` and `@EnableMethodSecurity` are used to activate Spring Security and method-level access control (`@PreAuthorize`).

**Design Decision:**

* The **CUSTOMER** role is the default for new users to separate administrative privileges.
* Both `CUSTOMER` and `ADMIN` roles can rent videos, but only `ADMIN` users can create, update, or delete video records.
* JWT-based stateless authentication ensures scalability and security without maintaining session state on the server.

---


