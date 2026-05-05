Write-Output "======================================"
Write-Output "SKILLVERSE API - COMPREHENSIVE TEST"
Write-Output "======================================"
Write-Output ""

$headers = @{"Content-Type" = "application/json"}
$passed = 0
$failed = 0
$failures = @()

function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [hashtable]$Headers,
        [string]$Body,
        [string]$Name,
        [int]$ExpectedStatus = 200
    )

    try {
        $params = @{
            Method = $Method
            Uri = $Url
            Headers = $Headers
            ErrorAction = 'Stop'
        }

        if ($Body) {
            $params['Body'] = $Body
        }

        $response = Invoke-WebRequest @params

        if ($response.StatusCode -eq $ExpectedStatus) {
            Write-Output "OK $Name ($($response.StatusCode))"
            return $true
        } else {
            Write-Output "FAIL $Name (Expected $ExpectedStatus, got $($response.StatusCode))"
            return $false
        }
    } catch {
        Write-Output "ERR $Name (ERROR: $($_.Exception.Message))"
        return $false
    }
}

# TEST 1: Create User
Write-Output "`n[1] USERS - CREATE"
$userBody = '{"fname":"John","lname":"Doe","email":"john.doe@example.com"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/users" -Headers $headers -Body $userBody -Name "POST /api/v1/users" -ExpectedStatus 201) { $passed++ } else { $failed++ }

# TEST 2: Get Users
Write-Output "`n[2] USERS - GET ALL"
$testUrl = "http://localhost:8080/api/v1/users?page=0&size=10"
if (Test-Endpoint -Method GET -Url $testUrl -Headers $headers -Name "GET /api/v1/users" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/users" }

# TEST 3: Get User by ID
Write-Output "`n[3] USERS - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/users/1" -Headers $headers -Name "GET /api/v1/users/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/users/1" }

# TEST 4: Create Course
Write-Output "`n[4] COURSES - CREATE"
$courseBody = '{"title":"Java Basics","description":"Learn Java programming from scratch","pricelevel":"beginner","difficulty":"easy","price":49.99,"thumbnail":"https://example.com/java.jpg"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/courses" -Headers $headers -Body $courseBody -Name "POST /api/v1/courses" -ExpectedStatus 201) { $passed++ } else { $failed++ }

# TEST 5: Get Courses
Write-Output "`n[5] COURSES - GET ALL"
$testUrl2 = "http://localhost:8080/api/v1/courses?page=0&size=10"
if (Test-Endpoint -Method GET -Url $testUrl2 -Headers $headers -Name "GET /api/v1/courses" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/courses" }

# TEST 6: Get Course by ID
Write-Output "`n[6] COURSES - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/courses/1" -Headers $headers -Name "GET /api/v1/courses/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/courses/1" }

# TEST 7: Enroll Student
Write-Output "`n[7] ENROLLMENTS - ENROLL STUDENT"
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/enrollments/enroll/1/1" -Headers $headers -Name "POST /enrollments/enroll/1/1" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /enrollments/enroll" }

# TEST 8: Get User Enrollments
Write-Output "`n[8] ENROLLMENTS - GET USER ENROLLMENTS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/enrollments/user/1" -Headers $headers -Name "GET /enrollments/user/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /enrollments/user/1" }

Write-Output ""
Write-Output "======================================"
Write-Output "TEST SUMMARY (8 core tests)"
Write-Output "======================================"
Write-Output "Passed: $passed"
Write-Output "Failed: $failed"

if ($failed -gt 0) {
    Write-Output ""
    Write-Output "FAILED ENDPOINTS:"
    foreach ($failure in $failures) {
        Write-Output "  - $failure"
    }
}

# TEST 1: Create User
Write-Output "`n[1] USERS - CREATE"
$userBody = '{"fname":"John","lname":"Doe","email":"john.doe@example.com"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/users" -Headers $headers -Body $userBody -Name "POST /api/v1/users" -ExpectedStatus 201) { $passed++ } else { $failed++ }

