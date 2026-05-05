# SkillVerse API - Comprehensive Endpoint Testing Report

**Date:** May 2, 2026  
**Environment:** PostgreSQL (Fresh Database), Spring Boot 8080  
**Status:** TESTING IN PROGRESS

---

## ENDPOINT TESTING CHECKLIST

### Users Endpoints

#### 1. GET /api/v1/users (Paginated)
```
Method: GET
URL: http://localhost:8080/api/v1/users?page=0&size=10
Expected: 200 OK with user list
```

#### 2. POST /api/v1/users (Create User)
```
Method: POST
URL: http://localhost:8080/api/v1/users
Body: {"fname":"John","lname":"Doe","email":"john@example.com"}
Expected: 201 Created
```

#### 3. GET /api/v1/users/{id} (Get User by ID)
```
Method: GET
URL: http://localhost:8080/api/v1/users/1
Expected: 200 OK with user data
```

---

### Courses Endpoints

#### 4. GET /api/v1/courses (Paginated)
```
Method: GET
URL: http://localhost:8080/api/v1/courses?page=0&size=10
Expected: 200 OK with course list
```

#### 5. POST /api/v1/courses (Create Course)
```
Method: POST
URL: http://localhost:8080/api/v1/courses
Body: {"title":"Java 101","description":"Learn Java programming","pricelevel":"beginner","difficulty":"easy","price":49.99,"thumbnail":"https://..."}
Expected: 201 Created
```

#### 6. GET /api/v1/courses/{id} (Get Course by ID)
```
Method: GET
URL: http://localhost:8080/api/v1/courses/1
Expected: 200 OK with course data
```

---

### Enrollment Endpoints  

#### 7. POST /api/v1/enrollments/enroll/{studentId}/{courseId}
```
Method: POST
URL: http://localhost:8080/api/v1/enrollments/enroll/1/1
Expected: 201 Created
```

#### 8. GET /api/v1/enrollments/user/{userId}
```
Method: GET
URL: http://localhost:8080/api/v1/enrollments/user/1
Expected: 200 OK with enrollment list
```

---

### Lesson Endpoints

#### 9. POST /api/v1/courses/{courseId}/lessons
```
Method: POST
URL: http://localhost:8080/api/v1/courses/1/lessons
Body: {"courseId":1,"title":"Introduction","video_url":"https://youtube.com/...","order_index":1,"resource_file":"resource.pdf"}
Expected: 201 Created
```

#### 10. GET /api/v1/courses/{courseId}/lessons
```
Method: GET
URL: http://localhost:8080/api/v1/courses/1/lessons
Expected: 200 OK with lesson list
```

#### 11. GET /api/v1/courses/{courseId}/lessons/{lessonId}
```
Method: GET
URL: http://localhost:8080/api/v1/courses/1/lessons/1
Expected: 200 OK
```

#### 12. PUT /api/v1/courses/{courseId}/lessons/{lessonId}
```
Method: PUT
URL: http://localhost:8080/api/v1/courses/1/lessons/1
Body: {"courseId":1,"title":"Updated","video_url":"https://...","order_index":1,"resource_file":"..."}
Expected: 200 OK
```  

#### 13. DELETE /api/v1/courses/{courseId}/lessons/{lessonId}
```
Method: DELETE
URL: http://localhost:8080/api/v1/courses/1/lessons/1
Expected: 204 No Content
```

---

### Quiz Endpoints

#### 14. POST /api/v1/quizzes
```
Method: POST
URL: http://localhost:8080/api/v1/quizzes
Body: {"courseId":1,"title":"Quiz 1"}
Expected: 201 Created
```

#### 15. GET /api/v1/quizzes/{id}/questions
```
Method: GET
URL: http://localhost:8080/api/v1/quizzes/1/questions
Expected: 200 OK
```

---

### Order Endpoints (From Postman)

#### 16. POST /api/v1/orders?userId=1
```
Method: POST
URL: http://localhost:8080/api/v1/orders?userId=1
Expected: 200 OK
```

#### 17. POST /api/v1/orders/{orderId}/items?courseId=2&price=99.99
```
Method: POST
URL: http://localhost:8080/api/v1/orders/1/items?courseId=1&price=49.99
Expected: 200 OK
```

#### 18. GET /api/v1/orders/{id}
```
Method: GET
URL: http://localhost:8080/api/v1/orders/1
Expected: 200 OK
```

#### 19. GET /api/v1/orders/user/{userId}
```
Method: GET
URL: http://localhost:8080/api/v1/orders/user/1
Expected: 200 OK
```

#### 20. GET /api/v1/orders?status=PENDING
```
Method: GET
URL: http://localhost:8080/api/v1/orders?status=PENDING
Expected: 200 OK
```

#### 21. PUT /api/v1/orders/{id}?status=COMPLETED
```
Method: PUT
URL: http://localhost:8080/api/v1/orders/1?status=COMPLETED
Expected: 200 OK
```

---

### Payment Endpoints (From Postman)

#### 22. POST /api/v1/payments/initiate
```
Method: POST
URL: http://localhost:8080/api/v1/payments/initiate
Body: {"userId":1,"courseId":1,"amount":49.99,"paymentMethod":"PayHere"}
Expected: 200 OK
```

#### 23. POST /api/v1/payments/webhook/payhere-detailed
```
Method: POST
URL: http://localhost:8080/api/v1/payments/webhook/payhere-detailed
Body: {"payment_id":1,"status_code":"2","amount":49.99}
Expected: 200 OK
```

#### 24. GET /api/v1/payments/{id}
```
Method: GET
URL: http://localhost:8080/api/v1/payments/1
Expected: 200 OK
```

#### 25. GET /api/v1/payments/user/{userId}
```
Method: GET
URL: http://localhost:8080/api/v1/payments/user/1
Expected: 200 OK
```

#### 26. GET /api/v1/payments
```
Method: GET
URL: http://localhost:8080/api/v1/payments
Expected: 200 OK
```

---

### Admin Endpoints

#### 27. GET /api/v1/admin/users
```
Method: GET
URL: http://localhost:8080/api/v1/admin/users
Expected: 200 OK
```

#### 28. GET /api/v1/admin/courses/stats
```
Method: GET
URL: http://localhost:8080/api/v1/admin/courses/stats
Expected: 200 OK
```

---

## TOTAL ENDPOINTS TO TEST: 28

---

## TEST EXECUTION LOG

### Phase 1: Setup (✓ COMPLETE)
- [x] Fresh database created (create-drop mode)
- [x] Spring Boot app compiled and running
- [x] App accessible on port 8080
- [ ] Initial test data loaded

###Phase 2: Core Endpoints Testing (IN PROGRESS)  
- [ ] Test Users endpoints
- [ ] Test Courses endpoints
- [ ] Test Enrollments endpoints
- [ ] Test Lessons endpoints
- [ ] Test Quizzes endpoints

### Phase 3: Orders/Payments Testing (PENDING)
- [ ] Test Order endpoints
- [ ] Test Payment endpoints

### Phase 4: Error Handling (PENDING)
- [ ] Test 404 errors
- [ ] Test 400 validation errors
- [ ] Test 500 server errors

### Phase 5: Fix Broken Endpoints (PENDING)
- [ ] Fix any failing endpoints
- [ ] Retest all endpoints
- [ ] Verify 100% pass rate

---

## Known Issues to Fix

(To be populated with any failing endpoints)


