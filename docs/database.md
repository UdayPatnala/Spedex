# SpeDex V1 Database Architecture & Schema

## 1. Persistence Engine
- **Engine**: Managed PostgreSQL (Production) / In-Memory H2 (Local Development & Unit Tests).
- **ORM**: Spring Data JPA / Hibernate 6.
- **Dialect**: `org.hibernate.dialect.PostgreSQLDialect` in production (`application-prod.properties`).

## 2. Core Entities & Relationships

### `users`
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `name` (VARCHAR)
- `email` (VARCHAR, UNIQUE, NOT NULL)
- `password` (VARCHAR, NOT NULL) - BCrypt hash
- `age` (INTEGER)
- `is_minor` (BOOLEAN)
- `created_at` (TIMESTAMP)

### `transactions`
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `user_id` (BIGINT, FOREIGN KEY -> users.id, NOT NULL)
- `type` (VARCHAR: `EXPENSE`, `INCOME`)
- `amount` (DOUBLE PRECISION, NOT NULL)
- `category` (VARCHAR, NOT NULL)
- `merchant` (VARCHAR)
- `description` (VARCHAR)
- `payment_method` (VARCHAR: `UPI`, `CASH`, `CARD`, `NET_BANKING`)
- `payment_reference` (VARCHAR)
- `occurred_at` (TIMESTAMP, NOT NULL)
- `created_at` (TIMESTAMP)

### `vendors` (Saved Payment References)
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `user_id` (BIGINT, FOREIGN KEY -> users.id, NOT NULL)
- `name` (VARCHAR, NOT NULL)
- `vpa` (VARCHAR)
- `payment_url` (VARCHAR)
- `qr_code_ref` (VARCHAR)
- `notes` (VARCHAR)
- `category` (VARCHAR)

### `trips` & `trip_expenses`
- Travel expedition journals and segregated group trip expense line items.

## 3. Forward Compatibility & Migration Rule
- Future V2 additions (e.g. multi-currency rates, recurring rules) must be added via additive migrations.
- Existing tables and records must never be dropped during normal upgrades.
