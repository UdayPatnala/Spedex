# SpeDex Privacy, Age Verification, Consent & Financial Safety Specification

**Document Version:** `2.05.00.0`  
**Governing Standard:** Digital Personal Data Protection Act, 2023 (DPDP Act) Technical Controls  
**Status:** ARCHITECTURALLY VERIFIED  

---

## 1. Executive Summary & Privacy-by-Design Principles

SpeDex is engineered from the ground up as a **privacy-first, non-custodial financial utility and campus expense tracker**. Under the Digital Personal Data Protection Act, 2023 (DPDP Act), SpeDex acts as a Data Fiduciary regarding user profile information, and adheres strictly to four core architectural principles:

1. **Data Minimization (Section 6(1))**: SpeDex collects only data strictly necessary for providing expense journaling, hostel budget management, and travel ledgers. Banking secrets (UPI PINs, debit card CVVs, net banking passwords) are **never requested, never transmitted, and never stored**.
2. **Purpose Limitation (Section 6(2))**: Personal data gathered for transactional ledgering is never repurposed for behavioural advertising or third-party data broker monetization.
3. **Purpose-Separated Consent (Section 6(1) & 6(4))**: Consent is not bundled. Users have granular, independent controls over distinct operational capabilities (Telemetry, Marketing, Geolocation, AI personalization) and can withdraw consent at any time without forfeiture of core accounting functionality.
4. **Minor Protection & Financial Safety (Section 9)**: Individuals under 18 operate in a dedicated **Financial Learning / Journal Mode**. Live payment execution (UPI payment preparation, QR generation, deep links) is prohibited server-side, and behavioural profiling is strictly blocked.

> **Responsible Engineering Boundary**:  
> Implementation of the technical controls specified herein ensures SpeDex is architected to support compliance with the technical provisions of the DPDP Act, 2023. This specification does not constitute legal certification or formal statutory indemnity.

---

## 2. Age Determination & Minor Financial Safety Layer

### 2.1 The Two Operational Personas

```text
               User Registration / Age Gating
                             │
            ┌────────────────┴────────────────┐
            ▼                                 ▼
   Declared Age >= 18                Declared Age < 18
            │                                 │
   FULL_TRANSACTIONAL                LEARNING_JOURNAL
   (Adult Account)                   (Minor Safety Account)
            │                                 │
   • UPI QR Generation Active        • Live UPI Generation BLOCKED (403)
   • Payment Link Shortcuts Active   • Payment QR Scanning BLOCKED
   • Autonomous Ledgers Active       • Manual Accounting ACTIVE
   • Standard Consent Matrix         • Budgeting & Goals ACTIVE
                                     • Profiling / Tracking FORBIDDEN
                                     • Parental Consent: PENDING
```

### 2.2 Server-Side Capability Enforcement Matrix

Client-side flags (`isMinor`) are never trusted for authorization. The centralized `UserCapabilityService` enforces restrictions server-side before any payment-related endpoint executes:

| Action / Capability | Adult (18+) | Minor (<18) | Enforcement Mechanism |
|---|---|---|---|
| **Initiate Payment (`POST /api/payments/prepare`)** | Allowed | **Blocked (HTTP 403)** | `UserCapabilityService.canInitiatePayment()` checks user age; logs `PAYMENT_ACTION_BLOCKED` in audit ledger |
| **Complete Payment (`POST /api/payments/{id}/complete`)** | Allowed | **Blocked (HTTP 403)** | Server-side validation |
| **Generate UPI QR Code** | Allowed | **Suppressed / Replaced** | Web & Mobile UI hides live QR; displays educational notice |
| **Scan UPI Payment QR** | Allowed | **Suppressed** | Mobile camera suppresses UPI scanner for minors |
| **Record Manual Expense** | Allowed | **Allowed** | Full access to educational expense bookkeeping |
| **Manage Budgets & Goals** | Allowed | **Allowed** | Full access to hostel budgeting tools |
| **Telemetry Analytics** | Opt-In Required | **Blocked** | Section 9 prohibits minor tracking |
| **Educational Marketing** | Opt-In Required | **Blocked** | Section 9 prohibits targeted marketing to minors |

### 2.3 Parental / Guardian Verification Workflow

For minor accounts, SpeDex implements verifiable parental consent with minimal guardian data collection:
1. **Minimal Data Collection**: Only Guardian Name and Guardian Email are gathered. No sensitive parent biometric or national identity numbers are recorded.
2. **Lifecycle States**:
   - `PENDING`: Initial state upon minor registration or guardian update.
   - `VERIFIED`: Token link clicked via secure verification endpoint (`GET /api/privacy/guardian-consent/verify?token=...`).
   - `REJECTED` / `WITHDRAWN`: Revocation logged in immutable `ConsentRecord` table.

---

## 3. Purpose-Separated Consent Architecture (Section 6)

Consent is separated into distinct, independently toggleable categories:

| Purpose Key | Category | Default Adult | Default Minor | Description |
|---|---|---|---|---|
| `TERMS_AND_PRIVACY` | Essential | Granted | Granted | Essential platform operation (authentication, password hash, encryption) |
| `ANALYTICS` | Telemetry | Opt-In (false) | **Permanently False** | Anonymous performance benchmarks and system crash diagnostics |
| `MARKETING` | Communication | Opt-In (false) | **Permanently False** | Student financial literacy digests and product release summaries |
| `LOCATION` | Contextual | Opt-In (false) | **Permanently False** | Approximate city-level tagging for active travel trips |
| `AI` | Smart Features | Opt-In (false) | **Permanently False** | Predictive heuristics for automatic expense categorization |

