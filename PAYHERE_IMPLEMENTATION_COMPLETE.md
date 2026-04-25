# PayHere Payment Integration - Complete Implementation

## Overview
This implementation provides a complete PayHere payment flow integration for the Skillverse learning platform, including:
- Payment initiation endpoint
- PayHere webhook handling
- Automatic student enrollment on payment success
- Orders and order items management
- Complete database schema with all payment-related tables

## What Was Implemented

### 1. **Database Entities**

#### PaymentMethod Entity
- Stores available payment methods (PayHere, Credit Card, etc.)
- `payment_method` table created automatically

#### Orders Entity
- Represents student orders
- Linked to User (many orders per user)
- Contains order items
- Fields: id, user_id, total, status, created_at

#### OrderItem Entity
- Individual items in an order (courses)
- Linked to Orders (many items per order)
- Linked to Course
- Fields: id, orders_id, course_id, price

#### Payment Entity
- Records individual payment transactions
- Linked to User, Course, and PaymentMethod
- Fields: id, user_id, course_id, amount, payment_method_id, status, txnReference, paid_at, created_at

### 2. **Repositories**
- `PaymentRepository`: Query payments by user, status, transaction reference
- `OrdersRepository`: Query orders by user and status
- `OrderItemRepository`: Query order items by order
- `PaymentMethodRepository`: Find payment methods by name

### 3. **Services**

#### PaymentService
- `createPayment()`: Initiates a new payment
- `confirmPayment()`: Updates payment status (SUCCESS/FAILED/CANCELLED)
- `getPaymentByTxnReference()`: Retrieve payment by transaction ID
- `getUserPayments()`: Get all payments for a user
- **Auto-enrollment**: On payment success, automatically creates Enrollment record

#### OrdersService
- `createOrder()`: Create new order for user
- `addItemToOrder()`: Add course to order and update total
- `getOrderById()`: Retrieve order with items
- `getUserOrders()`: Get all orders for user
- `getOrdersByStatus()`: Filter orders by status
- `updateOrderStatus()`: Change order status

### 4. **Controllers**

#### PaymentController (`/api/v1/payments`)
- **POST** `/initiate` - Initiate payment (Student clicks Pay)
- **POST** `/webhook/payhere` - Webhook endpoint (alternative format)
- **POST** `/webhook/payhere-detailed` - Main webhook endpoint
- **GET** `/{paymentId}` - Get payment details
- **GET** `/user/{userId}` - Get user's payments
- **GET** `/txn/{txnReference}` - Get payment by transaction reference
- **GET** - Get all payments (admin)

#### OrdersController (`/api/v1/orders`)
- **POST** `?userId=X` - Create order
- **POST** `/{orderId}/items` - Add item to order
- **GET** `/{orderId}` - Get order details
- **GET** `/user/{userId}` - Get user's orders
- **GET** `?status=X` - Filter orders by status
- **PUT** `/{orderId}` - Update order status

### 5. **Payment Flow**

```
1. INITIATE
   ↓
   Student calls: POST /api/v1/payments/initiate
   ├─ userId: Student ID
   ├─ courseId: Course to purchase
   ├─ amount: Course price
   └─ paymentMethod: "PayHere"
   ↓
   Response: paymentId, txnReference, status=PENDING

2. PAYMENT GATEWAY
   ↓
   Student redirected to PayHere payment page
   ├─ Enters card details
   ├─ Completes payment
   └─ PayHere processes transaction

3. WEBHOOK
   ↓
   PayHere sends: POST /api/v1/payments/webhook/payhere-detailed
   ├─ payment_id: Payment ID
   ├─ status_code: 2 (SUCCESS), -1 (FAILED), -2 (CANCELLED)
   └─ amount: Transaction amount
   ↓
   Backend validates webhook

4. AUTO-ENROLLMENT
   ↓
   If status_code == 2 (SUCCESS):
   ├─ Update Payment.status = "SUCCESS"
   ├─ Set Payment.txnReference = payment_id
   ├─ Set Payment.paid_at = current timestamp
   └─ Create Enrollment record:
      ├─ user_id = student
      ├─ course_id = course
      ├─ enrolled_at = now
      ├─ progress = 0.0
      └─ status = "active"

5. CONFIRMATION
   ↓
   Student can verify:
   ├─ GET /api/v1/payments/{paymentId} → status = "SUCCESS"
   └─ GET /api/v1/enrollments → student enrolled in course
```

## PayHere Status Codes
- `0` = Initiated
- `2` = Completed (SUCCESS)
- `-1` = Failed
- `-2` = Cancelled

## Database Schema

### payment_method
```sql
CREATE TABLE payment_method (
    id SERIAL PRIMARY KEY,
    method VARCHAR(255) NOT NULL
);
```

### orders
```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    total DOUBLE PRECISION,
    status VARCHAR(255),
    created_at TIMESTAMP
);
```

### order_item
```sql
CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    orders_id INTEGER NOT NULL REFERENCES orders(id),
    course_id INTEGER NOT NULL REFERENCES course(id),
    price DOUBLE PRECISION
);
```

