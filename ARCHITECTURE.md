# SpeDex — System Architecture Specification & Modular Ownership Boundaries

**Authoritative Architecture Source (`ARCHITECTURE.md`)**  
**Operating Version:** `2.05.02.0`  
**Governing Standard:** Universal Modular Architecture & Change-Isolation Governance System (`.agents/AGENTS.md`)  
**Repository Working Tree:** Verified Clean (`main`)  
**Automated Verification:** 150 / 150 Tests Passing (100% Pass Rate across 4 Test Suites)

---

## 1. System Overview & Architectural Topology

SpeDex (**Speed Index + Spending Index**) is an enterprise-grade financial intelligence utility, automated travel expense ledger, merchant quick-pay directory, and non-custodial smart wallet platform designed for young Indian transactors.

The system is engineered as a multi-tier, modular client-server architecture with strict domain boundaries, tenant isolation, and DPDP-ready privacy protections.

```text
┌─────────────────────────────────────────────────────────────────────────┐
│                        CLIENT APPLICATION LAYER                         │
│                                                                         │
│  ┌───────────────────────┐  ┌────────────────────┐  ┌────────────────┐  │
│  │     Web Dashboard     │  │     Mobile App     │  │ Native Android │  │
│  │ (React 19 / Vite 6)   │  │(Expo 53 / RN 0.79) │  │(Compose / Room)│  │
│  │   [dashboard_app/]    │  │     [mobile/]      │  │[mobile_native] │  │
│  └───────────┬───────────┘  └─────────┬──────────┘  └────────┬───────┘  │
└──────────────┼────────────────────────┼──────────────────────┼──────────┘
               │                        │                      │
               ▼                        ▼                      ▼
    HTTPS / REST API (Stateless Bearer JWT Authentication & Strict CORS)
               │
               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    BACKEND APPLICATION LAYER (Spring Boot 3)            │
│                                                                         │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │ Security Filter Chain: JwtAuthenticationFilter, BCrypt, CORS      │  │
│  └─────────────────────────────────┬─────────────────────────────────┘  │
│                                    │                                    │
│  ┌─────────────────────────────────▼─────────────────────────────────┐  │
│  │ REST Controllers: Auth, Dashboard, Trip, Payment, Privacy, Cap.   │  │
│  └─────────────────────────────────┬─────────────────────────────────┘  │
│                                    │                                    │
│  ┌─────────────────────────────────▼─────────────────────────────────┐  │
│  │ Service Layer: Business Rules, IDOR Validation & Capability Engine│  │
│  │ (UserService, TripService, UserCapabilityService, PrivacyService) │  │
│  └─────────────────────────────────┬─────────────────────────────────┘  │
│                                    │                                    │
│  ┌─────────────────────────────────▼─────────────────────────────────┐  │
│  │ Data Access Layer: Spring Data JPA Repositories                   │  │
│  └─────────────────────────────────┬─────────────────────────────────┘  │
└────────────────────────────────────┼────────────────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         DATA PERSISTENCE LAYER                          │
│                                                                         │
│  • Local Dev / Automated Tests: In-Memory H2 Database Engine            │
│  • Production Target: Managed PostgreSQL (Supabase / AWS RDS / Neon)    │
│  • Rolling 180-Day Retention Purge & Machine-Readable Data Portability  │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Core Architecture Principles (The 4 Pillars)

1. **Domain Ownership over File Type**: Code is organized and owned by domain rather than scattered across generic folders. Every page, feature, button, service, and data model has a clear, singular owner.
2. **Locality of Behavior**: Code that changes together lives together. UI, state, validation, types, and domain services remain close to their owning domain.
3. **Change Isolation & Bounded Radius**: Modifying a local feature (e.g. search filter or travel destination rate) must never destabilize or modify unrelated domains (e.g. authentication, payments, or privacy consent).
4. **Preservation Invariant ("Nothing Disappears")**: No existing feature or behavior may be deleted or degraded merely because code was moved, refactored, or modernized.

---

## 3. Major System Domains & Ownership Boundaries

SpeDex is partitioned into 6 distinct, loosely coupled domains:

```text
SPEDEX DOMAINS
├── DOMAIN 1: Authentication & User Accounts (Auth)
├── DOMAIN 2: Core Expense Ledger & Velocity Analytics (Ledger)
├── DOMAIN 3: Trips Ledger Subsystem & Multi-Currency Forex (Trips)
├── DOMAIN 4: Vendor Directory & UPI Payment Preparation (Payments)
├── DOMAIN 5: DPDP Act 2023 Privacy, Age Verification & Consent (Privacy)
└── DOMAIN 6: Mobile Sync Simulation & Companion Diagnostics (Simulator)
```

### Domain 1: Authentication & User Accounts
- **Responsibility**: User signup, age verification gating (<18 vs 18+), BCrypt password hashing, stateless HMAC-SHA256 JWT generation, profile management, and account status enforcement.
- **Backend Components**: `AuthController.java`, `UserService.java`, `UserRepository.java`, `User.java`, `JwtUtil.java`, `SecurityConfig.java`.
- **Frontend Components**: `LoginView.tsx`, `Sidebar.tsx` (user badge), `api.ts` (`login`, `signup`, `getMe`).
- **Mobile Components**: `AuthProvider.tsx`, `LoginScreen.tsx`.
- **Tests**: `UserServiceTest.java`, `JwtUtilTest.java`, `AuthProvider.test.tsx`.

### Domain 2: Core Expense Ledger & Velocity Analytics
- **Responsibility**: Micro-transaction recording, categorization, weekly burn rate computation, spending velocity gauges, cash vs digital tagging, and monthly budget limits.
- **Backend Components**: `DashboardController.java`, `TransactionService.java`, `TransactionRepository.java`, `BudgetRepository.java`, `Transaction.java`, `Budget.java`.
- **Frontend Components**: `OverviewView.tsx`, `AnalyticsView.tsx`, `TransactionModal.tsx`, `StatsCard.tsx`.
- **Mobile Components**: `HomeScreen.tsx`, `AnalyticsScreen.tsx`.
- **Tests**: `DashboardControllerTest.java`, `App.test.tsx`.

### Domain 3: Trips Ledger Subsystem & Multi-Currency Forex
- **Responsibility**: Travel budgeting, session lifecycle (Single Active Trip Invariant), foreign currency conversion with offline forex rate cache (USD, EUR, GBP, AED, SGD, THB, NPR, JPY), per-trip custom exchange rates, category percentage distribution, and secondary foreign spend analytics (`foreign_total_spend = totalSpend / exchangeRate`).
- **Backend Components**: `TripController.java`, `TripService.java`, `TripRepository.java`, `Trip.java`, `TripDto.java`, `TripDetailsDto.java`.
- **Frontend Components**: `TripsView.tsx`, `forex.ts`, `TripCard.tsx`.
- **Mobile Components**: `TripsScreen.tsx`, `TripsScreen.test.tsx`.
- **E2E Infrastructure**: `mock_backend.py`, `test-trips-e2e.py` (77 tests).
- **Tests**: `TripServiceTest.java` (12 tests).

### Domain 4: Vendor Directory & UPI Payment Preparation
- **Responsibility**: Local merchant directory, frequency tracking, non-custodial UPI intent generation (`upi://pay`), static and dynamic QR code generation, and server-side capability authorization gating.
- **Backend Components**: `PaymentController.java`, `UserCapabilityService.java`, `VendorRepository.java`, `Vendor.java`.
- **Frontend Components**: `PaymentsView.tsx`, `VendorsView.tsx`, `QrModal.tsx`.
- **Mobile Components**: `PaymentsScreen.tsx`, `PaymentConfirmScreen.tsx`.
- **Tests**: `PaymentControllerTest.java`, `UserCapabilityServiceTest.java`, `PaymentConfirmScreen.test.tsx`.

