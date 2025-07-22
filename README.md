Overview
This service provides authentication and authorization functionality with role-based access control. It includes endpoints for user registration, login, token management, and role-based example endpoints.

Base URL
All endpoints are prefixed with /api/auth for authentication services or /example for role testing.

Authentication Endpoints
1. User Registration
Endpoint: PUT /api/auth/register

Request Body:

json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
Response:

Success: 200 OK with registered user details

Error: 4xx/5xx with error message

Description:
Registers a new user with the system. By default, new users receive the GUEST role.

2. User Login
Endpoint: POST /api/auth/login

Request Body:

json
{
  "username": "string",
  "password": "string"
}
Response:

Success: 200 OK with JWT tokens

Error: 401 Unauthorized for invalid credentials

Description:
Authenticates a user and returns access and refresh tokens.

3. Token Refresh
Endpoint: POST /api/auth/refresh_token

Request Body:

json
{
  "refreshToken": "string"
}
Response:

Success: 200 OK with new JWT tokens

Error: 401 Unauthorized for invalid/expired refresh token

Description:
Generates new access and refresh tokens using a valid refresh token.

4. User Logout
Endpoint: POST /api/auth/logout

Request Body:

json
{
  "refreshToken": "string"
}
Response:

Success: 200 OK with confirmation message

Error: 400 Bad Request for invalid token

Description:
Invalidates the provided refresh token, logging the user out.

5. Password Change
Endpoint: PATCH /api/auth/change_password

Request Body:

json
{
  "oldPassword": "string",
  "newPassword": "string"
}
Permissions: Authenticated users only

Response:

Success: 200 OK with confirmation message

Error: 400 Bad Request for invalid old password

Description:
Allows authenticated users to change their password.

6. Role Change
Endpoint: PATCH /api/auth/change_role

Request Body:

json
{
  "username": "string",
  "newRole": "string"
}
Permissions: ADMIN role required

Response:

Success: 200 OK with updated user details

Error: 403 Forbidden for non-admin users

Description:
Allows administrators to change user roles.

Role Testing Endpoints
1. Public Information
Endpoint: GET /example/base-info

Permissions: ADMIN, PREMIUM_USER, or GUEST role

Response:
"Everybody can see it"

2. Restricted Information
Endpoint: GET /example/not-guest

Permissions: ADMIN or PREMIUM_USER role

Response:
"Admins and premium users can see it"

3. Admin Only Information
Endpoint: GET /example/admin-only

Permissions: ADMIN role only

Response:
"Only admins can see it"

text
Authorization: Bearer <token>
Roles Hierarchy
ROLE_ADMIN - Full access
ROLE_PREMIUM_USER - Extended privileges
ROLE_GUEST - Basic access (default for new users)
