# Pagination & Input Validation Implementation Guide

## Overview
Added pagination support to GET endpoints and input validation to all POST/PUT request bodies. Invalid requests now return 400 with detailed error messages.

---

## 1. PAGINATION

### Implementation
Using Spring Data's `Pageable` with `PageRequest`:
- `page` - Zero-indexed page number (default: 0)
- `size` - Page size (default: 10)

### Endpoints with Pagination

#### GET /api/v1/users (Paginated)
```
GET http://localhost:8080/api/v1/users?page=0&size=10
```

**Query Parameters:**
- `page` (optional, default=0) - Page number starting from 0
- `size` (optional, default=10) - Records per page

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "fname": "John",
      "lname": "Doe",
      "email": "john@example.com",
      "phone": "123456789",
      "created_at": "2026-04-26T10:00:00"
    },
    {
      "id": 2,
      "fname": "Jane",
      "lname": "Smith",
      "email": "jane@example.com",
      "phone": "987654321",
      "created_at": "2026-04-26T11:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "size": 10,
  "number": 0,
  "sort": {
    "unsorted": true,
    "sorted": false,
    "empty": true
  },
  "numberOfElements": 2,
  "empty": false
}
```

#### GET /api/v1/courses (Paginated)
```
GET http://localhost:8080/api/v1/courses?page=0&size=5
```

**Response:** Same structure as users, but with `CourseDTO` objects.

---

## 2. INPUT VALIDATION

### Validation Rules

#### UserDTO Validation
| Field | Rules | Error Message |
|-------|-------|---------------|
| fname | @NotBlank, @Size(2-100) | "First name is required" / "must be between 2-100 chars" |
| lname | @NotBlank, @Size(2-100) | "Last name is required" / "must be between 2-100 chars" |
| email | @NotBlank, @Email | "Email is required" / "Email should be valid" |
| phone | @Size(max 20) | "Phone must not exceed 20 characters" |

#### CourseDTO Validation
| Field | Rules | Error Message |
|-------|-------|---------------|
| title | @NotBlank, @Size(3-200) | "Course title is required" / "must be between 3-200 chars" |
| description | @NotBlank, @Size(10-2000) | "Description is required" / "must be between 10-2000 chars" |
| pricelevel | @Size(max 50) | "Price level must not exceed 50 characters" |
| difficulty | @Size(max 50) | "Difficulty must not exceed 50 characters" |
| price | @NotNull, @DecimalMin(0), @DecimalMax(999999.99) | "Price is required" / "must be >= 0 and <= 999999.99" |
| thumbnail | @Size(max 500) | "Thumbnail URL must not exceed 500 characters" |

---

## 3. Error Responses

### 400 Bad Request - Validation Failed

**POST /api/v1/users (Invalid Data)**
```
POST http://localhost:8080/api/v1/users
Content-Type: application/json

{
  "fname": "J",
  "lname": "D",
  "email": "invalid-email",
  "phone": "123456789012345678901234567890"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Invalid Request",
  "status": 400,
  "message": "fname: First name must be between 2 and 100 characters, lname: Last name must be between 2 and 100 characters, email: Email should be valid, phone: Phone number must not exceed 20 characters",
  "timestamp": "2026-04-26T12:00:00",
  "path": "/api/v1/users"
}
```

**POST /api/v1/courses (Missing Required Fields)**
```
POST http://localhost:8080/api/v1/courses
Content-Type: application/json

