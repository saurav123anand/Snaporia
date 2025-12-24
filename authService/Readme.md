## AUTH SERVICE

🎯 Responsibility

### This Auth Service is only a TOKEN FACTORY.

# What it does NOT have
No JWT validation
No JwtAuthFilter
No @PreAuthorize
No business APIs
No gateway logic

# What it DOES have

## Login
Register
Refresh token (Redis)
Logout
User persistence
Password validation

Client → Auth Service
├── /auth/register
├── /auth/login
├── /auth/refresh
└── /auth/logout
JWT is issued here,
JWT is validated later at API Gateway.

✅ Registration works
✅ Login works
✅ JWT verified with Base64 secret
✅ Refresh token rotation works
✅ Multi-device login works
✅ Logout single device works
✅ Logout all devices works
✅ Redis data is readable
✅ Validation errors return 400