### Domain 5: DPDP Act 2023 Privacy, Age Verification & Consent
- **Responsibility**: DPDP technical compliance, minor protection (<18 Financial Learning Mode), verifiable parental consent lifecycle, purpose-separated consent toggles (Essential, Analytics, Marketing, Location, AI), immutable audit logging (`PrivacyAuditLog`), machine-readable JSON data export (`Download My SpeDex Data`), right to erasure (`DELETE MY DATA`), and grievance redressal ticketing with SLA monitoring.
- **Backend Components**: `PrivacyController.java`, `CapabilityController.java`, `PrivacyService.java`, `UserCapabilityService.java`, `ConsentRecordRepository.java`, `PrivacyAuditLogRepository.java`, `PrivacyGrievanceRepository.java`.
- **Frontend Components**: `PrivacyCenter.tsx`, `CookieConsentBanner.tsx`.
- **Mobile Components**: `PrivacyScreen.tsx`, `PrivacyScreen.test.tsx`.
- **Tests**: `PrivacyServiceTest.java` (13 tests), `PrivacyCenter.test.tsx` (2 tests).

### Domain 6: Mobile Sync Simulation & Companion Diagnostics
- **Responsibility**: Side-by-side live simulator embedded in web dashboard, QR synchronization diagnostics, and cross-client preview.
- **Frontend Components**: `MobileSyncSimulator.tsx`.

