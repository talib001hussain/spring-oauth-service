# Authentication Service API - cURL Commands

This file contains cURL commands for testing the Authentication Service API endpoints.

## Authentication Endpoints

### 1. Sign Up

Register a new user with email and password.

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Expected Response:
```
User registered successfully with email: user@example.com
```

### 2. Sign In

Authenticate a user and get a JWT token.

```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Expected Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "email": "user@example.com"
}
```

## User Profile Endpoints

For the following commands, you need to extract the token from the sign-in response and use it in the Authorization header.

### 3. Get User Profile

Retrieve the user's profile information.

```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Expected Response:
```json
{
  "userId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "name": "John Doe",
  "height": 180.0,
  "bmi": 24.5
}
```

### 4. Update User Profile

Update the user's profile information.

```bash
curl -X PUT http://localhost:8080/api/user/profile \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "John Doe",
    "height": 180.0,
    "bmi": 24.5
  }'
```

Expected Response:
```json
{
  "userId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "name": "John Doe",
  "height": 180.0,
  "bmi": 24.5
}
```

## Using the JWT Token

When you sign in, you'll receive a JWT token in the response. To use this token for authenticated endpoints:

1. Extract the token from the sign-in response
2. Add it to the Authorization header as a Bearer token

Example of extracting the token using jq (if available):

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }' | jq -r '.token')

echo $TOKEN
```

Then use the token in subsequent requests:

```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer $TOKEN"
```

## Testing with Windows PowerShell

If you're using Windows PowerShell, you may need to adjust the commands slightly:

```powershell
$headers = @{
    "Content-Type" = "application/json"
}

$body = @{
    email = "user@example.com"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/signin" -Method Post -Headers $headers -Body $body

$token = $response.token

$authHeaders = @{
    "Authorization" = "Bearer $token"
}

Invoke-RestMethod -Uri "http://localhost:8080/api/user/profile" -Method Get -Headers $authHeaders
```