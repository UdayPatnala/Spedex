# Project Constitution & Specification: SpeDex

## 1. Product Identity & Vision
SpeDex (**Speed Index** + **Spending Index**) is an enterprise-grade financial management platform designed for high-frequency personal finance tracking, automated multi-currency trip expense ledgers, instant UPI vendor directories, and velocity analytics.

### Core Value Proposition
- **High-Velocity Expense Tracking**: Fast, zero-friction logging with automatic category categorization and cash vs digital tagging.
- **Smart Trip Ledgers**: Automated session-based travel budgets that aggregate cash/card splits, vendor transactions, and category distributions without cluttering daily living expenses.
- **Indian Rupee (INR) Aesthetic**: Curated color palette reflecting Indian Rupee banknotes paired with classic Cormorant Garamond headings and modern Sora body typefaces.
- **Enterprise IDOR & Security Hardening**: Strict user isolation across all endpoints ensuring unauthorized data access attempts receive `403 Forbidden` ("Access denied").

---

## 2. Technical Architecture

### 2.1 Backend Layer (`backend/`)
- **Runtime**: Java 17 / Spring Boot 3.x
- **Persistence**: Spring Data JPA / Hibernate with H2 (in-memory dev/test) and PostgreSQL support.
- **Security**: Spring Security filter chain with JWT Bearer authentication, BCrypt password hashing, and resource-ownership validation across all service methods.

### 2.2 Dashboard Web Application (`dashboard_app/`)
- **Runtime**: React 19 + TypeScript 5.8 + Vite 6
- **Architecture**: Modular component structure with responsive glassmorphism styles, live Mobile Sync Simulator, QR code generation, and CSV ledger exports.

### 2.3 Mobile Ecosystem
- **Cross-Platform App (`mobile/`)**: Expo SDK 53 / React Native with async storage, biometric auth stubs, touch-optimized layouts, and full feature parity with web.
- **Native Android (`mobile_native_android/`)**: Kotlin Jetpack Compose application with Room local database and Material 3 design tokens.

---

## 3. REST API Interface Contracts

### Authentication
- `POST /api/auth/register` — Create user account, returns JWT and user payload.
- `POST /api/auth/login` — Authenticate credentials, returns JWT.
- `GET /api/auth/me` — Retrieve current authenticated user profile.
- `PUT /api/auth/profile` — Update user profile details.

### Trips Ledger
- `GET /api/trips` — List all trips for current authenticated user.
- `POST /api/trips` — Start a new trip (automatically closes any active trip).
- `GET /api/trips/{id}` — Retrieve trip summary, transaction list, and category breakdown.
- `POST /api/trips/{id}/complete` — Mark trip as completed.
- `POST /api/trips/{id}/transactions` — Add an expense transaction directly to a trip.

### Dashboard & Analytics
- `GET /api/dashboard/bundle` — Aggregated dashboard payload (user, overview, vendors, budgets, analytics).
- `GET /api/dashboard/overview` — Overview metrics and recent transactions.
- `GET /api/vendors` — Directory of UPI payees and default payment rules.
- `POST /api/vendors` — Register a new vendor.
- `GET /api/budget/screen` — Budget metrics, category progress, and savings tips.
- `GET /api/analytics` — Spending velocity, category percentages, and day-of-week breakdown.

---

## 4. Security & Access Control Invariants
1. **Ownership Enforcement**: Every query for trips, transactions, budgets, or vendors must filter by `user_id`. Attempting to read or mutate another user's resource throws `AccessDeniedException` (HTTP 403).
2. **Deterministic Response Codes**:
   - Valid request: `200 OK` or `201 Created`
   - Unauthenticated: `401 Unauthorized`
   - Unauthorized access / IDOR attempt: `403 Forbidden`
   - Missing resource: `404 Not Found`
   - Invalid input: `400 Bad Request`
