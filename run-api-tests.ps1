#!/usr/bin/env powershell
# Updated Comprehensive API Testing Script for Skillverse V2
# Fixed endpoint calls to use correct HTTP methods and parameters

$ErrorActionPreference = "SilentlyContinue"
$WarningPreference = "SilentlyContinue"

$BaseUrl = "http://localhost:8080"
$Results = @()
$IDs = @{}
$PassCount = 0
$FailCount = 0

function Test-Endpoint {
    param(
        [string]$TestName,
        [string]$Method,
        [string]$Endpoint,
        [object]$Body = $null,
        [int]$ExpectedStatus = 200
    )

    $Url = "$BaseUrl$Endpoint"

    try {
        $Params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
        }

        if ($Body) {
            $Params['Body'] = $Body | ConvertTo-Json -Depth 10
        }

        $Response = Invoke-WebRequest @Params
        $StatusCode = $Response.StatusCode

        $Result = @{
            TestName = $TestName
            Method = $Method
            Endpoint = $Endpoint
            StatusCode = $StatusCode
            Success = ($StatusCode -eq $ExpectedStatus)
            Content = $Response.Content
        }

        $Color = "Green"
    }
    catch {
        $StatusCode = $_.Exception.Response.StatusCode.value__

        $ErrorStream = $_.Exception.Response.GetResponseStream()
        $StreamReader = New-Object System.IO.StreamReader($ErrorStream)
        $ErrorContent = $StreamReader.ReadToEnd()

        $Result = @{
            TestName = $TestName
            Method = $Method
            Endpoint = $Endpoint
            StatusCode = $StatusCode
            Success = ($StatusCode -eq $ExpectedStatus)
            Content = $ErrorContent
        }

        $Color = if ($Result.Success) { "Green" } else { "Red" }
    }

    $Results += $Result

    if ($Result.Success) { $global:PassCount++ } else { $global:FailCount++ }

    Write-Host "[$($Result.StatusCode)] $TestName (Expected: $ExpectedStatus)" -ForegroundColor $Color

    return $Result
}

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Skillverse V2 - Comprehensive API Tests" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Health Check
Write-Host "1. Health Check" -ForegroundColor Yellow
Test-Endpoint -TestName "GET /hello" -Method "GET" -Endpoint "/hello" -ExpectedStatus 200 | Out-Null

Write-Host ""
Write-Host "2. User Management" -ForegroundColor Yellow

# Create User 1
$UserPayload1 = @{
    fname = "John"
    lname = "Doe"
    email = "john.doe@example.com"
}
$User1Response = Test-Endpoint -TestName "POST /api/v1/users (User 1)" -Method "POST" -Endpoint "/api/v1/users" -Body $UserPayload1 -ExpectedStatus 201
if ($User1Response.Success) {
    $User1Data = $User1Response.Content | ConvertFrom-Json
    $IDs['User1'] = $User1Data.id
    Write-Host "  -> User 1 ID: $($IDs['User1'])" -ForegroundColor Gray
}

# Create User 2
$UserPayload2 = @{
    fname = "Jane"
    lname = "Smith"
    email = "jane.smith@example.com"
}
$User2Response = Test-Endpoint -TestName "POST /api/v1/users (User 2)" -Method "POST" -Endpoint "/api/v1/users" -Body $UserPayload2 -ExpectedStatus 201
if ($User2Response.Success) {
    $User2Data = $User2Response.Content | ConvertFrom-Json
    $IDs['User2'] = $User2Data.id
    Write-Host "  -> User 2 ID: $($IDs['User2'])" -ForegroundColor Gray
}

# Get All Users
Test-Endpoint -TestName "GET /api/v1/users" -Method "GET" -Endpoint "/api/v1/users" -ExpectedStatus 200 | Out-Null

# Get User by ID
if ($IDs['User1']) {
    Test-Endpoint -TestName "GET /api/v1/users/$($IDs['User1'])" -Method "GET" -Endpoint "/api/v1/users/$($IDs['User1'])" -ExpectedStatus 200 | Out-Null
}

Write-Host ""
Write-Host "3. Course Management" -ForegroundColor Yellow

# Create Course 1
$CoursePayload1 = @{
    title = "Learn Java Programming"
    description = "Complete guide to master Java programming from basics to advanced concepts"
    pricelevel = "PREMIUM"
    difficulty = "BEGINNER"
    price = 49.99
    thumbnail = "https://example.com/java-course.jpg"
}
$Course1Response = Test-Endpoint -TestName "POST /api/v1/courses (Course 1)" -Method "POST" -Endpoint "/api/v1/courses" -Body $CoursePayload1 -ExpectedStatus 201
if ($Course1Response.Success) {
    $Course1Data = $Course1Response.Content | ConvertFrom-Json
    $IDs['Course1'] = $Course1Data.id
    Write-Host "  -> Course 1 ID: $($IDs['Course1'])" -ForegroundColor Gray
}

# Create Course 2
$CoursePayload2 = @{
    title = "Web Development Bootcamp"
    description = "Learn HTML, CSS, JavaScript, and React to build modern web applications"
    pricelevel = "PREMIUM"
    difficulty = "INTERMEDIATE"
    price = 79.99
    thumbnail = "https://example.com/web-course.jpg"
}
$Course2Response = Test-Endpoint -TestName "POST /api/v1/courses (Course 2)" -Method "POST" -Endpoint "/api/v1/courses" -Body $CoursePayload2 -ExpectedStatus 201
if ($Course2Response.Success) {
    $Course2Data = $Course2Response.Content | ConvertFrom-Json
    $IDs['Course2'] = $Course2Data.id
    Write-Host "  -> Course 2 ID: $($IDs['Course2'])" -ForegroundColor Gray
}

