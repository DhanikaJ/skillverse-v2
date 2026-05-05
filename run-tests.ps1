$headers = @{"Content-Type" = "application/json"}
$passed = 0
$failed = 0

Write-Output "Testing SkillVerse API Endpoints..."
Write-Output ""

# Test 1: Create User
try {
    $body = '{"fname":"John","lname":"Doe","email":"john.doe@example.com"}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/users" -Headers $headers -Method POST -Body $body
    if ($response.StatusCode -eq 201) { Write-Output "[PASS] POST /api/v1/users"; $passed++ } else { Write-Output "[FAIL] POST /api/v1/users"; $failed++ }
} catch { Write-Output "[ERROR] POST /api/v1/users"; $failed++ }

# Test 2: Get Users
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/users?page=0&size=10" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /api/v1/users"; $passed++ } else { Write-Output "[FAIL] GET /api/v1/users"; $failed++ }
} catch { Write-Output "[ERROR] GET /api/v1/users"; $failed++ }

# Test 3: Get User by ID
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/users/1" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /api/v1/users/1"; $passed++ } else { Write-Output "[FAIL] GET /api/v1/users/1"; $failed++ }
} catch { Write-Output "[ERROR] GET /api/v1/users/1"; $failed++ }

# Test 4: Create Course
try {
    $body = '{"title":"Java Basics","description":"Learn Java from scratch","pricelevel":"beginner","difficulty":"easy","price":49.99,"thumbnail":"https://example.com/java.jpg"}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/courses" -Headers $headers -Method POST -Body $body
    if ($response.StatusCode -eq 201) { Write-Output "[PASS] POST /api/v1/courses"; $passed++ } else { Write-Output "[FAIL] POST /api/v1/courses"; $failed++ }
} catch { Write-Output "[ERROR] POST /api/v1/courses"; $failed++ }

# Test 5: Get Courses
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/courses?page=0&size=10" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /api/v1/courses"; $passed++ } else { Write-Output "[FAIL] GET /api/v1/courses"; $failed++ }
} catch { Write-Output "[ERROR] GET /api/v1/courses"; $failed++ }

# Test 6: Get Course by ID
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/courses/1" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /api/v1/courses/1"; $passed++ } else { Write-Output "[FAIL] GET /api/v1/courses/1"; $failed++ }
} catch { Write-Output "[ERROR] GET /api/v1/courses/1"; $failed++ }

# Test 7: Enroll Student
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/enrollments/enroll/1/1" -Headers $headers -Method POST
    if ($response.StatusCode -eq 201) { Write-Output "[PASS] POST /enroll/1/1"; $passed++ } else { Write-Output "[FAIL] POST /enroll/1/1"; $failed++ }
} catch { Write-Output "[ERROR] POST /enroll/1/1: $($_.Exception.Message)"; $failed++ }

# Test 8: Get Enrollments
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/enrollments/user/1" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /enrollments/user/1"; $passed++ } else { Write-Output "[FAIL] GET /enrollments/user/1"; $failed++ }
} catch { Write-Output "[ERROR] GET /enrollments/user/1"; $failed++ }

# Test 9: Create Lesson
try {
    $body = '{"courseId":1,"title":"Intro","video_url":"https://youtube.com/watch?v=abc","order_index":1,"resource_file":"intro.pdf"}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/courses/1/lessons" -Headers $headers -Method POST -Body $body
    if ($response.StatusCode -eq 201) { Write-Output "[PASS] POST /courses/1/lessons"; $passed++ } else { Write-Output "[FAIL] POST /courses/1/lessons"; $failed++ }
} catch { Write-Output "[ERROR] POST /courses/1/lessons: $($_.Exception.Message)"; $failed++ }

# Test 10: Get Lessons
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/courses/1/lessons" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /courses/1/lessons"; $passed++ } else { Write-Output "[FAIL] GET /courses/1/lessons"; $failed++ }
} catch { Write-Output "[ERROR] GET /courses/1/lessons"; $failed++ }

# Test 11: Create Quiz
try {
    $body = '{"courseId":1,"title":"Quiz 1"}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/quizzes" -Headers $headers -Method POST -Body $body
    if ($response.StatusCode -eq 201) { Write-Output "[PASS] POST /api/v1/quizzes"; $passed++ } else { Write-Output "[FAIL] POST /api/v1/quizzes"; $failed++ }
} catch { Write-Output "[ERROR] POST /api/v1/quizzes: $($_.Exception.Message)"; $failed++ }

# Test 12: Get Quiz Questions
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/quizzes/1/questions" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /quizzes/1/questions"; $passed++ } else { Write-Output "[FAIL] GET /quizzes/1/questions"; $failed++ }
} catch { Write-Output "[ERROR] GET /quizzes/1/questions"; $failed++ }

# Test 13: Create Order
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/orders?userId=1" -Headers $headers -Method POST
    if ($response.StatusCode -eq 201 -or $response.StatusCode -eq 200) { Write-Output "[PASS] POST /api/v1/orders?userId=1"; $passed++ } else { Write-Output "[FAIL] POST /api/v1/orders?userId=1"; $failed++ }
} catch { Write-Output "[ERROR] POST /api/v1/orders?userId=1: $($_.Exception.Message)"; $failed++ }

# Test 14: Get Orders
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/orders/1" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /orders/1"; $passed++ } else { Write-Output "[FAIL] GET /orders/1"; $failed++ }
} catch { Write-Output "[ERROR] GET /orders/1"; $failed++ }

# Test 15: Initiate Payment
try {
    $body = '{"userId":1,"courseId":1,"amount":49.99,"paymentMethod":"PayHere"}'
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/payments/initiate" -Headers $headers -Method POST -Body $body
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] POST /payments/initiate"; $passed++ } else { Write-Output "[FAIL] POST /payments/initiate"; $failed++ }
} catch { Write-Output "[ERROR] POST /payments/initiate: $($_.Exception.Message)"; $failed++ }

# Test 16: Get Payments
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/payments" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write_Output "[PASS] GET /api/v1/payments"; $passed++ } else { Write-Output "[FAIL] GET /api/v1/payments"; $failed++ }
} catch { Write-Output "[ERROR] GET /api/v1/payments"; $failed++ }

# Test 17: Admin Get Users
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/admin/users" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /admin/users"; $passed++ } else { Write-Output "[FAIL] GET /admin/users"; $failed++ }
} catch { Write-Output "[ERROR] GET /admin/users"; $failed++ }

# Test 18: Admin Get Stats
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/admin/courses/stats" -Headers $headers -Method GET
    if ($response.StatusCode -eq 200) { Write-Output "[PASS] GET /admin/courses/stats"; $passed++ } else { Write-Output "[FAIL] GET /admin/courses/stats"; $failed++ }
} catch { Write-Output "[ERROR] GET /admin/courses/stats"; $failed++ }

Write-Output ""
Write-Output "====== SUMMARY ======"
Write-Output "Passed: $passed"
Write-Output "Failed: $failed"
Write-Output "Total: $($passed + $failed)"

