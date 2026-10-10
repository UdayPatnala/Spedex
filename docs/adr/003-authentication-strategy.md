# ADR 003: Stateless JWT Authentication Strategy

## Status
Accepted

## Context
A decoupled architecture spanning web and mobile clients requires a stateless, scalable authentication mechanism that does not depend on server session affinity.

## Decision
Implement stateless JSON Web Token (JWT) authentication:
- Passwords hashed with BCrypt.
- `POST /api/auth/login` and `POST /api/auth/register` issue a signed HMAC-SHA256 JWT token.
- `JwtAuthenticationFilter` intercepts protected requests, validates the signature, and populates the `SecurityContext`.
- Client stores the token locally and includes it as an `Authorization: Bearer <token>` header.

## Consequences
- Highly scalable and mobile-friendly.
- The server does not maintain server-side session state for normal requests.
