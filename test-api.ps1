$baseUrl = "http://localhost:8080/api/v1"

Write-Host "=== SkillVerse API Comprehensive Test ===" -ForegroundColor Cyan

# 1. Create Users
Write-Host "`n[1] Creating Users..."
$user1Body = @{fname="Alice"; lname="Johnson"; email="alice@example.com"} | ConvertTo-Json
$r1 = Invoke-WebRequest -Uri "$baseUrl/users" -Method POST -ContentType "application/json" -Body $user1Body -UseBasicParsing -ErrorAction SilentlyContinue
$user1Id = ($r1.Content | ConvertFrom-Json).id
Write-Host "✓ User 1 Created (ID: $user1Id)"

$user2Body = @{fname="Bob"; lname="Smith"; email="bob@example.com"} | ConvertTo-Json
$r2 = Invoke-WebRequest -Uri "$baseUrl/users" -Method POST -ContentType "application/json" -Body $user2Body -UseBasicParsing -ErrorAction SilentlyContinue
$user2Id = ($r2.Content | ConvertFrom-Json).id
Write-Host "✓ User 2 Created (ID: $user2Id)"

# 2. Create Courses
Write-Host "`n[2] Creating Courses..."
$course1Body = @{
    title="Java Fundamentals"
    description="Learn Java programming from scratch"
    pricelevel="Beginner"
    difficulty="Easy"
    price=99.99
    thumbnail="https://example.com/java.png"
} | ConvertTo-Json
$r3 = Invoke-WebRequest -Uri "$baseUrl/courses" -Method POST -ContentType "application/json" -Body $course1Body -UseBasicParsing -ErrorAction SilentlyContinue
$course1Id = ($r3.Content | ConvertFrom-Json).id
Write-Host "✓ Course 1 Created (ID: $course1Id)"

$course2Body = @{
    title="Python Mastery"
    description="Advanced Python programming and best practices"
    pricelevel="Intermediate"
    difficulty="Medium"
    price=129.99
    thumbnail="https://example.com/python.png"
} | ConvertTo-Json
$r4 = Invoke-WebRequest -Uri "$baseUrl/courses" -Method POST -ContentType "application/json" -Body $course2Body -UseBasicParsing -ErrorAction SilentlyContinue
$course2Id = ($r4.Content | ConvertFrom-Json).id
Write-Host "✓ Course 2 Created (ID: $course2Id)"

# 3. Test User Endpoints
Write-Host "`n[3] Testing User Endpoints..."
$r = Invoke-WebRequest -Uri "$baseUrl/users" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
Write-Host "✓ GET /users - Status: $($r.StatusCode)"

$r = Invoke-WebRequest -Uri "$baseUrl/users/$user1Id" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
Write-Host "✓ GET /users/$user1Id - Status: $($r.StatusCode)"

# 4. Test Course Endpoints
Write-Host "`n[4] Testing Course Endpoints..."
$r = Invoke-WebRequest -Uri "$baseUrl/courses" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
Write-Host "✓ GET /courses - Status: $($r.StatusCode)"

$r = Invoke-WebRequest -Uri "$baseUrl/courses/$course1Id" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
Write-Host "✓ GET /courses/$course1Id - Status: $($r.StatusCode)"

# 5. Test Enrollment Endpoints
Write-Host "`n[5] Creating Enrollment..."
$enrollmentBody = @{userId=$user1Id; courseId=$course1Id} | ConvertTo-Json
$r5 = Invoke-WebRequest -Uri "$baseUrl/enrollments" -Method POST -ContentType "application/json" -Body $enrollmentBody -UseBasicParsing -ErrorAction SilentlyContinue
$enrollmentId = ($r5.Content | ConvertFrom-Json).id
Write-Host "✓ Enrollment Created (ID: $enrollmentId)"

$r = Invoke-WebRequest -Uri "$baseUrl/enrollments" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
Write-Host "✓ GET /enrollments - Status: $($r.StatusCode)"

# 6. Test Payment Endpoints
Write-Host "`n[6] Testing Payment Endpoints..."
$paymentBody = @{
    userId=$user1Id
    courseId=$course1Id
    amount=99.99
    paymentMethod="PayHere"
} | ConvertTo-Json
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/payments/initiate" -Method POST -ContentType "application/json" -Body $paymentBody -UseBasicParsing -ErrorAction SilentlyContinue
    Write-Host "✓ POST /payments/initiate - Status: $($r.StatusCode)"
} catch {
    Write-Host "✗ POST /payments/initiate - Error: $($_.Exception.Response.StatusCode)"
}

try {
    $r = Invoke-WebRequest -Uri "$baseUrl/payments" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
    Write-Host "✓ GET /payments - Status: $($r.StatusCode)"
} catch {
    Write-Host "✗ GET /payments - Error: $($_.Exception.Response.StatusCode)"
}

# 7. Test Order Endpoints
Write-Host "`n[7] Testing Order Endpoints..."
try {
    $r = Invoke-WebRequest -Uri "$baseUrl/orders?userId=$user1Id" -Method POST -UseBasicParsing -ErrorAction SilentlyContinue
    Write-Host "✓ POST /orders - Status: $($r.StatusCode)"
} catch {
    Write-Host "✗ POST /orders - Error: $($_.Exception.Response.StatusCode)"
}

try {
    $r = Invoke-WebRequest -Uri "$baseUrl/orders" -Method GET -UseBasicParsing -ErrorAction SilentlyContinue
    Write-Host "✓ GET /orders - Status: $($r.StatusCode)"
} catch {
    Write-Host "✗ GET /orders - Error: $($_.Exception.Response.StatusCode)"
}

Write-Host "`n=== Test Complete ===" -ForegroundColor Green

