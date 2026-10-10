# SpeDex V1 API Specification

## Base URL
- Local: `http://localhost:8080/api`
- Production: `https://<render-service-name>.onrender.com/api`

## Authentication
Add header to protected endpoints:
`Authorization: Bearer <jwt_token>`

---

## 1. Authentication & User Profile
- `POST /api/auth/register`: Create account (`name`, `email`, `password`, `age`)
- `POST /api/auth/login`: Authenticate and receive JWT token (`email`, `password`)
- `GET /api/me`: Get current authenticated user profile and capability summary
- `PATCH /api/me`: Update display name or profile details

## 2. Transactions
- `GET /api/transactions`: Paginated transaction list.
  - Query parameters: `page` (default 0), `size` (default 20), `type` (EXPENSE/INCOME), `category`, `keyword`.
- `POST /api/transactions`: Record new transaction.
  - Body: `{ amount, type, category, merchant, description, paymentMethod, paymentReference, occurredAt }`
- `GET /api/transactions/{id}`: Retrieve single transaction.
- `PUT /api/transactions/{id}`: Update transaction.
- `DELETE /api/transactions/{id}`: Remove transaction.

## 3. Categories
- `GET /api/categories`: Returns list of system default and custom user categories.

## 4. Payment References
- `GET /api/payment-references`: List saved vendor and shop payment links.
- `POST /api/payment-references`: Create payment reference (`name`, `vpa`, `paymentUrl`, `qrCodeRef`, `notes`).
- `PUT /api/payment-references/{id}`: Update saved reference.
- `DELETE /api/payment-references/{id}`: Delete saved reference.

## 5. Data Export & Privacy
- `GET /api/export/transactions`: Streams RFC 4180 CSV export of all user transactions.
- `GET /api/privacy/export`: Complete machine-readable JSON data export.
- `POST /api/privacy/erase`: Permanent account and data erasure (`{ "confirm": "DELETE MY DATA" }`).

## 6. Health
- `GET /api/health`: Health status probe (`{ "status": "UP" }`).
