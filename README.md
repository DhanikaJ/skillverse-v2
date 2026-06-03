# SkillVerse 2.0

<p align="center">
  <b>RESTful backend for managing users, courses, and enrollments</b><br/>
  Clean architecture • Relational modeling • Production-ready conventions
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-orange">
  <img alt="Spring Boot" src="https://img.shields.io/badge/SpringBoot-3.x-brightgreen">
  <img alt="JPA" src="https://img.shields.io/badge/JPA-Hibernate-blue">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-Build-red">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-lightgrey">
</p>

---

## 🚀 Overview
SkillVerse is a RESTful backend that manages **users, courses, and enrollments** featuring interactive API documentation via Swagger UI, **normalized data modeling**, and **layered architecture**.  
It demonstrates production patterns such as **idempotent endpoints**, **validation-ready DTOs**, and **clear separation of concerns**.

---

## 🧠 Architecture

```text
Client (Postman / Frontend)
        │
        ▼
Controller (HTTP layer: validation, mapping)
        │
        ▼
Service (business logic, invariants)
        │
        ▼
Repository (JPA / Hibernate)
        │
        ▼
PostgreSQL Database
```

<b>Design choices</b> 

- Controllers are thin; business rules live in services
- JPA entities model relationships (User ↔ Enrollment ↔ Course ↔ Payment)
- Exceptions bubble to a centralized `@ControllerAdvice` handler
- Schema managed via `ddl-auto=validate` in production
- Environment-driven configuration via `.env` and Spring Profiles

# ✨ Features
- **Authentication & Security** — JWT-based stateless authentication with Spring Security
- **Users API** — create & retrieve users with role-based access
- **Courses API** — manage courses, lessons, and quizzes
- **Enrollments** — automated link between users and courses (idempotent)
- **Payment Integration** — Complete PayHere payment gateway integration with webhooks
- **Input Validation** — Comprehensive request validation using Jakarta Bean Validation
- **Pagination & Filtering** — Optimized data retrieval for large datasets
- **File Uploads** — Cloudinary integration for course thumbnails and resources
- **Error Handling** — Global exception handler for consistent API responses
- **Documentation** — Interactive API documentation via Swagger UI
- **Deployment Ready** — Multi-profile configuration (dev/prod) for Railway deployment

# 📖 API Documentation
- Interactive API documentation is available via Swagger UI:
```text
http://localhost:8080/swagger-ui/index.html
```
- You can explore endpoints, view request/response schemas, and execute API calls directly from the browser.

# 🔌 API (sample)
```text
POST   /api/v1/users
POST   /api/v1/courses
POST   /api/v1/enrollments      # { "userId": 1, "courseId": 1 }
GET    /api/v1/users
GET    /api/v1/courses
GET    /api/v1/enrollments
```
Example: Create user
```text
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Dhanika","email":"dhanika@test.com"}'
```

Example response
```text
{
  "id": 1,
  "name": "Dhanika",
  "email": "dhanika@test.com"
}
```

# ⚙️ Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL / H2 (Test)
- PayHere Payment Gateway
- Cloudinary (File Storage)
- Maven
- OpenAPI / Swagger UI
- Railway (Deployment)

# ▶️ Run Locally
1) Clone
```text
git clone https://github.com/YOUR_USERNAME/skillverse-v2.git
cd skillverse-v2
```

2) Environment Variables
Create a `.env` file in the root directory:
```text
DATABASE_URL=jdbc:postgresql://localhost:5332/skillverse-v2
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your_secret_key
CLOUDINARY_CLOUD_NAME=your_cloud
CLOUDINARY_API_KEY=your_key
CLOUDINARY_API_SECRET=your_secret
```

3) Start app
```text
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

4) Test

Use Postman or curl with the endpoints above.

### 5. Access API Documentation
- Open Swagger UI:
```text
http://localhost:8080/swagger-ui/index.html
```

# 🖼️ Screenshots

<b>Postman<b/>

<img alt="Postman Create User" src="docs/postman_create_user.png">
<img alt="Postman Create Course" src="docs/postman_create_course.png">
<img alt="Postman Enrollment" src="docs/postman-enrollment.png">

<b>Database<b/>

<img alt="Postgre tables" src="docs/db-tables.png">
<img alt="Postgre Course" src="docs/db-courses.png">

### Swagger UI
![Swagger UI](docs/swagger.png)

# 🗂️ Project Structure
```text
src/main/java/com/skillverse
 ├── config          # Security, CORS, OpenAPI configs
 ├── controller      # REST Controllers (API endpoints)
 ├── service         # Business logic & integrations
 ├── repository      # Database access (JPA)
 ├── entity          # Database models
 ├── dto             # Data Transfer Objects (Request/Response)
 ├── security        # JWT & Auth filters
 └── exception       # Global error handling
```

# 🧪 Notes on Design
- **Security**: Stateless JWT-based security with password hashing (BCrypt)
- **Validation**: Strict input validation using `@Valid` and Request DTOs
- **Persistence**: Relational modeling with PostgreSQL and Hibernate optimizations
- **Scalability**: Pagination and filtering implemented for all major resource listings
- **Integrations**: Service-oriented architecture for external APIs (PayHere, Cloudinary)

# 🗺️ Roadmap
- [ ] Email Notifications (Order confirmation, Password reset)
- [ ] Analytics Dashboard for Admin users
- [ ] Multi-tenant support for different organizations
- [ ] Mobile App integration (React Native)
- [ ] Unit & Integration testing coverage expansion

<b>👤 Author<b/> - Dhanika Jagodaarachchi