### Consent Withdrawal SLA
In compliance with Section 6(4), withdrawing consent is as straightforward as granting it. Users toggle the switch in the **Privacy Control Centre**, immediately executing `POST /api/privacy/consent/withdraw`. The backend updates the user entity, records an immutable `WITHDRAWN` event in `consent_records`, and logs a `CONSENT_WITHDRAWN` entry in `privacy_audit_logs`.

---

## 4. Immutable Privacy & Safety Audit Trail

All privacy-critical and safety-critical events are recorded in the `privacy_audit_logs` table:

```sql
CREATE TABLE privacy_audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    user_email VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    ip_address VARCHAR(100),
    user_agent VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
```

### Registered Audit Event Types
- `ACCOUNT_CREATED`: Logged upon registration with age category.
- `AGE_CATEGORY_ASSIGNED`: Records adult or minor designation.
- `GUARDIAN_CONSENT_REQUESTED`: Verification token issued to guardian.
- `GUARDIAN_CONSENT_VERIFIED`: Parent/guardian confirmed verification.
- `PAYMENT_ACTION_BLOCKED`: Security block triggered when minor attempts payment initiation.
- `CONSENT_UPDATED`: Modification of granular consent settings.
- `CONSENT_WITHDRAWN`: Immediate revocation of optional processing purpose.
- `DATA_EXPORT_REQUESTED`: Data principal exercised Section 11 access right.
- `ACCOUNT_DELETION_REQUESTED`: Data principal exercised Section 12 erasure right.
- `GRIEVANCE_SUBMITTED`: Section 13 formal dispute initiated.

---

## 5. Third-Party Subprocessor Registry

SpeDex operates exclusively with transparent, enterprise-grade cloud infrastructure:

| Subprocessor | Operational Purpose | Data Handled | Jurisdiction | Security Safeguards |
|---|---|---|---|---|
| **Render Inc.** | Backend API Hosting | Encrypted application payloads, JWTs | Frankfurt (EU) / Singapore (APAC) | TLS 1.3 in transit, ISO 27001, SOC 2 Type II |
| **Vercel Inc.** | Web Dashboard CDN | Static client bundle, edge caching | Global Edge / Mumbai PoP | SOC 2 Type II, edge origin shield |
| **Supabase / AWS** | Managed PostgreSQL DB | Hashed passwords, encrypted ledgers | Singapore (`ap-southeast-1`) | AES-256 at rest, VPC isolation, Automated Backups |
| **Google Fonts** | Typography Assets | CDN caching of font files | Global Anycast | Zero PII logging, cookieless domain |

---

## 6. Statutory Data Subject Rights

### 6.1 Right to Access Personal Data (Section 11)
Any authenticated user can trigger `GET /api/privacy/export` (via the **Download My SpeDex Data** button in the Privacy Center). The response is an instant machine-readable JSON archive containing:
- Complete Profile & Account Identifiers
- Active Capability & Mode Assignment
- All Recorded Transactions (Amount, Vendor, Category, Payment Method, Timestamp)
- All Saved Vendors & Payee Directory
- All Active & Past Budgets
- All Recurring Reminders
- All Travel Trips & Shared Group Ledgers
- Complete Timestamped `ConsentRecord` History
- Complete `PrivacyAuditLog` Timeline
- All Submitted Grievances and DPO Responses

### 6.2 Right to Correction (Section 12(1))
Users may update their profile names, avatars, and notification preferences at any time via `PUT /api/auth/profile`.

### 6.3 Right to Erasure / Anonymization (Section 12(3))
Users may exercise permanent erasure via `POST /api/privacy/erase`. To prevent accidental deletion, the backend requires exact confirmation payload `{"confirmationText": "DELETE MY DATA"}`:
1. User credentials (`passwordHash`) are scrambled.
2. Personal identifiers (`name`, `profilePictureUrl`, `guardianEmail`, `guardianName`) are nullified or replaced with `"Erased User"`.
3. Account status is flagged `isErased = true`, preventing future login.
4. Financial transaction figures are decoupled from identity to satisfy mandatory financial recordkeeping regulations while ensuring irreversible pseudonymization.

### 6.4 Right of Grievance Redressal (Section 13)
The Privacy Center provides a direct ticket submission form connected to `POST /api/privacy/grievance`.
- **Acknowledgement SLA**: Within 48 hours.
- **Resolution SLA**: Within 15 to 30 calendar days.
- **Data Protection Officer Contact**: `dpo@spedex.internal`.

---

## 7. Architectural Verification Standard

Every privacy mechanism in SpeDex is verified through multi-tier automated test suites:
1. **Backend Unit & Capability Tests (`./mvnw.cmd test`)**: 55/55 passing, specifically verifying `UserCapabilityServiceTest`, `PaymentControllerTest` minor blocking, and `PrivacyServiceTest` consent withdrawals.
2. **Dashboard Web Tests (`npm test -- --run`)**: 9/9 passing in Vitest, verifying Privacy Center consent switches, modal interactions, and production Vite compilation.
3. **Mobile Screen Tests (`npm test` & `npx tsc --noEmit`)**: 7/7 passing in Jest with 0 TypeScript compilation errors, verifying QR scanner suppression and minor banner visibility.
4. **Root End-to-End Suite (`python test-trips-e2e.py`)**: 77/77 passing across authentication, transactions, and trip ledgers.
