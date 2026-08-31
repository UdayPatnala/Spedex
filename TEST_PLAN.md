# SpeDex Master Test Plan & Verification Strategy

## 1. Scope and Strategy
SpeDex employs a multi-tiered test strategy across the entire full-stack ecosystem:
1. **Tier 1: Unit & Domain Tests** — Isolated unit testing for business logic, helper functions, JWT utility methods, and mathematical analytics routines.
2. **Tier 2: Component & Integration Tests** — Integration testing for Spring Boot JPA repositories, REST controllers, React views, and React Native screen workflows.
3. **Tier 3: End-to-End API Tests** — Automated black-box validation of authentication, IDOR security boundaries, multi-user isolation, and Trip ledger lifecycle.
4. **Tier 4: Static Quality & Typechecks** — TypeScript compiler verification (`tsc --noEmit`) and Maven build verification.

---

## 2. Test Suites and Execution Commands

### 2.1 Backend (Spring Boot & JUnit 5)
- **Path**: `backend/`
- **Command**: `./mvnw.cmd test`
- **Coverage**: 37 tests covering:
  - `GlobalExceptionHandlerTest`: Validates 400, 401, 403, 404, 500 error mapping.
  - `JwtUtilTest` & `SecurityConfigTest`: Validates token issuing, expiration, and header filter chains.
  - `TripServiceTest`: Validates trip lifecycle, active-trip switching, and transaction aggregations.
  - `UserServiceTest`: Validates user registration, password hashing, and IDOR protection.

### 2.2 Dashboard Web App (Vitest & Testing Library)
- **Path**: `dashboard_app/`
- **Command**: `npm test`
- **Coverage**: 7 tests covering:
  - `App.test.tsx`: Direct workspace access, view navigation, transaction renders.
  - `api.test.ts`: Fetch error handling, auth headers, network retry/fallback.

### 2.3 Mobile Expo App (Jest & Jest-Expo)
- **Path**: `mobile/`
- **Command**: `npm test`
- **Coverage**: 4 test suites / 6 tests covering:
  - `AuthProvider.test.tsx`: Mobile auth state management and persistence.
  - `TripsScreen.test.tsx`: Trips creation, transaction modal, and category breakdown.
  - `PaymentConfirmScreen.test.tsx`: UPI intent confirmation workflows.
  - `helpers.test.ts`: Theme token and color calculations.

### 2.4 End-to-End Python Suite (71 Tests)
- **Path**: `test-trips-e2e.py`
- **Command**: `python test-trips-e2e.py`
- **Coverage**:
  - Full trip creation and auto-completion.
  - Cash vs digital payment methods.
  - Category breakdown accuracy.
  - Security checks: IDOR validation across trips and transactions (HTTP 403 checks).
  - Malformed payload rejection (HTTP 400 checks).
  - Unauthenticated access rejection (HTTP 401 checks).
