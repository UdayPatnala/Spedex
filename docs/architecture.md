# SpeDex V1 System Architecture

## 1. System Overview

SpeDex is structured as a two-repository decoupled full-stack architecture serving web clients with zero direct database exposure:

```
             USER
               │
               ▼
         ┌───────────┐
         │  Vercel   │
         │ SpeDex Web│ (React 19 + Vite 6 + TypeScript)
         └─────┬─────┘
               │
            HTTPS/API (JWT Bearer Auth)
               │
               ▼
         ┌───────────┐
         │  Render   │
         │ SpeDex API│ (Spring Boot 3 + Java 17)
         └─────┬─────┘
               │
         Secure DB connection (JDBC / SSL)
               │
               ▼
         ┌───────────┐
         │ PostgreSQL│
         │  Managed  │ (Render Managed PostgreSQL / Supabase)
         └───────────┘
```

## 2. Core Separation of Responsibilities

### Frontend (`dashboard_app` / `spe-dex-web`)
- **Technology**: React 19, Vite 6, TypeScript, Tailwind CSS, Lucide React, Firebase Analytics.
- **Responsibilities**:
  - Interactive UI/UX, responsive views, transaction forms, modal dialogues.
  - Client-side data filtering, sorting, and optimistic state updates.
  - JWT token storage in `localStorage` with authorization header injection.
  - Routing and fallback handling via `vercel.json`.
  - Zero direct database access (`DATABASE_URL` is never bundled into client assets).

### Backend (`backend` / `spe-dex-api`)
- **Technology**: Java 17, Spring Boot 3.4.4, Spring Security 6, Spring Data JPA, Hibernate, PostgreSQL driver.
- **Responsibilities**:
  - Centralized authentication and authorization (`JwtAuthenticationFilter`, `SecurityConfig`).
  - Strict resource ownership verification (`authenticatedUser.id == resource.userId`).
  - Business domain services (`TransactionService`, `PaymentReferenceService`, `TripService`, `PrivacyService`).
  - RFC 4180 streaming CSV generation and file export.
  - Server-side capability enforcement for adult vs minor transactors.

## 3. Communication Contract
- Transport: HTTPS JSON REST APIs (`/api/*`).
- Authentication: `Authorization: Bearer <JWT_TOKEN>`.
- Error Contract: Standardized RFC 7807 problem details or `{ "error": string, "message": string, "status": number }`.
- Public Endpoints: `/api/auth/**`, `/api/categories`, `/api/health`.
- Protected Endpoints: `/api/transactions/**`, `/api/payment-references/**`, `/api/trips/**`, `/api/privacy/**`, `/api/me`.
