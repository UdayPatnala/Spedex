# SpeDex V1 API Specification

## Base URL
- Local: `http://localhost:8080/api`
- Production: `https://<render-service-name>.onrender.com/api`

## Authentication
Add header to protected endpoints:
`Authorization: Bearer <jwt_token>`

---

## 1. Authentication & User Profile (`AuthController`)
- `POST /api/auth/register`: Create account (`name`, `email`, `password`, `age`).
- `POST /api/auth/login`: Authenticate and receive JWT token (`email`, `password`).
- `GET /api/auth/me`: Get current authenticated user profile, avatar, and account tier.
- `PATCH /api/auth/me`: Update display name or profile picture URL.

## 2. Dashboard Aggregated Bundle (`DashboardController`)
- `GET /api/dashboard`: Aggregated initial payload combining KPI counters, monthly budget progress, category distributions, recent transactions, and active trip status in a single network roundtrip.

## 3. Capabilities & Minor Protection Engine (`CapabilityController`)
- `GET /api/capabilities`: Evaluates server-side authorizations based on statutory age gating (<18) and DPDP consent:
  - `canInitiatePayment`: Boolean (false for minors).
  - `canViewAnalytics`: Boolean.
  - `canExportData`: Boolean.
  - `canRequestErasure`: Boolean.
  - `isMinor`: Boolean.

## 4. Transactions Ledger (`TransactionController`)
- `GET /api/transactions`: Paginated transaction list.
  - Query parameters: `page` (default 0), `size` (default 20), `type` (EXPENSE/INCOME), `category`, `keyword`.
- `POST /api/transactions`: Record new transaction.
  - Body: `{ amount, type, category, merchant, description, paymentMethod, paymentReference, occurredAt }`
- `GET /api/transactions/{id}`: Retrieve single transaction (with strict tenant isolation).
- `PUT /api/transactions/{id}`: Update transaction.
- `DELETE /api/transactions/{id}`: Remove transaction.

## 5. Categories (`CategoryController`)
- `GET /api/categories`: Returns list of system default and custom user categories with visual metadata (emojis, banknote palette color tokens).

## 6. Payment References Directory (`PaymentReferenceController`)
- `GET /api/payment-references`: List saved vendor and shop payment links.
- `POST /api/payment-references`: Create payment reference (`name`, `vpa`, `paymentUrl`, `qrCodeRef`, `notes`).
- `PUT /api/payment-references/{id}`: Update saved reference.
- `DELETE /api/payment-references/{id}`: Delete saved reference.

## 7. Payment Intent Preparation (`PaymentController`)
- `POST /api/payments/prepare`: Prepares UPI intent string and QR payload for adult transactors.
  - Returns `403 Forbidden` (`Payment actions restricted for minors`) if caller is under 18.

## 8. Trips Ledger Subsystem (`TripController`)
- `GET /api/trips`: List user travel expeditions sorted by active status and timestamp.
- `POST /api/trips`: Create new trip session (`name`, `currency`, `exchangeRate`). Automatically marks overlapping open trips as completed.
- `GET /api/trips/{id}`: Retrieve detailed trip breakdown, including cash vs. online expenditure splits, category breakdown, and total spent.
- `POST /api/trips/{id}/complete`: Explicitly close an ongoing trip session.
- `POST /api/trips/{id}/transactions`: Log an expense linked directly to a trip session.

## 9. Data Rights, Export & Privacy Center (`PrivacyController`, `ExportController`)
- `GET /api/export/transactions`: Streams RFC 4180 compliant CSV file of user transactions with ISO timestamps and quoted text fields.
- `GET /api/privacy/export`: Complete machine-readable JSON data export under DPDP Act 2023 Section 11.
- `GET /api/privacy/settings`: Retrieve current user privacy flags, retention settings, and guardian status.
- `POST /api/privacy/consent`: Update purpose-separated consent flags (`analytics_consent`, `marketing_consent`).
- `POST /api/privacy/guardian-consent`: Generate verifiable guardian consent verification links for minors.
- `GET /api/privacy/audit`: Retrieve immutable server-side privacy audit event ledger.
- `POST /api/privacy/grievance`: File formal privacy inquiry/complaint to Data Protection Officer (48h acknowledgement SLA).
- `GET /api/privacy/grievance`: List submitted grievance tickets and status.
- `POST /api/privacy/erase`: Permanent account erasure and anonymization (`{ "confirmationText": "DELETE MY DATA" }`).

## 10. Health & Readiness Probe (`HealthController`)
- `GET /api/health`: Liveness probe returning `{ "status": "UP" }`.