---

## 4. Architectural Ownership Map

| Layer | Domain 1 (Auth) | Domain 2 (Ledger) | Domain 3 (Trips) | Domain 4 (Payments) | Domain 5 (Privacy) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Page / View** | `LoginView` | `OverviewView`, `AnalyticsView` | `TripsView` | `PaymentsView`, `VendorsView` | `PrivacyCenter` (in Settings) |
| **Mobile Screen** | `LoginScreen` | `HomeScreen`, `AnalyticsScreen` | `TripsScreen` | `PaymentsScreen`, `PaymentConfirmScreen` | `PrivacyScreen` |
| **Key Features** | Age verification, JWT | Weekly burn, Velocity | Forex cache, Trip sessions | Payee quick-pay, UPI QR | Consent switches, Data export, Audit log |
| **Service Layer** | `UserService` | `TransactionService` | `TripService` | `UserCapabilityService` | `PrivacyService`, `UserCapabilityService` |
| **Controller** | `AuthController` | `DashboardController` | `TripController` | `PaymentController` | `PrivacyController`, `CapabilityController` |
| **Data Entity** | `User` | `Transaction`, `Budget` | `Trip` | `Vendor` | `ConsentRecord`, `PrivacyAuditLog`, `PrivacyGrievance` |
| **Shared Infra** | `JwtAuthenticationFilter`, `SecurityConfig` | Currency formatters, DB | Forex cache, DB | QR rendering, Capabilities | Audit engine, SLA monitoring |

---

## 5. Traceability Engine (Action $\rightarrow$ Data)

Every user interaction traces through a deterministic, unidirectional sequence:

```text
PAGE
 ↓
SECTION
 ↓
FEATURE
 ↓
COMPONENT
 ↓
ACTION
 ↓
FUNCTION
 ↓
SERVICE
 ↓
API / REST
 ↓
DATA REPOSITORY
```

### Traceability Trace Examples

#### Trace A: Starting a Cross-Border International Trip
```text
TripsView (Page)
 └── Start Trip Modal (Section)
      └── Destination Currency Selector (Feature)
           └── Currency Dropdown & Exchange Rate Input (Component)
                └── User clicks "Start Trip" (Action)
                     └── handleStartTrip() (Function)
                          └── api.startTrip(name, currency, rate) (Client Service)
                               └── POST /api/trips (REST API)
                                    └── TripService.startTrip() (Backend Service)
                                         ├── Enforces Single Active Trip Invariant (closes prior trips)
                                         └── TripRepository.save(trip) (Data Layer)
```

#### Trace B: Minor Account Live Payment Safeguard
```text
PaymentsView (Page)
 └── Vendor Card (Section)
      └── "Pay via UPI" Button (Component)
           └── User clicks "Pay" (Action)
                └── handlePreparePayment() (Function)
                     └── POST /api/payments/prepare (REST API)
                          └── PaymentController evaluates UserCapabilityService.canInitiatePayment()
                               ├── Gated: user.isMinor == true -> throws AccessDeniedException
                               ├── Logs PAYMENT_ACTION_BLOCKED in PrivacyAuditLog
                               └── Returns HTTP 403 Forbidden {"mode": "LEARNING_JOURNAL"}
```

