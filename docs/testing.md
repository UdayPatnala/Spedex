# SpeDex V1 Testing Strategy & Verification Guide

## Test Suite Inventory

SpeDex enforces a 4-tier verification protocol with 100% pass invariant across all tiers.

### Tier 1: Backend Integration & Unit Tests (Spring Boot)
- **Command**: `cd backend && ./mvnw.cmd test`
- **Scope**:
  - `TransactionControllerIntegrationTest`: 8 integration tests covering full transaction lifecycle, filtering, pagination, CSV streaming, and IDOR protection.
  - `TripServiceTest`: 12 tests covering expedition calculations, currency conversions, and auto-closure.
  - `PrivacyServiceTest`: 13 tests verifying DPDP consent, minor protection, and data erasure.
  - `UserServiceTest` & `JwtUtilTest`: 16 tests verifying authentication and token security.
  - Exception and configuration tests: 16 tests.
- **Total**: 65 / 65 passing.

### Tier 2: Frontend Web Dashboard Tests (Vitest + Testing Library)
- **Command**: `cd dashboard_app && npm test -- --run`
- **Scope**:
  - `App.test.tsx`: Authenticated vs unauthenticated routing, workspace navigation including Ledger.
  - `api.test.ts`: REST client endpoints, token attachment, error handling.
  - `PrivacyCenter.test.tsx`: Consent switches and DPDP dialogs.
- **Total**: 10 / 10 passing (and 0 Vite build errors).

### Tier 3: Mobile Client Tests (Jest + TypeScript)
- **Command**: `cd mobile && npm test && npx tsc --noEmit`
- **Scope**: Theme helpers, authentication provider, payment confirmation, privacy screen, trips screen.
- **Total**: 7 / 7 passing (0 type errors).

### Tier 4: Root End-to-End Test Suite (Python)
- **Command**: `python test-trips-e2e.py`
- **Scope**: Hermetic multi-client E2E verification of trips and transaction state across 77 assertions.
- **Total**: 77 / 77 passing.

### Grand Total
**159 / 159 tests passing (100% pass rate).**