# TEST 2: Get Users
Write-Output "`n[2] USERS - GET ALL"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/users?page=0&size=10" -Headers $headers -Name "GET /api/v1/users" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/users" }

# TEST 3: Get User by ID
Write-Output "`n[3] USERS - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/users/1" -Headers $headers -Name "GET /api/v1/users/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/users/1" }

# TEST 4: Create Course
Write-Output "`n[4] COURSES - CREATE"
$courseBody = '{"title":"Java Basics","description":"Learn Java programming from scratch","pricelevel":"beginner","difficulty":"easy","price":49.99,"thumbnail":"https://example.com/java.jpg"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/courses" -Headers $headers -Body $courseBody -Name "POST /api/v1/courses" -ExpectedStatus 201) { $passed++ } else { $failed++ }

# TEST 5: Get Courses
Write-Output "`n[5] COURSES - GET ALL"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/courses?page=0&size=10" -Headers $headers -Name "GET /api/v1/courses" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/courses" }

# TEST 6: Get Course by ID
Write-Output "`n[6] COURSES - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/courses/1" -Headers $headers -Name "GET /api/v1/courses/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/courses/1" }

# TEST 7: Enroll Student
Write-Output "`n[7] ENROLLMENTS - ENROLL STUDENT"
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/enrollments/enroll/1/1" -Headers $headers -Name "POST /api/v1/enrollments/enroll/1/1" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /api/v1/enrollments/enroll" }

# TEST 8: Get User Enrollments
Write-Output "`n[8] ENROLLMENTS - GET USER ENROLLMENTS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/enrollments/user/1" -Headers $headers -Name "GET /api/v1/enrollments/user/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /api/v1/enrollments/user/1" }

# TEST 9: Create Lesson
Write-Output "`n[9] LESSONS - CREATE"
$lessonBody = '{"courseId":1,"title":"Introduction","video_url":"https://youtube.com/watch?v=abc123","order_index":1,"resource_file":"intro.pdf"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/courses/1/lessons" -Headers $headers -Body $lessonBody -Name "POST /api/v1/courses/1/lessons" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /courses/lessons" }

# TEST 10: Get Lessons
Write-Output "`n[10] LESSONS - GET ALL"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/courses/1/lessons" -Headers $headers -Name "GET /api/v1/courses/1/lessons" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /courses/lessons" }

# TEST 11: Get Lesson by ID
Write-Output "`n[11] LESSONS - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/courses/1/lessons/1" -Headers $headers -Name "GET /api/v1/courses/1/lessons/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /courses/lessons/1" }

# TEST 12: Update Lesson
Write-Output "`n[12] LESSONS - UPDATE"
$updateLessonBody = '{"courseId":1,"title":"Updated Intro","video_url":"https://youtube.com/watch?v=xyz789","order_index":1,"resource_file":"intro-updated.pdf"}'
if (Test-Endpoint -Method PUT -Url "http://localhost:8080/api/v1/courses/1/lessons/1" -Headers $headers -Body $updateLessonBody -Name "PUT /api/v1/courses/1/lessons/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "PUT /courses/lessons/1" }

# TEST 13: Create Quiz
Write-Output "`n[13] QUIZZES - CREATE"
$quizBody = '{"courseId":1,"title":"Quiz 1"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/quizzes" -Headers $headers -Body $quizBody -Name "POST /api/v1/quizzes" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /quizzes" }

# TEST 14: Get Quiz Questions
Write-Output "`n[14] QUIZZES - GET QUESTIONS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/quizzes/1/questions" -Headers $headers -Name "GET /api/v1/quizzes/1/questions" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /quizzes/questions" }

# TEST 15: Create Order
Write-Output "`n[15] ORDERS - CREATE"
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/orders?userId=1" -Headers $headers -Name "POST /api/v1/orders?userId=1" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /orders" }

# TEST 16: Add Item to Order
Write-Output "`n[16] ORDERS - ADD ITEM"
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/orders/1/items?courseId=1&price=49.99" -Headers $headers -Name "POST /api/v1/orders/1/items" -ExpectedStatus 201) { $passed++ } else { $failed++; $failures += "POST /orders/items" }

