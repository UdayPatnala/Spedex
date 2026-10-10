# Changelog

All notable changes to the SpeDex project are documented in this file in accordance with Semantic Versioning and the SpeDex Universal Version Controller.

## [1.0.0] - 2026-09-30 (Internal Release: 2.07.00.0)

### Added
- **Core 18-Step User Loop**:
  - Secure Registration (`POST /api/auth/register`)
  - Login with JWT (`POST /api/auth/login`)
  - User Profile retrieval and update (`GET /api/me`, `PATCH /api/me`)
  - Add Transaction (`POST /api/transactions`) with type, amount, category, date, and payment method
  - Edit Transaction (`PUT /api/transactions/{id}`)
  - Delete Transaction (`DELETE /api/transactions/{id}`)
  - Transaction History with pagination, category filter, direction toggle, and instant search (`GET /api/transactions`)
  - Dynamic Category Breakdown (`GET /api/categories`)
  - Real-time Spending Summaries and velocity indicators
  - Saved Payment References directory (`POST /api/payment-references`, `GET /api/payment-references`, `PUT /api/payment-references/{id}`, `DELETE /api/payment-references/{id}`)
  - Personal Financial Dashboard with live metrics
  - RFC 4180 compliant CSV Data Export (`GET /api/export/transactions`)
  - Irreversible Account & Data Erasure (`POST /api/privacy/erase`)
  - Security & Privacy Settings with purpose-separated DPDP consent toggles
  - Secure Sign-Out token invalidation
- **Backend Architecture**:
  - `TransactionController`, `TransactionService`, and `TransactionRepository` with `JpaSpecificationExecutor`
  - `PaymentReferenceController` and `PaymentReferenceService` with URI scheme validation (`upi://`, `http://`, `https://`)
  - `CategoryController` returning default and custom categories
  - `ExportController` streaming CSV files with proper headers and escaping
  - PostgreSQL profile (`application-prod.properties`) for Render deployment with dynamic `DATABASE_URL` parsing and connection pooling
  - Integration test suite (`TransactionControllerIntegrationTest.java`) with 8 comprehensive test cases
- **Frontend Web Dashboard (`dashboard_app`)**:
  - Replaced blocking "Restoring your session..." screen with instant-rendering `LandingPage` as the default entry view
  - Dedicated `TransactionsView` with KPI cards, search bar, category pill filters, pagination controls, and CSV export button
  - `AddTransactionModal`, `EditTransactionModal`, and `DeleteConfirmModal`
  - Firebase SDK integration with Cloud Firestore (`db`) and Analytics telemetry (`src/firebase.ts`)
  - Automatic `VITE_API_BASE_URL` normalization ensuring clean `/api` routing
  - Vercel Single-Page-Application rewrite routing (`vercel.json`)
  - One-click demo credential autofill on login
- **Cloud & Deployment Automation**:
  - `render.yaml`: Blueprint specification for Render Docker web service
  - `Dockerfile`: Hardened with deterministic `spedex-backend.jar` target and dynamic `PORT` binding
  - Dynamic `server.port=${PORT:8080}` configured in Spring Boot properties
- **Documentation & Governance**:
  - Complete architecture, deployment, security, API, database, and testing guides in `docs/`
  - Architectural Decision Records in `docs/adr/001` through `006`

### Changed
- Enforced strict JWT authentication in `App.tsx` (removed artificial demo mock bypass)
- Swapped unauthenticated startup flow from a loading spinner card to the interactive `LandingPage`
- Synchronized all manifest versions across backend (`pom.xml`), dashboard (`package.json`), mobile (`package.json`), and `src/version.ts` to `2.07.00.0` / `v1.0.0-PROD`

### Security
- Strict IDOR tenant isolation across all transaction, trip, and payment reference operations (`403 Forbidden` on mismatch)
- Zero credential storage (never store or request UPI MPIN, card CVV, or banking passwords)
- Minor protection guardrails (<18 Financial Learning Mode with payment action blocking)