# Get All Courses
Test-Endpoint -TestName "GET /api/v1/courses" -Method "GET" -Endpoint "/api/v1/courses" -ExpectedStatus 200 | Out-Null

# Get Course by ID
if ($IDs['Course1']) {
    Test-Endpoint -TestName "GET /api/v1/courses/$($IDs['Course1'])" -Method "GET" -Endpoint "/api/v1/courses/$($IDs['Course1'])" -ExpectedStatus 200 | Out-Null
}

Write-Host ""
Write-Host "4. Enrollment Management" -ForegroundColor Yellow

# Enroll User in Course (using path parameters)
if ($IDs['User1'] -and $IDs['Course1']) {
    $EnrollResponse = Test-Endpoint -TestName "POST /api/v1/enrollments/enroll/$($IDs['User1'])/$($IDs['Course1'])" -Method "POST" -Endpoint "/api/v1/enrollments/enroll/$($IDs['User1'])/$($IDs['Course1'])" -ExpectedStatus 201
    if ($EnrollResponse.Success) {
        $EnrollData = $EnrollResponse.Content | ConvertFrom-Json
        $IDs['Enrollment1'] = $EnrollData.id
        Write-Host "  -> Enrollment ID: $($IDs['Enrollment1'])" -ForegroundColor Gray
    }
}

# Get User Enrollments
if ($IDs['User1']) {
    Test-Endpoint -TestName "GET /api/v1/enrollments/user/$($IDs['User1'])" -Method "GET" -Endpoint "/api/v1/enrollments/user/$($IDs['User1'])" -ExpectedStatus 200 | Out-Null
}

# Get Course Enrollments
if ($IDs['Course1']) {
    Test-Endpoint -TestName "GET /api/v1/enrollments/course/$($IDs['Course1'])" -Method "GET" -Endpoint "/api/v1/enrollments/course/$($IDs['Course1'])" -ExpectedStatus 200 | Out-Null
}

Write-Host ""
Write-Host "5. Order Management" -ForegroundColor Yellow

# Create Order (using query parameter)
if ($IDs['User1']) {
    $OrderResponse = Test-Endpoint -TestName "POST /api/v1/orders?userId=$($IDs['User1'])" -Method "POST" -Endpoint "/api/v1/orders?userId=$($IDs['User1'])" -ExpectedStatus 200

    if ($OrderResponse.Success) {
        $OrderData = $OrderResponse.Content | ConvertFrom-Json
        $IDs['Order1'] = $OrderData.orderId
        Write-Host "  -> Order ID: $($IDs['Order1'])" -ForegroundColor Gray

        # Add Item to Order (using query parameters)
        if ($IDs['Course1']) {
            Test-Endpoint -TestName "POST /api/v1/orders/$($IDs['Order1'])/items?courseId=$($IDs['Course1'])&price=49.99" -Method "POST" -Endpoint "/api/v1/orders/$($IDs['Order1'])/items?courseId=$($IDs['Course1'])&price=49.99" -ExpectedStatus 200 | Out-Null
        }

        # Get Order by ID
        Test-Endpoint -TestName "GET /api/v1/orders/$($IDs['Order1'])" -Method "GET" -Endpoint "/api/v1/orders/$($IDs['Order1'])" -ExpectedStatus 200 | Out-Null
    }
}

# Get User Orders
if ($IDs['User1']) {
    Test-Endpoint -TestName "GET /api/v1/orders/user/$($IDs['User1'])" -Method "GET" -Endpoint "/api/v1/orders/user/$($IDs['User1'])" -ExpectedStatus 200 | Out-Null
}

# Get Orders with Status Filter
Test-Endpoint -TestName "GET /api/v1/orders?status=PENDING" -Method "GET" -Endpoint "/api/v1/orders?status=PENDING" -ExpectedStatus 200 | Out-Null

# Update Order Status (using query parameter)
if ($IDs['Order1']) {
    Test-Endpoint -TestName "PUT /api/v1/orders/$($IDs['Order1'])?status=COMPLETED" -Method "PUT" -Endpoint "/api/v1/orders/$($IDs['Order1'])?status=COMPLETED" -ExpectedStatus 200 | Out-Null
}

Write-Host ""
Write-Host "6. Payment Management" -ForegroundColor Yellow

# Get All Payments
Test-Endpoint -TestName "GET /api/v1/payments" -Method "GET" -Endpoint "/api/v1/payments" -ExpectedStatus 200 | Out-Null

# Get Payments by User
if ($IDs['User1']) {
    Test-Endpoint -TestName "GET /api/v1/payments/user/$($IDs['User1'])" -Method "GET" -Endpoint "/api/v1/payments/user/$($IDs['User1'])" -ExpectedStatus 200 | Out-Null
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Test Summary" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

$Total = $PassCount + $FailCount

Write-Host "Total Tests: $Total" -ForegroundColor White
Write-Host "Passed: $PassCount" -ForegroundColor Green
Write-Host "Failed: $FailCount" -ForegroundColor $(if ($FailCount -eq 0) { "Green" } else { "Red" })
Write-Host ""

if ($FailCount -gt 0) {
    Write-Host "Failed Tests:" -ForegroundColor Red
    $Results | Where-Object { $_.Success -eq $false } | ForEach-Object {
        Write-Host "  [FAIL] $($_.TestName)" -ForegroundColor Red
        Write-Host "    $($_.Method) $($_.Endpoint)" -ForegroundColor Gray
        Write-Host "    Status: $($_.StatusCode)" -ForegroundColor Gray
        $ContentPreview = if ($_.Content.Length -gt 200) { $_.Content.Substring(0, 200) + "..." } else { $_.Content }
        Write-Host "    Response: $ContentPreview" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "Test completed at $(Get-Date)" -ForegroundColor Cyan