{
  "title": "AB",
  "description": "Short",
  "price": -10
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Invalid Request",
  "status": 400,
  "message": "title: Title must be between 3 and 200 characters, description: Description must be between 10 and 2000 characters, price: Price must be greater than or equal to 0",
  "timestamp": "2026-04-26T12:00:00",
  "path": "/api/v1/courses"
}
```

### 404 Not Found

**GET /api/v1/users/999**
```json
{
  "error": "Resource Not Found",
  "status": 404,
  "message": "User not found with ID: 999",
  "timestamp": "2026-04-26T12:00:00",
  "path": "/api/v1/users/999"
}
```

---

## 4. Test Scenarios

### Pagination Tests

#### Test 1: Get First Page
```bash
GET /api/v1/users?page=0&size=10
Expected: First 10 users returned
```

#### Test 2: Get Second Page
```bash
GET /api/v1/users?page=1&size=10
Expected: Users 11-20 returned
```

#### Test 3: Small Page Size
```bash
GET /api/v1/courses?page=0&size=2
Expected: Only 2 courses per page
```

#### Test 4: Invalid Page (Should Still Work)
```bash
GET /api/v1/courses?page=999&size=10
Expected: Empty content array with proper pagination metadata
```

### Validation Tests

#### Test 1: Valid User Creation
```bash
POST /api/v1/users
{
  "fname": "John",
  "lname": "Doe",
  "email": "john@example.com",
  "phone": "+1234567890"
}
Expected: 200 OK (or appropriate success response)
```

#### Test 2: Missing First Name
```bash
POST /api/v1/users
{
  "fname": "",
  "lname": "Doe",
  "email": "john@example.com",
  "phone": "123"
}
Expected: 400 Bad Request with "First name is required"
```

#### Test 3: Email Format Invalid
```bash
POST /api/v1/users
{
  "fname": "John",
  "lname": "Doe",
  "email": "invalid-email",
  "phone": "123"
}
Expected: 400 Bad Request with "Email should be valid"
```

#### Test 4: First Name Too Short
```bash
POST /api/v1/users
{
  "fname": "J",
  "lname": "Doe",
  "email": "john@example.com",
  "phone": "123"
}
Expected: 400 Bad Request with "must be between 2 and 100 characters"
```

#### Test 5: Valid Course Creation
```bash
POST /api/v1/courses
{
  "title": "Advanced Java Programming",
  "description": "Learn advanced Java concepts and best practices",
  "pricelevel": "advanced",
  "difficulty": "hard",
  "price": 99.99,
  "thumbnail": "https://example.com/image.jpg"
}
Expected: 200 OK
```

#### Test 6: Course Title Too Short
```bash
POST /api/v1/courses
{
  "title": "AB",
  "description": "Learn advanced Java concepts and best practices",
  "price": 99.99
}
Expected: 400 Bad Request with "must be between 3 and 200 characters"
```

#### Test 7: Course Description Missing
```bash
POST /api/v1/courses
{
  "title": "Advanced Java Programming",
  "description": "Short",
  "price": 99.99
}
Expected: 400 Bad Request with "must be between 10 and 2000 characters"
```

#### Test 8: Price Out of Range
```bash
POST /api/v1/courses
{
  "title": "Advanced Java Programming",
  "description": "Learn advanced Java concepts and best practices",
  "price": -50
}
Expected: 400 Bad Request with "must be greater than or equal to 0"
```

---

## 5. Postman Examples

### Pagination Request
```
Method: GET
URL: http://localhost:8080/api/v1/users?page=0&size=5

No body required
No special headers needed
```

### Validation Request (Valid)
```
Method: POST
URL: http://localhost:8080/api/v1/users

Headers:
Content-Type: application/json

Body:
{
  "fname": "John",
  "lname": "Doe",
  "email": "john.doe@example.com",
  "phone": "9876543210"
}
```

### Validation Request (Invalid)
```
Method: POST
URL: http://localhost:8080/api/v1/users

Headers:
Content-Type: application/json

Body:
{
  "fname": "J",
  "lname": "D",
  "email": "not-an-email",
  "phone": "12345678901234567890123456789"
}

Expected Response: 400 Bad Request with multiple validation errors
```

---

## 6. Code Changes Summary

### Services Updated
- `UsersService.getUsers(Pageable)` - Returns `Page<UserDTO>`
- `CourseService.getCourses(Pageable)` - Returns `Page<CourseDTO>`

### Controllers Updated
- `UsersController.getUsers()` - Accepts `page` and `size` parameters
- `CourseController.getCourses()` - Accepts `page` and `size` parameters
- Both POST endpoints use `@Valid` annotation

### DTOs Updated
- `UserDTO` - Added validation annotations
- `CourseDTO` - Added validation annotations

### Repositories Updated
- `UsersRepository.findAll(Pageable)` - JPA pagination support
- `CourseRepository.findAll(Pageable)` - JPA pagination support

---

## 7. Testing Checklist

### Pagination
- [ ] `GET /api/v1/users?page=0&size=10` returns paginated results
- [ ] `GET /api/v1/courses?page=0&size=5` returns paginated results
- [ ] Response includes pagination metadata (totalPages, totalElements, etc.)
- [ ] Second page works correctly (`page=1`)
- [ ] Different page sizes work (size=5, 20, 50)

### Validation - Users
- [ ] Valid user creation succeeds
- [ ] Missing fname returns 400 with error message
- [ ] Invalid email returns 400
- [ ] Phone exceeding 20 chars returns 400
- [ ] First name < 2 chars returns 400

### Validation - Courses
- [ ] Valid course creation succeeds
- [ ] Missing title returns 400
- [ ] Title < 3 chars returns 400
- [ ] Description < 10 chars returns 400
- [ ] Negative price returns 400
- [ ] Price > 999999.99 returns 400

---

## Summary

✅ **Pagination**
- Page and size parameters supported
- Default page=0, size=10
- Works for `/users` and `/courses`

✅ **Input Validation**
- @NotBlank, @Email, @Size, @DecimalMin/Max annotations
- 400 Bad Request returned for invalid data
- Detailed error messages per field
- Applied to UserDTO and CourseDTO

✅ **Error Handling**
- Global exception handler catches validation errors
- Returns structured error response with timestamps
- Clear, actionable error messages

**Status: READY FOR TESTING** 🚀