### payment
```sql
CREATE TABLE payment (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    course_id INTEGER NOT NULL REFERENCES course(id),
    amount DOUBLE PRECISION,
    payment_method_id INTEGER REFERENCES payment_method(id),
    status VARCHAR(255),
    txn_reference VARCHAR(255),
    paid_at TIMESTAMP,
    created_at TIMESTAMP
);
```

## Testing with Postman

### Step 1: Import Collection
1. Open Postman
2. Click "Import" 
3. Select `Skillverse_PayHere_Postman_Collection.json`
4. All endpoints will be imported

### Step 2: Test Payment Initiation
```bash
POST http://localhost:8080/api/v1/payments/initiate

{
  "userId": 1,
  "courseId": 2,
  "amount": 99.99,
  "paymentMethod": "PayHere"
}
```
**Response:**
```json
{
  "success": true,
  "message": "Payment initiated successfully",
  "paymentId": 1,
  "txnReference": null,
  "amount": 99.99,
  "status": "PENDING"
}
```
Save `paymentId` value.

### Step 3: Simulate PayHere Webhook
```bash
POST http://localhost:8080/api/v1/payments/webhook/payhere-detailed

{
  "merchant_id": "1234567",
  "order_id": "ORDER123",
  "payment_id": "1",
  "status_code": "2",
  "amount": 99.99,
  "currency": "LKR",
  "custom_1": "user-ref-123",
  "md5sig": "dummy-signature"
}
```
**Response:** `OK`

### Step 4: Verify Payment Status
```bash
GET http://localhost:8080/api/v1/payments/1
```
**Expected:** `status = "SUCCESS"`, `paid_at = current timestamp`

### Step 5: Verify Auto-Enrollment
```bash
GET http://localhost:8080/api/v1/enrollments/user/1
```
**Expected:** Student is enrolled in the course

## Key Files Created

### Models
- `PaymentMethod.java` - Payment method entity
- `Orders.java` - Order entity
- `OrderItem.java` - Order item entity
- `Payment.java` - Payment transaction entity

### Repositories
- `PaymentMethodRepository.java`
- `OrdersRepository.java`
- `OrderItemRepository.java`
- `PaymentRepository.java`

### Services
- `PaymentService.java` - Payment business logic
- `OrdersService.java` - Order management

### Controllers
- `PaymentController.java` - Payment endpoints
- `OrdersController.java` - Order endpoints

### Configuration
- `DataInitializationConfig.java` - Auto-initializes payment methods on startup

### Documentation
- `PAYMENT_API_GUIDE.md` - API documentation
- `Skillverse_PayHere_Postman_Collection.json` - Postman test collection

## Running the Application

1. Ensure PostgreSQL is running with the `skillverse-v2` database
2. Build the project:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   java -jar target/skillverse-v2-0.0.1-SNAPSHOT.jar
   ```
4. Application starts on `http://localhost:8080`
5. Webhook endpoints ready to receive PayHere callbacks

## Important Notes

1. **Payment Methods Auto-initialization**: On first run, payment methods are automatically created:
   - PayHere
   - Credit Card
   - Debit Card
   - PayPal
   - Bank Transfer

2. **Auto-Enrollment**: When a payment is confirmed (status_code=2), the student is automatically enrolled in the course

3. **Webhook Signature Validation**: The webhook signature validation is currently a stub (returns true). In production, implement proper MD5 signature verification with PayHere's merchant secret.

4. **Transaction Reference**: Currently uses PayHere's `payment_id`. Can be extended to use custom references.

5. **CORS Enabled**: Controllers have `@CrossOrigin(origins = "*", allowedHeaders = "*")` for development. Restrict in production.

## Future Enhancements

1. **PayHere Signature Validation**: Implement MD5 signature verification
2. **Refund Processing**: Add refund endpoints and logic
3. **Payment Analytics**: Dashboard for payment metrics
4. **Recurring Payments**: Subscription model support
5. **Webhook Retry Logic**: Retry failed webhook deliveries
6. **Email Notifications**: Send confirmation emails on successful payment
7. **Payment Gateway Abstraction**: Support multiple payment gateways
8. **Invoice Generation**: PDF invoice generation on successful payment

## Dependencies Used

- Spring Boot 4.0.5
- Spring Data JPA
- Hibernate 7.2.7
- PostgreSQL JDBC Driver

## Troubleshooting

**Port 8080 Already in Use**
```bash
# Find and kill process
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Database Connection Issues**
- Verify PostgreSQL is running
- Check connection string in `application.properties`
- Ensure database `skillverse-v2` exists

**Webhook Not Triggering**
- Check application logs for errors
- Verify PayHere is sending to correct endpoint
- Test webhook endpoint manually with Postman

## Support

For issues or questions, refer to:
- `PAYMENT_API_GUIDE.md` - Complete API documentation
- Application logs - Debug information
- Postman collection - Test endpoint examples