# TEST 17: Get Order
Write-Output "`n[17] ORDERS - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/orders/1" -Headers $headers -Name "GET /api/v1/orders/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /orders/1" }

# TEST 18: Get User Orders
Write-Output "`n[18] ORDERS - GET USER ORDERS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/orders/user/1" -Headers $headers -Name "GET /api/v1/orders/user/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /orders/user/1" }

# TEST 19: Get Orders by Status
Write-Output "`n[19] ORDERS - GET BY STATUS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/orders?status=PENDING" -Headers $headers -Name "GET /api/v1/orders?status=PENDING" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /orders?status" }

# TEST 20: Update Order Status
Write-Output "`n[20] ORDERS - UPDATE STATUS"
if (Test-Endpoint -Method PUT -Url "http://localhost:8080/api/v1/orders/1?status=COMPLETED" -Headers $headers -Name "PUT /api/v1/orders/1?status=COMPLETED" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "PUT /orders/status" }

# TEST 21: Initiate Payment
Write-Output "`n[21] PAYMENTS - INITIATE"
$paymentBody = '{"userId":1,"courseId":1,"amount":49.99,"paymentMethod":"PayHere"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/payments/initiate" -Headers $headers -Body $paymentBody -Name "POST /api/v1/payments/initiate" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "POST /payments/initiate" }

# TEST 22: Webhook Success
Write-Output "`n[22] PAYMENTS - WEBHOOK SUCCESS"
$webhookBody = '{"payment_id":"1","status_code":"2","amount":49.99,"merchant_id":"1234","order_id":"order1"}'
if (Test-Endpoint -Method POST -Url "http://localhost:8080/api/v1/payments/webhook/payhere-detailed" -Headers $headers -Body $webhookBody -Name "POST /api/v1/payments/webhook/payhere-detailed" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "POST /payments/webhook" }

# TEST 23: Get Payment
Write-Output "`n[23] PAYMENTS - GET BY ID"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/payments/1" -Headers $headers -Name "GET /api/v1/payments/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /payments/1" }

# TEST 24: Get User Payments
Write-Output "`n[24] PAYMENTS - GET USER PAYMENTS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/payments/user/1" -Headers $headers -Name "GET /api/v1/payments/user/1" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /payments/user/1" }

# TEST 25: Get All Payments
Write-Output "`n[25] PAYMENTS - GET ALL"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/payments" -Headers $headers -Name "GET /api/v1/payments" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /payments" }

# TEST 26: Admin Get All Users
Write-Output "`n[26] ADMIN - GET ALL USERS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/admin/users" -Headers $headers -Name "GET /api/v1/admin/users" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /admin/users" }

# TEST 27: Admin Get Course Stats
Write-Output "`n[27] ADMIN - GET COURSE STATS"
if (Test-Endpoint -Method GET -Url "http://localhost:8080/api/v1/admin/courses/stats" -Headers $headers -Name "GET /api/v1/admin/courses/stats" -ExpectedStatus 200) { $passed++ } else { $failed++; $failures += "GET /admin/stats" }

# TEST 28: Delete Lesson
Write-Output "`n[28] LESSONS - DELETE"
if (Test-Endpoint -Method DELETE -Url "http://localhost:8080/api/v1/courses/1/lessons/1" -Headers $headers -Name "DELETE /api/v1/courses/1/lessons/1" -ExpectedStatus 204) { $passed++ } else { $failed++; $failures += "DELETE /lessons" }

Write-Output ""
Write-Output "======================================"
Write-Output "TEST RESULTS"
Write-Output "======================================"
Write-Output "Passed: $passed/28"
Write-Output "Failed: $failed/28"

if ($failed -gt 0) {
    Write-Output ""
    Write-Output "FAILED ENDPOINTS:"
    foreach ($failure in $failures) {
        Write-Output "  - $failure"
    }
}

Write-Output ""
if ($failed -eq 0) {
    Write-Output "✓ ALL TESTS PASSED!"
} else {
    Write-Output "✗ $failed endpoint(s) need fixing"
}


