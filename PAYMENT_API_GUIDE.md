# PayHere Payment Integration - API Testing Guide

## Overview
This guide demonstrates how to test the PayHere payment flow integration with Postman.

## Payment Flow
1. **Student initiates payment** → POST `/api/v1/payments/initiate`
2. **Backend creates payment record** → Status: PENDING
3. **Student completes payment on PayHere** → PayHere redirects to success/failure page
4. **PayHere sends webhook** → POST `/api/v1/payments/webhook/payhere` or `/api/v1/payments/webhook/payhere-detailed`
5. **Backend confirms payment & enrolls student** → Enrollment record created automatically

## API Endpoints

### 1. Initiate Payment
**Endpoint:** `POST /api/v1/payments/initiate`

**Request Body:**
```json
{
  "userId": 1,
  "courseId": 2,
  "amount": 99.99,
  "paymentMethod": "PayHere"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Payment initiated successfully",
  "paymentId": 15,
  "txnReference": null,
  "amount": 99.99,
  "status": "PENDING"
}
```

### 2. PayHere Webhook (Detailed Format)
**Endpoint:** `POST /api/v1/payments/webhook/payhere-detailed`

**Request Body (from PayHere server):**
```json
{
  "merchant_id": "YOUR_MERCHANT_ID",
  "order_id": "ORDER123",
  "payment_id": "15",
  "status_code": "2",
  "amount": 99.99,
  "currency": "LKR",
  "custom_1": "user-ref-123",
  "md5sig": "hash-signature"
}
```

**PayHere Status Codes:**
- `0` = Initiated
- `2` = Completed (SUCCESS)
- `-1` = Failed
- `-2` = Cancelled

### 3. Get Payment by ID
**Endpoint:** `GET /api/v1/payments/{paymentId}`

**Example:** `GET /api/v1/payments/15`

**Response:**
```json
{
  "id": 15,
  "user": {
    "id": 1,
    "fname": "John",
    "lname": "Doe"
  },
  "course": {
    "id": 2,
    "title": "Advanced Java",
    "price": 99.99
  },
  "amount": 99.99,
  "paymentMethod": {
    "id": 1,
    "method": "PayHere"
  },
  "status": "SUCCESS",
  "txn_reference": "15",
  "paid_at": "2026-04-25T00:45:00",
  "created_at": "2026-04-25T00:40:00"
}
```

### 4. Get User's Payments
**Endpoint:** `GET /api/v1/payments/user/{userId}`

**Example:** `GET /api/v1/payments/user/1`

### 5. Get Payment by Transaction Reference
**Endpoint:** `GET /api/v1/payments/txn/{txnReference}`

**Example:** `GET /api/v1/payments/txn/TXN123456`

### 6. Get All Payments (Admin)
**Endpoint:** `GET /api/v1/payments`

---

## Orders API

### 1. Create Order
**Endpoint:** `POST /api/v1/orders?userId=1`

**Response:**
```json
{
  "success": true,
  "message": "Order created successfully",
  "orderId": 5,
  "userId": 1,
  "status": "PENDING",
  "total": 0.0
}
```

### 2. Add Item to Order
**Endpoint:** `POST /api/v1/orders/{orderId}/items?courseId=2&price=99.99`

**Response:**
```json
{
  "success": true,
  "message": "Item added to order successfully",
  "itemId": 10,
  "courseId": 2,
  "price": 99.99
}
```

### 3. Get Order by ID
**Endpoint:** `GET /api/v1/orders/{orderId}`

### 4. Get User's Orders
**Endpoint:** `GET /api/v1/orders/user/{userId}`

### 5. Get Orders by Status
**Endpoint:** `GET /api/v1/orders?status=PENDING`

### 6. Update Order Status
**Endpoint:** `PUT /api/v1/orders/{orderId}?status=COMPLETED`

---

## Complete Postman Test Scenario

### Step 1: Initiate Payment
```
POST http://localhost:8080/api/v1/payments/initiate
Headers: Content-Type: application/json

{
  "userId": 1,
  "courseId": 2,
  "amount": 99.99,
  "paymentMethod": "PayHere"
}
```
**Save the `paymentId` from response (e.g., 15)**

### Step 2: Simulate PayHere Webhook
```
POST http://localhost:8080/api/v1/payments/webhook/payhere-detailed
Headers: Content-Type: application/json

{
  "merchant_id": "1234567",
  "order_id": "ORDER123",
  "payment_id": "15",
  "status_code": "2",
  "amount": 99.99,
  "currency": "LKR",
  "custom_1": "user-ref-123",
  "md5sig": "dummy-signature"
}
```

### Step 3: Verify Payment Status
```
GET http://localhost:8080/api/v1/payments/15
```

**Expected Response:** Status should be "SUCCESS"

### Step 4: Verify Enrollment Was Created
```
GET http://localhost:8080/api/v1/enrollments/user/1
```

---

## Database Schema

### Tables Created:

1. **payment_method**
   - id (PK)
   - method (VARCHAR)

2. **orders**
   - id (PK)
   - user_id (FK)
   - total (DECIMAL)
   - status (VARCHAR)
   - created_at (TIMESTAMP)

3. **order_item**
   - id (PK)
   - orders_id (FK)
   - course_id (FK)
   - price (DECIMAL)

4. **payment**
   - id (PK)
   - user_id (FK)
   - course_id (FK)
   - amount (DECIMAL)
   - payment_method_id (FK)
   - status (VARCHAR)
   - txn_reference (VARCHAR)
   - paid_at (TIMESTAMP)
   - created_at (TIMESTAMP)

---

## Notes

- When a payment is confirmed with status "SUCCESS" or "COMPLETED", an enrollment record is automatically created
- The student is automatically enrolled in the course after successful payment
- All timestamps are stored in UTC
- The webhook endpoint validates the PayHere signature (implement actual validation in production)
- Transaction reference field can store PayHere's payment_id or custom reference

## Future Enhancements

1. Implement PayHere MD5 signature validation
2. Add payment retry mechanism
3. Implement refund processing
4. Add payment analytics dashboard
5. Implement recurring payments/subscriptions
6. Add webhook retry mechanism for failed notifications

