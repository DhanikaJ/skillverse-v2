# SkillVerse v2 — Backend API

**SkillVerse v2** is the backend service for the SkillVerse online learning platform. It provides RESTful APIs for authentication, course management, cart and checkout workflows, learner enrollment, lesson access, purchase history, instructor operations, and administrative reporting.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Node.js](https://img.shields.io/badge/Node.js-339933?logo=nodedotjs&logoColor=white)](https://nodejs.org/)
[![Express](https://img.shields.io/badge/Express-000000?logo=express&logoColor=white)](https://expressjs.com/)
<img alt="JPA" src="https://img.shields.io/badge/JPA-Hibernate-blue">
<img alt="Maven" src="https://img.shields.io/badge/Maven-Build-red">
[![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black)](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
[![REST API](https://img.shields.io/badge/API-REST-02569B?logo=api&logoColor=white)](https://restfulapi.net/)

## Frontend Repository

This backend powers the SkillVerse frontend client:

**Frontend:** [DhanikaJ/skillverse-ui](https://github.com/DhanikaJ/skillverse-ui)

The frontend and backend are separate cross-origin applications. The backend must allow requests from the frontend origin through CORS and expose a stable JSON API contract.

## Overview

SkillVerse v2 handles the server-side logic for the SkillVerse learning platform. It receives requests from the React frontend, validates user input, applies business rules, manages persistent data, and returns structured JSON responses.

The service is designed as a standalone API so it can support the current React frontend as well as future clients such as mobile applications, administrative dashboards, or third-party integrations.

## Features

### Authentication and Users

- User registration
- User login and session authentication
- Token-based authorization
- Role-based access for learners, instructors, and administrators
- Secure password storage

### Courses and Learning

- Course listing and retrieval
- Course detail endpoints
- Lesson and learning-content access
- Enrollment management
- Learner dashboard data
- Purchase history

### Commerce

- Shopping-cart operations
- Checkout workflow
- Order and purchase records
- Payment integration support

### Instructor and Admin

- Instructor course-management endpoints
- Administrative reporting endpoints
- Platform activity and business reporting data

## Technology Stack

| Layer | Technology |
|---|---|
| Runtime | Node.js |
| Web framework | Express.js |
| Language | JavaScript |
| API style | RESTful JSON API |
| Authentication | JWT |
| Database | PostgreSQL / MySQL / MongoDB, depending on project configuration |
| CORS | Cross-Origin Resource Sharing middleware |
| Environment configuration | dotenv |
| Deployment | Render, Railway, Fly.io, AWS, or VPS |
| License | MIT License |

## Architecture

```text
SkillVerse UI (React Frontend)
              │
              ▼
        CORS Middleware
              │
              ▼
       Express API Layer
              │
              ├── Authentication Middleware
              ├── Route Controllers
              ├── Business Services
              ├── Data Access Layer
              └── Database
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

## Project Structure

```text
skillverse-v2/
├── src/
│   ├── config/             # Database, environment, and app configuration
│   ├── controllers/        # Request handlers
│   ├── middleware/         # Authentication, authorization, error handling
│   ├── models/             # Database models and schemas
│   ├── routes/             # API route definitions
│   ├── services/           # Business logic
│   ├── utils/              # Helper functions
│   ├── app.js              # Express application setup
│   └── server.js           # Server entry point
├── .env                    # Local environment variables
├── package.json            # Dependencies and scripts
└── README.md               # Project documentation
```

## API Base URL

Local development:

```text
http://localhost:5000/api
```

Production:

```text
[https://your-backend-domain.com/api](https://your-backend-domain.com/api)
```

## API Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Authenticate a user | Public |
| `GET` | `/api/auth/me` | Get authenticated user profile | Authenticated |
| `GET` | `/api/courses` | Retrieve available courses | Public |
| `GET` | `/api/courses/:id` | Retrieve course details | Public |
| `GET` | `/api/cart` | Retrieve the user’s cart | Authenticated |
| `POST` | `/api/cart` | Add a course to the cart | Authenticated |
| `DELETE` | `/api/cart/:id` | Remove an item from the cart | Authenticated |
| `POST` | `/api/checkout` | Process a checkout request | Authenticated |
| `GET` | `/api/my-learning` | Retrieve enrolled courses | Authenticated |
| `GET` | `/api/lessons/:id` | Retrieve lesson content | Authenticated |
| `GET` | `/api/purchases` | Retrieve purchase history | Authenticated |
| `GET` | `/api/instructor/courses` | Retrieve instructor courses | Instructor |
| `GET` | `/api/admin/reports` | Retrieve administrative reports | Admin |

> Adjust paths and methods to match the actual route definitions in `src/routes/`.

## Prerequisites

- Node.js 18 or later
- npm or yarn
- Git
- A configured database
- A frontend client, preferably [SkillVerse UI](https://github.com/DhanikaJ/skillverse-ui)

## Installation

Clone the backend repository:

```bash
git clone [https://github.com/DhanikaJ/skillverse-v2.git](https://github.com/DhanikaJ/skillverse-v2.git)
cd skillverse-v2
```

Install dependencies:

```bash
npm install
```

## Environment Configuration

Create a `.env` file in the backend root:

```env
NODE_ENV=development
PORT=5000

DATABASE_URL=your_database_connection_string
JWT_SECRET=your_secure_jwt_secret
JWT_EXPIRES_IN=1d

CORS_ORIGIN=http://localhost:5173
```

For a deployed frontend, update CORS:

```env
CORS_ORIGIN=[https://your-frontend-domain.com](https://your-frontend-domain.com)
```

For multiple allowed frontend origins, configure an array in the application code:

```javascript
const allowedOrigins = process.env.CORS_ORIGIN.split(",");

app.use(
  cors({
    origin: allowedOrigins,
    credentials: true,
  })
);
```

Then use:

```env
CORS_ORIGIN=http://localhost:5173,[https://your-frontend-domain.com](https://your-frontend-domain.com)
```

## Running the Backend

Development mode:

```bash
npm run dev
```

Production mode:

```bash
npm start
```

The API should be available at:

```text
http://localhost:5000
```

## Connecting the Frontend

In the frontend repository, set:

```env
VITE_API_BASE_URL=http://localhost:5000/api
```

Then start the frontend:

```bash
npm run dev
```

Verify the connection by opening the frontend, registering or logging in, and confirming that course data loads from the backend.

## CORS Configuration

Because SkillVerse UI and SkillVerse v2 run on separate origins, CORS must be enabled on this backend.

Example Express configuration:

```javascript
const cors = require("cors");

app.use(
  cors({
    origin: process.env.CORS_ORIGIN,
    credentials: true,
  })
);
```

For local development, the frontend origin is commonly:

```text
http://localhost:5173
```

For production, use the exact deployed frontend domain, including the protocol:

```text
[https://skillverse-ui.example.com](https://skillverse-ui.example.com)
```

## Database Setup

Configure the database connection using `DATABASE_URL`.

Example PostgreSQL connection string:

```env
DATABASE_URL=postgresql://username:password@localhost:5432/skillverse
```

Example MongoDB connection string:

```env
DATABASE_URL=mongodb://localhost:27017/skillverse
```

If the project includes migration or seeding scripts, run them according to the configured scripts in `package.json`, for example:

```bash
npm run migrate
npm run seed
```

## Build and Deployment

Install production dependencies:

```bash
npm ci --omit=dev
```

Start the service:

```bash
npm start
```

Recommended production checklist:

- Use HTTPS only.
- Store secrets in the hosting platform’s environment variables.
- Use a managed database with automated backups.
- Enable request logging and error monitoring.
- Configure rate limiting for authentication endpoints.
- Restrict CORS to the production frontend domain.
- Use a process manager or container orchestration system.
- Run database migrations before deployment.
- Remove development-only dependencies from production builds.

## Security Considerations

- Hash passwords using bcrypt or Argon2.
- Use strong, randomly generated JWT secrets.
- Apply authentication middleware to protected routes.
- Enforce role-based authorization for instructor and admin endpoints.
- Validate and sanitize all request data.
- Use parameterized queries or ORM-safe query methods to prevent SQL injection.
- Protect against NoSQL injection when using MongoDB.
- Implement CSRF protection where cookie-based authentication is used.
- Apply rate limiting and account lockout mechanisms.
- Keep dependencies updated and scan for vulnerabilities.

## Testing

Run the test suite if configured:

```bash
npm test
```

Recommended test coverage:

- Authentication and authorization flows
- Course retrieval and enrollment
- Cart and checkout workflows
- Role-based access control
- Input validation and error responses
- Database service methods

## Roadmap

- Complete API integration tests
- Add refresh-token authentication
- Implement payment-provider webhooks
- Add course progress tracking
- Add instructor analytics endpoints
- Add course reviews and ratings
- Add notification and email services
- Add API documentation using Swagger or OpenAPI
- Add Docker and CI/CD deployment workflows

## Contributing

1. Fork the repository.
2. Create a feature branch:

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. Commit changes:

   ```bash
   git commit -m "feat: add course progress endpoint"
   ```

4. Push the branch:

   ```bash
   git push origin feature/your-feature-name
   ```

5. Open a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

**DhanikaJ**  
Portforlio: [DhanikaJ](https://DhanikaJ.dev)