#### Trace C: Right to Erasure / Anonymization
```text
Settings -> Privacy Center (Page)
 └── Danger Zone (Section)
      └── Account Erasure Modal (Feature)
           └── Confirmation Input "DELETE MY DATA" (Component)
                └── User submits confirmation (Action)
                     └── handleEraseAccount() (Function)
                          └── POST /api/privacy/erase (REST API)
                               └── PrivacyService.eraseUserData() (Backend Service)
                                    ├── Scrambles PII & credentials to "Erased User"
                                    ├── Sets isErased = true
                                    ├── Appends ACCOUNT_DELETION_REQUESTED to PrivacyAuditLog
                                    └── Flushes user session and returns HTTP 200
```

---

## 6. Change Radius Protocol & Change Manifest Model

Before executing any modification, calculate the **Change Radius**:

```text
Files classified as:
├── DIRECT:     Files directly implementing the requested change.
├── RELATED:    Files immediately supporting the change (types, tests).
├── DEPENDENT:  Files consuming the feature (views, callers).
├── SHARED:     Shared infrastructure (CORS, JWT filter, shared UI components).
└── UNRELATED:  All other modules (STRICTLY PROHIBITED FROM MODIFICATION).
```

### Change Manifest Standard
```text
CHANGE MANIFEST
Requested: <Exact description of requested change>
Change Level: MAJOR | SUB-VERSION | FUNCTIONAL | PATCH
Direct:      <Specific files implementing change>
Related:     <Types, tests, configs immediately required>
Dependent:   <Components consuming the modified module>
Shared:      <Shared components touched — caution & verification required>
Unrelated:   <Explicitly protected modules>
Rule:        Allowed modification radius is Direct + necessary Related.
```

---

## 7. Module Registry & Public API Contracts

| Module | Owning Domain | Public Contract Entry Point | External Consumers |
| :--- | :--- | :--- | :--- |
| **`auth`** | Authentication | `api.login()`, `api.signup()`, `SecurityConfig` | App shell, navigation guards |
| **`trips`** | Trips Ledger | `TripService`, `api.getTrips()`, `api.startTrip()`, `forex.ts` | `TripsView`, `TripsScreen`, E2E suite |
| **`payments`** | Payments | `PaymentController`, `UserCapabilityService`, `api.preparePayment()` | `PaymentsView`, `PaymentsScreen` |
| **`privacy`** | Privacy & DPDP | `PrivacyService`, `UserCapabilityService`, `PrivacyCenter` | Settings, Auth signup, Capability guards |
| **`ledger`** | Core Ledger | `TransactionService`, `DashboardController`, `api.getDashboard()` | `OverviewView`, `AnalyticsView`, `HomeScreen` |
| **`ui_shared`** | Shared Infra | `components/common/`, `Sidebar.tsx`, `forex.ts` | All views and screens |

---

## 8. Anti-Cascade & Preservation Invariants

1. **No Cascade Cleanup**: Never perform opportunistic "cleanups" or refactoring of unrelated files during a targeted fix.
2. **File Replacement Protection**: Never rewrite an entire file when a targeted, surgical change achieves the goal.
3. **Preservation Rule ("Nothing Disappears")**: Moving or updating code must preserve all existing edge cases, validations, error responses, and test assertions.
4. **Single Source of Truth**:
   - Application version: `dashboard_app/src/version.ts` synchronized with `pom.xml`, `package.json`, and `VERSION_CONTROLLER.md`.
   - Accounting ledger base currency: Indian Rupee (INR ₹).
   - Server capability enforcement: `UserCapabilityService.java`.

---

## 9. Verification & Testing Matrix

Every architectural transition or change must pass the 4-tier verification gate:

1. **Backend Service Layer**: `./mvnw.cmd test` — 57 / 57 tests passing.
2. **Dashboard Web Layer**: `npm test -- --run` & `npm run build` — 9 / 9 tests passing, 0 build errors.
3. **Mobile Layer**: `npm test` & `npx tsc --noEmit` — 7 / 7 tests passing, 0 type errors.
4. **Root E2E Integration**: `python test-trips-e2e.py` — 77 / 77 tests passing.
**Total System Coverage**: **150 / 150 automated tests (100% pass rate)**.
