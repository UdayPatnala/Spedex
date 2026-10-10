# SpeDex — Authoritative Product Master (SPEDEX.md / PRODUCT_MASTER.md)

**Authoritative Product Master File (`SPEDEX.md`)**  
**Current System Version:** `2.07.01.1` (Security & Git Push Secret Hygiene Patch)  
**Governing Architecture:** Universal Reverse-Engineering, Universal Version Control & Universal Modular Architecture (`.agents/AGENTS.md`)  
**Repository Working Tree:** Verified Clean (`main`)  
**Automated Test Status:** 162 / 162 Tests Passing (100% Pass Rate across 4 Test Suites)  
**Authoritative Version Controller:** [`VERSION_CONTROLLER.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/VERSION_CONTROLLER.md)  
**Governance Invariant:** Exactly two authoritative Markdown documents are maintained at repository root (`SPEDEX.md` as Product Master and `VERSION_CONTROLLER.md` as Version Control Master).

---

## 1. Project Identity

- **Project Name:** SpeDex
- **Etymology / Blend:** **Speed Index** (high-frequency, frictionless expense logging) + **Spending Index** (real-time velocity analytics and financial awareness).
- **Project Type:** Personal financial ledger, travel expense journal, vendor quick-pay directory, and campus budgeting utility.
- **Product Classification:** Non-custodial financial utility and organizational layer. SpeDex is **not** a bank, **not** a payment processor, and **not** a custodial wallet.
- **Operating Version:** `2.07.01.1` (Security Patch: Secret Hygiene & Git Push Protection Hardening).
- **Status:** **OPERATIONAL & 100% VERIFIED**.
- **Repository Location:** Local workspace `d:\PROJECT\AROH Open Source\Products\Spedex`.
- **Target Deployment Environments:**
  - Backend API: Containerized Java 17 / Spring Boot service deployed via multi-stage Docker build to Linux/Cloud environments (e.g., Render).
  - Dashboard Web App: React 19 + Vite 6 Single Page Application deployed to global CDN edge via Vercel.
  - Mobile Application: Cross-platform React Native / Expo SDK 53 application with native Android Jetpack Compose bridge.
- **Core Purpose:** Provide students, hostelites, and young adults with a lightweight, high-velocity spending ledger and merchant directory that eliminates the friction of everyday transaction tracking while maintaining strict DPDP-ready privacy and minor safety guardrails.

---

## 2. Motive

### 2.1 Original Motivation
Traditional personal finance and accounting software is designed for enterprise bookkeeping, tax reconciliation, or complex multi-asset investment tracking. These tools require extensive category configuration, manual double-entry bookkeeping, or deep banking account synchronization.

Younger demographics—particularly university students, hostel dwellers, and early-career professionals in India—interact with personal finance very differently:
- They execute multiple low-value micro-transactions every day (canteen snacks, chai, photocopies, rickshaws, shared meals).
- These micro-expenses are individually negligible (₹10 to ₹200), but collectively account for 60% to 80% of their discretionary monthly budget.
- The friction of opening a complex budgeting app to record a ₹20 chai leads to immediate user abandonment.
- Consequently, users experience "invisible financial leakage" where money disappears without a clear audit trail.

### 2.2 Core Problem Breakdown
1. **Problem A — Small Spending Disappears from Awareness:** High-frequency, low-value cash and UPI purchases slip through memory, making monthly budget reconciliation impossible.
2. **Problem B — Repeated Merchant Spending is Unindexed:** Users repeatedly visit the same 5 to 10 local vendors (mess, laundry, grocery, stationery) without visibility into aggregate vendor spending.
3. **Problem C — Scattered Payment References:** Merchant UPI IDs (VPAs), static QR codes, and phone numbers are scattered across phone photo galleries, chat apps, and SMS histories.
4. **Problem D — High Recording Friction:** If logging an expense takes longer than the payment itself, the user stops tracking.

### 2.3 Operating Philosophy
> **"Make spending frictionless; tracking becomes a useful byproduct."**

SpeDex bridges the gap between payment convenience and financial discipline by indexing vendors, providing quick payment references, and logging expenses in a unified, aesthetically refined ledger.

---

## 3. Vision

### 3.1 Long-Term Product Vision
SpeDex aims to become the default personal financial utility for Indian youth by providing a seamless, non-custodial intelligence layer above existing payment rails.

```text
┌──────────────────────────────────────────────────────────────┐
│                         USER ACTION                          │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│                    SPEDEX UTILITY LAYER                      │
│   • Merchant Directory & Saved UPI References                │
│   • Instant Expense Logging & Category Tagging               │
│   • Trips Ledger & Group Expense Distribution                │
│   • Real-Time Velocity Analytics & Hostel Budgeting          │
│   • DPDP Privacy Controls & Minor Financial Learning Mode    │
└──────────────┬───────────────────────────────┬───────────────┘
               │                               │
               ▼                               ▼
┌──────────────────────────────┐ ┌─────────────────────────────┐
│    EXISTING PAYMENT RAILS    │ │    PERSONAL FINANCIAL       │
│    (UPI Apps, Net Banking,   │ │    AWARENESS & LEDGER       │
│     Cash, Cards)             │ │    (Non-Custodial Insights) │
└──────────────────────────────┘ └─────────────────────────────┘
```

### 3.2 Key Differentiators
1. **Merchant-Centric Architecture:** Vendors are first-class entities with dedicated spending metrics, stored payment references, and quick-pay triggers.
2. **Automated Trips Ledger:** Dedicated travel sessions that isolate vacation or field-trip expenses from routine living expenses, preventing distorted monthly analytics.
3. **Indian Rupee Currency Note Aesthetics:** A visual identity built on the colors of contemporary Indian Rupee banknotes paired with classic `Cormorant Garamond` serif headings and clean `Sora` sans body typography.
4. **Age-Aware Financial Safety:** Built-in age gating that provides minors (<18) with a dedicated **Financial Learning / Journal Mode** where payment actions are strictly blocked server-side, but budgeting, manual bookkeeping, and financial literacy features remain accessible.

### 3.3 Strict Product Boundaries (What SpeDex is NOT)
- **NOT a Bank:** SpeDex does not hold deposits, issue credit, or maintain custodial accounts.
- **NOT a Payment Processor:** SpeDex does not settle transactions, hold merchant escrows, or process card charges directly. Payments are initiated via external UPI intent protocols or recorded manually.
- **NOT an Unrestricted Financial Service for Minors:** In compliance with statutory child protection principles, users under 18 cannot initiate payments, generate live payment QRs, or be subject to behavioral tracking.
- **ZERO Credential Storage:** SpeDex **never** asks for, transmits, or stores UPI MPINs, ATM PINs, debit/credit card numbers, CVVs, or net banking passwords.

---

## 4. Goals

### 4.1 Current Achieved Goals
- [x] Full-stack modular architecture across Spring Boot 3 backend, React 19 dashboard, and React Native mobile client.
- [x] End-to-end Trips Ledger subsystem with active session auto-closure and cash/card split calculations.
- [x] Strict tenant isolation and IDOR protection returning `403 Forbidden` across all user resources.
- [x] Centralized server-side capability engine (`UserCapabilityService`) governing adult vs. minor capabilities.
- [x] Minor protection framework (<18) with Financial Learning Mode, server-side payment blocking, and verifiable parental consent link lifecycle.
- [x] Comprehensive DPDP Act 2023 technical controls: purpose-separated consent, immutable audit logging, machine-readable JSON export, hard-gated erasure, and grievance redressal ticketing.
- [x] Complete SpeDex V1 18-step user loop (Register, Login, Profile, Add/Edit/Delete Transactions, History, Search/Filters, Categories, Spending Summaries, Analytics, Payment References CRUD, Personal Dashboard, RFC 4180 CSV Export, Account Deletion, Settings, Sign Out).
- [x] Production deployment configuration for Render (PostgreSQL profile `application-prod.properties`) and Vercel (SPA fallback rewrite `vercel.json`).
- [x] 100% test coverage across 4 automated verification suites (159/159 passing tests).
- [x] Authoritative version control and change governance under `A.BC.DE.F` formatting.

### 4.2 Medium & Long-Term Goals
- [ ] Cross-border multi-currency support with offline foreign exchange rate caching for international student travel.
- [ ] Privacy-preserving on-device SMS transaction parsing for automated expense reconciliation.
- [ ] Shared hostel room / multi-tenant family budgeting with granular sub-account permissions.
- [ ] Full feature parity in `mobile_native_android` (Jetpack Compose) matching the React Native / Web Dashboard implementations.

---

## 5. Scope

### 5.1 In Scope
- **Personal Expense Ledger:** High-frequency transaction entry, cash vs. digital tagging, category assignments, and timestamped ledgering.
- **Merchant Management:** Vendor directory, UPI ID (VPA) storage, category associations, and spending aggregation per vendor.
- **Trip Expense Ledgers:** Live travel sessions, automatic completion of overlapping trips, cash/online splits, and category distribution percentages.
- **Financial Analytics:** Real-time spending velocity gauges, day-of-week distribution histograms, weekly spending totals, and monthly budget progress.
- **Budgeting & Reminders:** Category monthly spending limits, alert threshold indicators, and bill reminder tracking.
- **Privacy & Data Governance:** Purpose-separated consent toggles (Essential, Telemetry, Marketing, Geolocation, AI), immutable privacy audit trail, parental consent token verification, machine-readable data export, irreversible account erasure, and grievance redressal.
- **Cross-Platform Delivery:** Responsive web dashboard, Expo React Native mobile app, and native Android foundation.

### 5.2 Out of Scope
- Direct custodial banking or wallet deposits.
- Clearing, switching, or direct settlement of UPI/NEFT/IMPS payments.
- Credit scoring, loan disbursement, or debt collection.
- Algorithmic securities trading, cryptocurrency speculation, or investment advisory.
- Arbitrary third-party ad tracking, behavioral profiling, or selling data to data brokers.

---

## 6. Target Users & Personas

### 6.1 Persona 1: Adult Transactor (18+ University Student / Young Professional)
- **Profile:** College student or entry-level engineer managing a monthly stipend or allowance.
- **Behavior:** Makes 5–15 small UPI and cash transactions daily at campus cafeterias, tea stalls, bookshops, and transport services.
- **Capabilities:** Full transactional autonomy. Can create vendors, generate live UPI QR codes, launch UPI intent links, manage travel budgets, and customize privacy preferences.
- **Core Value:** Instant visibility into daily spending velocity without tedious manual balance entry.

### 6.2 Persona 2: Minor Learner (<18 High School / Hostel Student)
- **Profile:** Boarding school or junior college student receiving parental pocket money.
- **Behavior:** Needs to track hostel expenses, stationery, and mess food while learning personal budgeting.
- **Capabilities:** Operating in **Financial Learning / Journal Mode**:
  - Full access to manual transaction logging, category tracking, budget progress, and travel ledgers.
  - Payment preparation (`POST /api/payments/prepare`), payment links, and live UPI QR generation are **strictly blocked server-side (HTTP 403 Forbidden)**.
  - Behavioral tracking, telemetry, and promotional marketing are **permanently disabled**.
  - Parental verification link workflow (`PENDING` $\rightarrow$ `VERIFIED`) initiated with minimal guardian contact data.
- **Core Value:** Safe financial education and bookkeeping without exposure to unauthorized digital payment risks.

### 6.3 Persona 3: Group Traveler / Event Organizer
- **Profile:** Student organizing a weekend batch trip or festival excursion.
- **Behavior:** Manages shared travel funds, pays group vendors, and needs to know exact cash vs. online expenditure splits.
- **Capabilities:** Creates bounded trip sessions, logs trip expenses in real time, and views consolidated settlement metrics.

---

## 7. Feature Inventory & Implementation Status

| Feature / Subsystem | Status | Description | Primary Code References |
|---|---|---|---|
| **Transaction Ledger** | `IMPLEMENTED` | Structured personal expense records with amount, category, payment mode (Cash/Digital), notes, and timestamps. | [`Transaction.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/Transaction.java), [`DashboardController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/DashboardController.java), [`PaymentsView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/PaymentsView.tsx) |
| **Manual Transaction Entry** | `IMPLEMENTED` | Low-friction modal entry available to all users (including minors in Financial Learning Mode). | [`TripController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/TripController.java), [`TripsView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/TripsView.tsx), [`HomeScreen.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/mobile/src/screens/HomeScreen.tsx) |
| **Merchant / Vendor Directory** | `IMPLEMENTED` | Saved payees with UPI VPAs, category associations, and aggregate historical expenditure. | [`Vendor.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/Vendor.java), [`AddVendorModal.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/vendors/AddVendorModal.tsx) |
| **QR & Payment Reference Storage** | `IMPLEMENTED` | Dynamic UPI URI generation (`upi://pay?pa=...`) for adult users; suppressed for minors. | [`PaymentController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/PaymentController.java), [`PaymentsView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/PaymentsView.tsx) |
| **Trips Ledger Subsystem** | `IMPLEMENTED` | Session-based travel budgeting, automatic closure of overlapping active trips, and cash/online split aggregation. | [`TripService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/TripService.java), [`TripController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/TripController.java), [`TripsView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/TripsView.tsx) |
| **Spending Velocity & Analytics** | `IMPLEMENTED` | Real-time calculation of daily burn velocity, weekly spending totals, and day-of-week distribution histograms. | [`DashboardService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/DashboardService.java), [`AnalyticsView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/AnalyticsView.tsx) |
| **Budget Management** | `IMPLEMENTED` | Category monthly allocation limits with dynamic visual progress gauges and alert thresholds. | [`Budget.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/Budget.java), [`BudgetView.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/views/BudgetView.tsx) |
| **Bill & Payment Reminders** | `IMPLEMENTED` | Due date tracking for recurring hostel rent, mess fees, and recharge reminders. | [`Reminder.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/Reminder.java), [`RemindersScreen.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/mobile/src/screens/RemindersScreen.tsx) |
| **Server Capability Engine** | `IMPLEMENTED` | Centralized capability check service governing payment permissions by age and status. | [`UserCapabilityService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/UserCapabilityService.java), [`CapabilityController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/CapabilityController.java) |
| **Age Gating & Minor Protection** | `IMPLEMENTED` | Age declaration on signup, minor designation (<18), and HTTP 403 Forbidden enforcement on payment endpoints. | [`UserService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/UserService.java), [`PaymentController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/PaymentController.java) |
| **Purpose-Separated Consent** | `IMPLEMENTED` | Independent opt-in toggles for Essential, Analytics, Marketing, Location, and AI processing with 1-click revocation. | [`PrivacyService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/PrivacyService.java), [`PrivacyCenter.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/PrivacyCenter.tsx) |
| **Verifiable Guardian Consent** | `IMPLEMENTED` | Minimal guardian data collection (name & email) with secure tokenized link verification workflow. | [`PrivacyService.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/service/PrivacyService.java), [`ConsentOnboardingModal.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/ConsentOnboardingModal.tsx) |
| **Privacy Audit Trail** | `IMPLEMENTED` | Immutable ledger recording lifecycle, consent, and safety-block events. | [`PrivacyAuditLog.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/PrivacyAuditLog.java), [`PrivacyCenter.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/PrivacyCenter.tsx) |
| **Data Subject Rights (Export/Erase)**| `IMPLEMENTED` | 1-click structured machine-readable JSON archive export and exact-phrase (`DELETE MY DATA`) account erasure. | [`PrivacyController.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/controller/PrivacyController.java), [`PrivacyCenter.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/PrivacyCenter.tsx) |
| **Grievance Redressal System** | `IMPLEMENTED` | In-app dispute ticketing with unique ticket IDs and automated DPO response tracking. | [`PrivacyGrievance.java`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/backend/src/main/java/com/spedex/model/PrivacyGrievance.java), [`PrivacyCenter.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/PrivacyCenter.tsx) |
| **Subprocessor Register & Cookies** | `IMPLEMENTED` | Complete transparency disclosure of cloud infrastructure (Render, Vercel, Supabase) and cookie preference modal. | [`CookieConsentBanner.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/CookieConsentBanner.tsx), [`PrivacyCenter.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/privacy/PrivacyCenter.tsx) |
| **Mobile Sync Simulator** | `IMPLEMENTED` | Embedded side-by-side companion simulator in web dashboard for real-time mobile sync and QR testing. | [`MobileSyncSimulator.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/components/mobile/MobileSyncSimulator.tsx) |
| **Cross-Platform Mobile App** | `IMPLEMENTED` | Touch-optimized React Native / Expo application with full feature parity. | [`mobile/src/AppNavigator.tsx`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/mobile/src/navigation/AppNavigator.tsx) |
| **Native Android Foundation** | `IMPLEMENTED` | Kotlin Jetpack Compose app with Android Room SQLite persistence and Material 3 design tokens. | [`mobile_native_android/`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/mobile_native_android) |
| **Offline Multi-Currency Forex** | `IMPLEMENTED` | Dynamic cross-border conversion with offline forex cache and custom exchange rates for international travel trips. | [`forex.ts`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/dashboard_app/src/utils/forex.ts) |
| **On-Device SMS Parser** | `PROPOSED` | Privacy-safe local receipt parsing from transactional bank SMS messages. | Conceptual product direction. |
| **AI-Assisted Spending Insights** | `PROPOSED` | Natural language spending summaries and anomaly detection (backend consent toggle in place). | Planned future enhancement. |

---

## 8. Functional Workflows

### 8.1 Workflow 1: User Registration & Age Gating
```text
User Submits SignUpRequestDto (name, email, password, age, consents)
                     │
                     ▼
             Age Evaluation (UserService)
                     │
         ┌───────────┴───────────┐
         ▼                       ▼
     Age >= 18               Age < 18
         │                       │
 • isMinor = false       • isMinor = true
 • adult capabilities    • Financial Learning Mode active
 • optional consents     • guardianConsentStatus = "PENDING"
   honored               • analytics & marketing forced to false
         │                       │
         └───────────┬───────────┘
                     │
                     ▼
 Create User Entity & Hash Password (BCrypt)
 Record Initial TERMS_AND_PRIVACY ConsentRecord
 Append ACCOUNT_CREATED & AGE_CATEGORY_ASSIGNED to PrivacyAuditLog
 Generate Bearer JWT Token
 Return AuthResponseDto to Client
```

### 8.2 Workflow 2: Transaction Logging
1. User clicks **"Log Expense"** in dashboard or mobile app.
2. User provides amount (₹), selects category (Food, Travel, Hostel, Shopping, Bills, Other), chooses payment method (`CASH` or `DIGITAL`), and optionally links a vendor or active trip.
3. Client dispatches `POST /api/trips/{id}/transactions` or updates the personal ledger.
4. Backend verifies ownership (`user.id == transaction.user.id`). If another user's resource is referenced, an `AccessDeniedException` (HTTP 403) is thrown.
5. Transaction is persisted in JPA repository.
6. Dashboard recalculates weekly burn, category distributions, and spending velocity.

### 8.3 Workflow 3: Trips Ledger Lifecycle & Multi-Currency Cross-Border Travel
1. User starts a new trip with destination, optional destination currency (`USD`, `EUR`, `GBP`, `AED`, `SGD`, `THB`, `NPR`, `JPY`), custom exchange rate, and budget via `POST /api/trips`.
2. Backend invokes `TripService.startTrip()`, which checks for existing active trips and **automatically sets their status to `COMPLETED`** (enforcing the Single Active Trip Invariant).
3. Any expense logged during an active trip session is linked directly to the trip. For international trips, user can toggle foreign currency entry mode; the client computes the INR equivalent using the trip exchange rate while recording the original foreign amount in the transaction notes.
4. The system continuously computes cash expenditure, online expenditure, category percentage distributions, and secondary foreign currency totals (`foreign_total_spend = totalSpend / exchangeRate`).
5. User marks trip completed via `POST /api/trips/{id}/complete`, generating an immutable travel expense summary.

### 8.4 Workflow 4: Minor Account Financial Learning Mode
1. Authenticated minor attempts to initiate a payment via `POST /api/payments/prepare`.
2. Controller passes user to `UserCapabilityService.canInitiatePayment(user)`.
3. Capability engine detects `user.isMinor == true` and returns `false`.
4. Controller immediately throws `AccessDeniedException("PAYMENT_ACTION_BLOCKED_FOR_MINOR")`.
5. GlobalExceptionHandler maps this to `HTTP 403 Forbidden`.
6. Backend logs `PAYMENT_ACTION_BLOCKED` in `privacy_audit_logs`.
7. Web and mobile clients display the **"Financial Learning / Journal Mode Active"** educational shield banner.

### 8.5 Workflow 5: Verifiable Guardian Consent Lifecycle
1. Minor or parent enters Guardian Name and Guardian Email in the Privacy Center.
2. Client sends `POST /api/privacy/guardian-consent/request`.
3. Backend generates a cryptographically random verification token, sets `guardianConsentStatus = "PENDING"`, and logs `GUARDIAN_CONSENT_REQUESTED`.
4. Parent clicks verification link containing the token (`GET /api/privacy/guardian-consent/verify?token=...`).
5. Backend validates the token, updates status to `VERIFIED`, logs `GUARDIAN_CONSENT_VERIFIED`, and records a new granted `ConsentRecord`.

### 8.6 Workflow 6: Data Subject Access Request (Section 11)
1. Authenticated user clicks **"Download My SpeDex Data"** in `Settings -> Privacy & Data`.
2. Client sends `GET /api/privacy/export`.
3. Backend constructs a structured `UserDataExportDto` bundling:
   - Full profile metadata and active capability flags.
   - All personal ledger transactions.
   - All saved vendors and payees.
   - All budgets and recurring reminders.
   - All active and historical trip ledgers.
   - Complete historical `ConsentRecord` ledger.
   - Complete `PrivacyAuditLog` timeline.
   - All filed grievances and DPO resolutions.
4. Client receives machine-readable JSON archive for portability.

### 8.7 Workflow 7: Account Erasure & Anonymization (Section 12)
1. Authenticated user navigates to the danger zone in the Privacy Center.
2. User must type the exact confirmation phrase: `DELETE MY DATA`.
3. Client dispatches `POST /api/privacy/erase` with the payload.
4. Backend verifies the exact phrase match.
5. User credentials are deleted; personal identifiers (`name`, `profilePictureUrl`, `guardianEmail`, `guardianName`) are nullified or scrambled to `"Erased User"`.
6. Account is flagged `isErased = true`, permanently invalidating future login attempts.
7. An audit record `ACCOUNT_DELETION_REQUESTED` is recorded.

---

## 9. Technology Stack

Every technology in the SpeDex ecosystem is selected for explicit architectural reasons:

### 9.1 Backend Technology Stack
- **Java 17 (Eclipse Temurin LTS):**
  - *Usage:* Primary runtime for the server application.
  - *Reason:* Long-term support stability, modern language features (records, pattern matching), and strong enterprise performance.
  - *Relevant Modules:* `backend/src/main/java/`
- **Spring Boot 3.4.1:**
  - *Usage:* Master application framework providing REST controllers, dependency injection, and transaction lifecycle management.
  - *Reason:* Standardized production ecosystem, embedded Tomcat server, and rapid testing utilities.
  - *Relevant Modules:* Entire `backend/`
- **Spring Security 6:**
  - *Usage:* Filter chain security, stateless JWT Bearer token authentication, and role/ownership authorization.
  - *Reason:* Prevents unauthorized resource access, isolates user data, and encrypts credentials.
  - *Relevant Modules:* `SecurityConfig.java`, `JwtAuthenticationFilter.java`
- **Spring Data JPA & Hibernate:**
  - *Usage:* Object-relational mapping and repository abstraction over relational database tables.
  - *Reason:* Type-safe queries, automatic schema management, and transaction boundary enforcement.
  - *Relevant Modules:* `model/`, `repository/`
- **H2 Database (2.3.232):**
  - *Usage:* In-memory and file-based embedded SQL relational database for local development and test execution.
  - *Reason:* Zero-dependency local setup, sub-second test execution, and zero operational cost.
  - *Relevant Modules:* `src/test/resources/application.properties`
- **PostgreSQL JDBC Driver:**
  - *Usage:* Production relational database connectivity (e.g., Supabase / Neon / AWS RDS).
  - *Reason:* Enterprise ACID compliance, robust indexing, and scalable concurrent ledger storage.
  - *Relevant Modules:* `backend/pom.xml`
- **JJWT (Java JWT - 0.11.5):**
  - *Usage:* Cryptographic creation, signing, and verification of HMAC-SHA256 Bearer tokens.
  - *Reason:* Stateless authentication allowing seamless client restarts without server session tracking.
  - *Relevant Modules:* `JwtUtil.java`
- **JUnit 5 & Mockito:**
  - *Usage:* Automated unit and integration testing harness.
  - *Reason:* Regression prevention and verification of business logic, IDOR protections, and compliance constraints.
  - *Relevant Modules:* `backend/src/test/java/`

### 9.2 Web Dashboard Technology Stack
- **React 19 (`19.0.0`):**
  - *Usage:* UI component architecture and declarative rendering.
  - *Reason:* Modern concurrent rendering, clean hook composition, and reactive state management.
  - *Relevant Modules:* `dashboard_app/src/`
- **TypeScript 5.8 (`^5.8.2`):**
  - *Usage:* Strict static typing across all views, API client contracts, and UI components.
  - *Reason:* Eliminates runtime type errors, guarantees API contract safety, and enhances maintainability.
  - *Relevant Modules:* Entire `dashboard_app/`
- **Vite 6 (`6.2.0`):**
  - *Usage:* Development bundler, hot module replacement (HMR), and production tree-shaking compiler.
  - *Reason:* Blazing-fast cold starts, optimized production builds (319 KB bundle), and standard modern tooling.
  - *Relevant Modules:* `vite.config.ts`
- **Vitest 3 (`3.0.7`):**
  - *Usage:* Fast native unit testing framework powered by Vite.
  - *Reason:* Direct ESM execution, compatibility with React Testing Library, and near-instant feedback.
  - *Relevant Modules:* `src/api.test.ts`, `src/App.test.tsx`, `PrivacyCenter.test.tsx`
- **Lucide React (`^1.16.0`):**
  - *Usage:* SVG iconography across sidebar, metrics, alerts, and navigation.
  - *Reason:* Consistent aesthetic stroke tokens and zero visual clutter.
  - *Relevant Modules:* `Sidebar.tsx`, `Topbar.tsx`, domain views.

### 9.3 Mobile Cross-Platform Technology Stack
- **React Native 0.79 (`0.79.2`):**
  - *Usage:* Native mobile rendering engine compiling to iOS and Android interfaces.
  - *Reason:* High performance native touch components and cross-platform reusability.
  - *Relevant Modules:* `mobile/src/`
- **Expo SDK 53 (`~53.0.0`):**
  - *Usage:* Mobile build framework, development client, and native module bindings.
  - *Reason:* Rapid development cycles, clean OTA updates, and standard Android APK builds.
  - *Relevant Modules:* `mobile/package.json`, `app.json`
- **React Navigation 7:**
  - *Usage:* Native stack navigation and bottom tab routing.
  - *Reason:* Smooth native transition animations and type-safe navigation parameters.
  - *Relevant Modules:* `AppNavigator.tsx`
- **AsyncStorage:**
  - *Usage:* Encrypted local device key-value storage for JWT tokens and offline caches.
  - *Reason:* Offline-first application launch and persistent session management.
  - *Relevant Modules:* `AuthProvider.tsx`, `client.ts`

### 9.4 Mobile Native Android Technology Stack
- **Kotlin 1.9.x:**
  - *Usage:* Modern JVM language for native Android implementation.
  - *Relevant Modules:* `mobile_native_android/app/src/main/java/`
- **Jetpack Compose:**
  - *Usage:* Modern declarative UI toolkit for Android.
  - *Relevant Modules:* `ui/screens/`
- **Android Room (SQLite):**
  - *Usage:* Local device SQLite object mapping and offline caching.
  - *Relevant Modules:* `data/AppDatabase.kt`, `data/Daos.kt`

### 9.5 End-to-End Test Infrastructure
- **Python 3 (`unittest`):**
  - *Usage:* Automated hermetic end-to-end integration test harness.
  - *Reason:* Platform-agnostic black-box testing verifying full HTTP REST contracts independently of language-specific test runners.
  - *Relevant Modules:* `test-trips-e2e.py`, `e2e_tests/`

---

## 10. Architecture

### 10.1 High-Level Architectural Topology
```text
┌─────────────────────────────────────────────────────────────────────────┐
│                        CLIENT PRESENTATION LAYER                        │
│                                                                         │
│   ┌────────────────────────┐  ┌─────────────────────┐  ┌────────────┐   │
│   │     Web Dashboard      │  │  Cross-Platform RN  │  │ Android    │   │
│   │  (React 19 / Vite 6)   │  │   (Expo SDK 53)     │  │ Native     │   │
│   └───────────┬────────────┘  └──────────┬──────────┘  └─────┬──────┘   │
└───────────────┼──────────────────────────┼───────────────────┼──────────┘
                │                          │                   │
                ▼                          ▼                   ▼
     HTTPS / REST API (Bearer JWT Authentication & Strict CORS Filter)
                │
                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    BACKEND APPLICATION LAYER (Spring Boot 3)            │
│                                                                         │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │ Security Filter Chain: JwtAuthenticationFilter, BCrypt, CORS    │   │
│   └────────────────────────────────┬────────────────────────────────┘   │
│                                    │                                    │
│   ┌────────────────────────────────▼────────────────────────────────┐   │
│   │ REST Controllers: Auth, Dashboard, Trip, Payment, Privacy, Cap. │   │
│   └────────────────────────────────┬────────────────────────────────┘   │
│                                    │                                    │
│   ┌────────────────────────────────▼────────────────────────────────┐   │
│   │ Service Layer: IDOR Ownership Validation & Capability Engine    │   │
│   │ (UserService, TripService, UserCapabilityService, PrivacyServ.) │   │
│   └────────────────────────────────┬────────────────────────────────┘   │
│                                    │                                    │
│   ┌────────────────────────────────▼────────────────────────────────┐   │
│   │ Data Access Layer: Spring Data JPA Repositories                 │   │
│   └────────────────────────────────┬────────────────────────────────┘   │
└────────────────────────────────────┼────────────────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         PERSISTENCE DATA LAYER                          │
│                                                                         │
│   • Local Dev / Automated Tests: In-Memory H2 Database Engine           │
│   • Production Target: Managed PostgreSQL (Supabase / AWS RDS / Neon)   │
└─────────────────────────────────────────────────────────────────────────┘
```

### 10.2 Architectural Boundaries & Invariants
1. **Controller $\rightarrow$ Service $\rightarrow$ Repository Separation:** Controllers must never call repositories directly. All business validation, age gating, and IDOR ownership checks occur strictly within the service layer.
2. **IDOR Prevention:** Every query for trips, transactions, budgets, vendors, or privacy records must filter by `user.id`. Any unauthorized access attempt throws `AccessDeniedException`, mapped globally to `HTTP 403 Forbidden`.
3. **Stateless Scalability:** The backend maintains zero HTTP session state. Authentication relies entirely on signed HMAC-SHA256 JWT tokens with a 24-hour expiration window.
4. **Non-Custodial Isolation:** Zero banking secrets are ever accepted by controllers or mapped in entities.

### 10.3 The Three Pillars of Engineering Governance
The SpeDex repository is governed by three foundational, permanent pillars codified in [`.agents/AGENTS.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/.agents/AGENTS.md):
1. **Pillar 1 — Universal Product Reverse-Engineering & Intent Recovery (Rules 0–29):** Reconstruct product intent, motive, and vision before inspecting implementation; compare intended vs actual; preserve recoverable checkpoints.
2. **Pillar 2 — Universal Version Controller & Change Governance (Protocols 1–44):** Mandatory persistent version ledger in `VERSION_CONTROLLER.md`; strict `A.BC.DE.F` hierarchy; the first checkpoint before modification and final ledger after verification.
3. **Pillar 3 — Universal Modular Architecture & Change-Isolation Governance (Protocols 1–76):** Strict domain boundaries, bounded change radius, action-to-data traceability, and preservation rules ("Nothing Disappears").

### 10.4 Major System Domains & Modular Ownership Boundaries
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

#### Domain 1: Authentication & User Accounts (`auth`)
- **Responsibility**: User signup, age verification gating (<18 vs 18+), BCrypt password hashing, stateless HMAC-SHA256 JWT generation, profile management, and account status enforcement.
- **Backend Components**: `AuthController.java`, `UserService.java`, `UserRepository.java`, `User.java`, `JwtUtil.java`, `SecurityConfig.java`.
- **Frontend Components**: `LoginView.tsx`, `Sidebar.tsx` (user badge), `api.ts` (`login`, `signup`, `getMe`).
- **Mobile Components**: `AuthProvider.tsx`, `LoginScreen.tsx`.
- **Android Native Components**: `SpedexViewModel.kt` (`user`, `updateProfile`), Room `UserDao`, `Entities.kt` (`User`).
- **Tests**: `UserServiceTest.java`, `JwtUtilTest.java`, `AuthProvider.test.tsx`.

#### Domain 2: Core Expense Ledger & Velocity Analytics (`ledger`)
- **Responsibility**: Micro-transaction recording, categorization, weekly burn rate computation, spending velocity gauges, cash vs digital tagging, and monthly budget limits.
- **Backend Components**: `DashboardController.java`, `TransactionService.java`, `TransactionRepository.java`, `BudgetRepository.java`, `Transaction.java`, `Budget.java`.
- **Frontend Components**: `OverviewView.tsx`, `AnalyticsView.tsx`, `TransactionModal.tsx`, `StatsCard.tsx`.
- **Mobile Components**: `HomeScreen.tsx`, `AnalyticsScreen.tsx`.
- **Android Native Components**: `HomeScreen.kt`, `BudgetsScreen.kt`, `AnalyticsScreen.kt`, Room `TransactionDao`, `BudgetDao`.
- **Tests**: `DashboardControllerTest.java`, `App.test.tsx`.

#### Domain 3: Trips Ledger Subsystem & Multi-Currency Forex (`trips`)
- **Responsibility**: Travel budgeting, session lifecycle (Single Active Trip Invariant), foreign currency conversion with offline forex rate cache (USD, EUR, GBP, AED, SGD, THB, NPR, JPY), per-trip custom exchange rates, category percentage distribution, and secondary foreign spend analytics (`foreign_total_spend = totalSpend / exchangeRate`).
- **Backend Components**: `TripController.java`, `TripService.java`, `TripRepository.java`, `Trip.java`, `TripDto.java`, `TripDetailsDto.java`.
- **Frontend Components**: `TripsView.tsx`, `forex.ts`, `TripCard.tsx`.
- **Mobile Components**: `TripsScreen.tsx`, `TripsScreen.test.tsx`.
- **Android Native Components**: `TripsScreen.kt`, `NativeParityUnitTest.kt`, Room `TripDao`, `Entities.kt` (`Trip`).
- **E2E Infrastructure**: `mock_backend.py`, `test-trips-e2e.py` (77 tests).
- **Tests**: `TripServiceTest.java` (12 tests), `NativeParityUnitTest.kt`.

#### Domain 4: Vendor Directory & UPI Payment Preparation (`payments`)
- **Responsibility**: Local merchant directory, frequency tracking, non-custodial UPI intent generation (`upi://pay`), static and dynamic QR code generation, and server-side capability authorization gating.
- **Backend Components**: `PaymentController.java`, `UserCapabilityService.java`, `VendorRepository.java`, `Vendor.java`.
- **Frontend Components**: `PaymentsView.tsx`, `VendorsView.tsx`, `QrModal.tsx`.
- **Mobile Components**: `PaymentsScreen.tsx`, `PaymentConfirmScreen.tsx`.
- **Android Native Components**: `PayConfirmScreen.kt`, `ScannerScreen.kt`, Room `VendorDao`.
- **Tests**: `PaymentControllerTest.java`, `UserCapabilityServiceTest.java`, `PaymentConfirmScreen.test.tsx`.

#### Domain 5: DPDP Act 2023 Privacy, Age Verification & Consent (`privacy`)
- **Responsibility**: DPDP technical compliance, minor protection (<18 Financial Learning Mode), verifiable parental consent lifecycle, purpose-separated consent toggles (Essential, Analytics, Marketing, Location, AI), immutable audit logging (`PrivacyAuditLog`), machine-readable JSON data export (`Download My SpeDex Data`), right to erasure (`DELETE MY DATA`), and grievance redressal ticketing with SLA monitoring.
- **Backend Components**: `PrivacyController.java`, `CapabilityController.java`, `PrivacyService.java`, `UserCapabilityService.java`, `ConsentRecordRepository.java`, `PrivacyAuditLogRepository.java`, `PrivacyGrievanceRepository.java`.
- **Frontend Components**: `PrivacyCenter.tsx`, `CookieConsentBanner.tsx`.
- **Mobile Components**: `PrivacyScreen.tsx`, `PrivacyScreen.test.tsx`.
- **Android Native Components**: `PrivacyCenterScreen.kt`, Room `PrivacyDao`, `Entities.kt` (`PrivacySettings`, `PrivacyAuditLogItem`).
- **Tests**: `PrivacyServiceTest.java` (13 tests), `PrivacyCenter.test.tsx` (2 tests), `NativeParityUnitTest.kt`.

#### Domain 6: Mobile Sync Simulation & Companion Diagnostics (`simulator`)
- **Responsibility**: Side-by-side live simulator embedded in web dashboard, QR synchronization diagnostics, and cross-client preview.
- **Frontend Components**: `MobileSyncSimulator.tsx`.

---

### 10.5 Architectural Ownership Map

| Layer | Domain 1 (Auth) | Domain 2 (Ledger) | Domain 3 (Trips) | Domain 4 (Payments) | Domain 5 (Privacy) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Web View** | `LoginView` | `OverviewView`, `AnalyticsView` | `TripsView` | `PaymentsView`, `VendorsView` | `PrivacyCenter` |
| **Mobile Screen** | `LoginScreen` | `HomeScreen`, `AnalyticsScreen` | `TripsScreen` | `PaymentsScreen`, `PaymentConfirmScreen` | `PrivacyScreen` |
| **Android Screen**| `HomeScreen` (Profile) | `HomeScreen`, `BudgetsScreen`, `AnalyticsScreen` | `TripsScreen` | `ScannerScreen`, `PayConfirmScreen` | `PrivacyCenterScreen` |
| **Key Features** | Age verification, JWT | Weekly burn, Velocity | Forex cache, Trip sessions | Payee quick-pay, UPI QR | Consent switches, Data export, Audit log |
| **Service Layer** | `UserService` | `TransactionService` | `TripService` | `UserCapabilityService` | `PrivacyService`, `UserCapabilityService` |
| **Controller** | `AuthController` | `DashboardController` | `TripController` | `PaymentController` | `PrivacyController`, `CapabilityController` |
| **Data Entity** | `User` | `Transaction`, `Budget` | `Trip` | `Vendor` | `ConsentRecord`, `PrivacyAuditLog`, `PrivacyGrievance` |
| **Shared Infra** | `JwtAuthenticationFilter`, `SecurityConfig` | Currency formatters, DB | Forex cache, DB | QR rendering, Capabilities | Audit engine, SLA monitoring |

---

### 10.6 Traceability Engine (Action $\rightarrow$ Data)

Every user interaction traces through a deterministic, unidirectional sequence:
`PAGE -> SECTION -> FEATURE -> COMPONENT -> ACTION -> FUNCTION -> SERVICE -> API / REST -> DATA REPOSITORY`

#### Trace A: Starting a Cross-Border International Trip
```text
TripsView / TripsScreen (Page)
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
PaymentsView / ScannerScreen (Page)
 └── Vendor Card / QR Reader (Section)
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

### 10.7 Change Radius Protocol & Change Manifest Model

Before executing any modification, calculate the **Change Radius**:
```text
Files classified as:
├── DIRECT:     Files directly implementing the requested change.
├── RELATED:    Files immediately supporting the change (types, tests).
├── DEPENDENT:  Files consuming the feature (views, callers).
├── SHARED:     Shared infrastructure (CORS, JWT filter, shared UI components).
└── UNRELATED:  All other modules (STRICTLY PROHIBITED FROM MODIFICATION).
```

Allowed modification radius is **Direct + necessary Related**.

---

### 10.8 Module Registry & Public API Contracts

| Module | Owning Domain | Public Contract Entry Point | External Consumers |
| :--- | :--- | :--- | :--- |
| **`auth`** | Authentication | `api.login()`, `api.signup()`, `SecurityConfig` | App shell, navigation guards |
| **`trips`** | Trips Ledger | `TripService`, `api.getTrips()`, `api.startTrip()`, `forex.ts` | `TripsView`, `TripsScreen`, E2E suite |
| **`payments`** | Payments | `PaymentController`, `UserCapabilityService`, `api.preparePayment()` | `PaymentsView`, `PaymentsScreen`, Android Native |
| **`privacy`** | Privacy & DPDP | `PrivacyService`, `UserCapabilityService`, `PrivacyCenter` | Settings, Auth signup, Capability guards |
| **`ledger`** | Core Ledger | `TransactionService`, `DashboardController`, `api.getDashboard()` | `OverviewView`, `AnalyticsView`, `HomeScreen` |
| **`ui_shared`** | Shared Infra | `components/common/`, `Sidebar.tsx`, `forex.ts` | All views and screens |

---

### 10.9 Anti-Cascade & Preservation Invariants

1. **No Cascade Cleanup**: Never perform opportunistic "cleanups" or refactoring of unrelated files during a targeted fix.
2. **File Replacement Protection**: Never rewrite an entire file when a targeted, surgical change achieves the goal.
3. **Preservation Rule ("Nothing Disappears")**: Moving or updating code must preserve all existing edge cases, validations, error responses, and test assertions.
4. **Single Source of Truth**:
   - Application version: `dashboard_app/src/version.ts` synchronized with `pom.xml`, `package.json`, and `VERSION_CONTROLLER.md`.
   - Accounting ledger base currency: Indian Rupee (INR ₹).
   - Server capability enforcement: `UserCapabilityService.java`.

---

## 11. Repository & File Structure

```text
Spedex/
├── .agents/
│   └── AGENTS.md                   # Operational governance, Product Intent, and Version Control specifications
├── backend/
│   ├── mvnw / mvnw.cmd             # Maven wrapper scripts
│   ├── pom.xml                     # Maven dependency & build manifest (Java 17, Spring Boot 3.4.1)
│   └── src/
│       ├── main/java/com/spedex/
│       │   ├── SpedexApplication.java    # Application main entry point
│       │   ├── controller/
│       │   │   ├── AuthController.java        # Login, register, profile management
│       │   │   ├── CapabilityController.java  # User capability inquiry (/api/capabilities)
│       │   │   ├── DashboardController.java   # Aggregated bundle, overview metrics, vendors, budgets
│       │   │   ├── HealthController.java      # Application health check (/api/health)
│       │   │   ├── PaymentController.java     # Payment preparation with minor capability blocking
│       │   │   ├── PrivacyController.java     # DPDP consent, export, erasure, grievances, audit logs
│       │   │   └── TripController.java        # Travel trips, active session tracking, trip expenses
│       │   ├── dto/                           # Data transfer objects for REST payloads
│       │   ├── exception/
│       │   │   └── GlobalExceptionHandler.java # Deterministic error mapping (400, 401, 403, 404, 500)
│       │   ├── model/                         # JPA entities (User, Transaction, Trip, Vendor, PrivacyAuditLog, etc.)
│       │   ├── repository/                    # Spring Data JPA repositories
│       │   ├── security/                      # JWT filter, token utilities, security configuration
│       │   └── service/                       # Core business logic & authorization engines
│       └── test/java/com/spedex/              # 55 automated backend unit & integration tests
├── dashboard_app/
│   ├── package.json                # Web app dependencies (React 19, Vite 6, TypeScript 5.8)
│   ├── vite.config.ts              # Vite compiler configuration
│   ├── vercel.json                 # Vercel SPA rewrite configuration
│   └── src/
│       ├── App.tsx                 # Core router and view coordinator
│       ├── api.ts                  # REST API client with JWT interception and fallback
│       ├── types.ts                # TypeScript data interfaces
│       ├── version.ts              # Authoritative frontend runtime version constant (2.05.00.0)
│       ├── components/             # Reusable UI components
│       │   ├── common/             # Skeletons, toasts
│       │   ├── landing/            # LandingPage entry component
│       │   ├── layout/             # Sidebar, Topbar, BrandLockup
│       │   ├── mobile/             # MobileSyncSimulator companion
│       │   ├── privacy/            # PrivacyCenter, CookieConsentBanner, LegalDocViewer
│       │   └── vendors/            # AddVendorModal
│       └── views/                  # Domain views (Home, Payments, Analytics, Budget, Trips, Settings, Auth)
├── mobile/
│   ├── package.json                # React Native / Expo dependencies
│   ├── app.json                    # Expo configuration
│   └── src/
│       ├── api/client.ts           # Mobile REST client with AsyncStorage token injection
│       ├── auth/AuthProvider.tsx   # Mobile auth state provider
│       ├── navigation/             # React Navigation stack
│       ├── screens/                # Touch-optimized mobile screens
│       └── theme/                  # INR currency note design tokens
├── mobile_native_android/          # Native Android Kotlin / Jetpack Compose application
├── e2e_tests/                      # Python mock backend & 77-case E2E integration test suite
├── Dockerfile                      # Multi-stage Docker build configuration
├── SPEDEX.md                       # Single Authoritative Project Master Knowledge File
├── VERSION_CONTROLLER.md           # Mandatory Persistent Version Controller Ledger
└── test-trips-e2e.py               # E2E test runner harness
```

---

## 12. Data Model

### 12.1 Entity Relationship Diagram
```text
┌───────────────────┐       1:N       ┌───────────────────┐
│       User        ├────────────────►│    Transaction    │
│  (id, email, age, │                 │ (id, amount, mode,│
│   isMinor, etc.)  ├─────────┐       │  category, time)  │
└─────────┬─────────┘         │       └───────────────────┘
          │ 1:N               │ 1:N
          ▼                   ▼       ┌───────────────────┐
┌───────────────────┐ ┌───────────────┤      Vendor       │
│       Trip        │ │               │ (id, name, vpa,   │
│ (id, name, budget,│ │               │  category, mode)  │
│  status, dates)   │ │               └───────────────────┘
└─────────┬─────────┘ │
          │ 1:N       │ 1:N           ┌───────────────────┐
          └───────────┼──────────────►│  PrivacyAuditLog  │
                      │               │ (id, eventType,   │
                      │ 1:N           │  description)     │
                      ├──────────────►└───────────────────┘
                      │               ┌───────────────────┐
                      │ 1:N           │   ConsentRecord   │
                      ├──────────────►│ (id, consentType, │
                      │               │  action, time)    │
                      │ 1:N           └───────────────────┘
                      ├──────────────►┌───────────────────┐
                      │               │  PrivacyGrievance │
                      │ 1:N           │ (id, ticketId,    │
                      └──────────────►│  status, response)│
                                      └───────────────────┘
                                      ┌───────────────────┐
                                      │      Budget       │
                                      │ (id, category,    │
                                      │  monthlyLimit)    │
                                      └───────────────────┘
```

### 12.2 Entity Details & Schema Constraints
1. **`users` (`User.java`):**
   - `id`: BIGINT (Primary Key, Auto Increment)
   - `email`: VARCHAR(255) (Unique, Not Null)
   - `password_hash`: VARCHAR(255) (Not Null, BCrypt Encrypted)
   - `name`: VARCHAR(255) (Not Null)
   - `age`: INTEGER (User-declared age, used for deterministic age gating)
   - `is_minor`: BOOLEAN (True if age < 18; gates all payment endpoints)
   - `guardian_name`: VARCHAR(255) (Minimal guardian collection for minor accounts)
   - `guardian_email`: VARCHAR(255) (Minimal guardian collection for minor accounts)
   - `guardian_consent_status`: VARCHAR(50) (`PENDING`, `VERIFIED`, `REJECTED`, `WITHDRAWN`)
   - `analytics_consent`: BOOLEAN (Purpose-separated telemetry toggle; permanently false for minors)
   - `marketing_consent`: BOOLEAN (Purpose-separated marketing toggle; permanently false for minors)
   - `location_consent`: BOOLEAN (Purpose-separated city tagging toggle; permanently false for minors)
   - `ai_consent`: BOOLEAN (Purpose-separated smart categorization toggle; permanently false for minors)
   - `is_erased`: BOOLEAN (Flag indicating executed Section 12 erasure)
   - `erased_at`: TIMESTAMP (Timestamp of account anonymization)

2. **`transactions` (`Transaction.java`):**
   - `id`: BIGINT (Primary Key)
   - `user_id`: BIGINT (Foreign Key referencing users, Not Null)
   - `vendor_id`: BIGINT (Foreign Key referencing vendors, Nullable)
   - `trip_id`: BIGINT (Foreign Key referencing trips, Nullable)
   - `amount`: DOUBLE (Transaction figure in INR ₹, Not Null)
   - `category`: VARCHAR(100) (Food, Travel, Hostel, Shopping, Bills, Other)
   - `payment_method`: VARCHAR(50) (`CASH`, `DIGITAL`, `UPI`)
   - `note`: VARCHAR(500)
   - `timestamp`: TIMESTAMP (Not Null)

3. **`trips` (`Trip.java`):**
   - `id`: BIGINT (Primary Key)
   - `user_id`: BIGINT (Foreign Key referencing users, Not Null)
   - `name`: VARCHAR(255) (Trip destination or label, Not Null)
   - `budget`: DOUBLE (Trip target budget, Not Null)
   - `status`: ENUM (`ACTIVE`, `COMPLETED`)
   - `start_date`: TIMESTAMP (Not Null)
   - `end_date`: TIMESTAMP (Nullable)

4. **`privacy_audit_logs` (`PrivacyAuditLog.java`):**
   - `id`: BIGINT (Primary Key)
   - `user_id`: BIGINT (Foreign Key, On Delete Set Null)
   - `user_email`: VARCHAR(255) (Not Null)
   - `event_type`: VARCHAR(100) (`ACCOUNT_CREATED`, `AGE_CATEGORY_ASSIGNED`, `PAYMENT_ACTION_BLOCKED`, `CONSENT_UPDATED`, `CONSENT_WITHDRAWN`, `DATA_EXPORT_REQUESTED`, `ACCOUNT_DELETION_REQUESTED`, `GRIEVANCE_SUBMITTED`)
   - `description`: VARCHAR(1000)
   - `timestamp`: TIMESTAMP (Not Null)

5. **`consent_records` (`ConsentRecord.java`):**
   - `id`: BIGINT (Primary Key)
   - `user_id`: BIGINT (Foreign Key referencing users, Not Null)
   - `consent_type`: VARCHAR(100) (`TERMS_AND_PRIVACY`, `ANALYTICS`, `MARKETING`, `LOCATION`, `AI`, `RIGHT_TO_ERASURE`)
   - `action`: VARCHAR(50) (`GRANTED`, `UPDATED`, `WITHDRAWN`)
   - `details`: VARCHAR(1000)
   - `timestamp`: TIMESTAMP (Not Null)

---

## 13. API & Integration Layer

All endpoints run on base path `/api`. Unauthenticated requests to protected endpoints return `401 Unauthorized`. Accessing resources owned by another user returns `403 Forbidden`.

### 13.1 Authentication Endpoints (`AuthController.java`)
- `POST /api/auth/register`: Create user account. Body: `SignUpRequestDto`. Returns JWT token and user profile.
- `POST /api/auth/login`: Authenticate credentials. Body: `LoginRequestDto`. Returns JWT token.
- `GET /api/auth/me`: Retrieve authenticated user profile. Returns `SpedexUserDto`.
- `PUT /api/auth/profile`: Update user display profile. Body: updated profile fields.

### 13.2 Capability Endpoints (`CapabilityController.java`)
- `GET /api/capabilities`: Retrieve server-authorized capabilities for current user. Returns `UserCapabilitiesDto`:
  ```json
  {
    "canInitiatePayment": false,
    "canOpenPaymentLink": false,
    "canSavePaymentMethod": false,
    "canUseMerchantQR": false,
    "canRecordManualTransaction": true,
    "canManageBudgets": true,
    "canUseAnalytics": true,
    "canReceiveMarketing": false,
    "isMinor": true,
    "mode": "FINANCIAL_LEARNING_JOURNAL",
    "restrictionReason": "Account registered as under 18 years of age. Financial Learning / Journal Mode active."
  }
  ```

### 13.3 Dashboard & Overview Endpoints (`DashboardController.java`)
- `GET /api/dashboard/bundle`: Aggregated bundle containing user info, overview metrics, vendors, budgets, and analytics.
- `GET /api/dashboard/overview`: High-level metrics (total spent, daily burn, recent transaction ledger).
- `GET /api/analytics`: Category breakdown percentages, day-of-week spend distribution, and spending velocity.
- `GET /api/vendors`: Directory of registered vendors and saved UPI references.
- `POST /api/vendors`: Register a new vendor. Body: `VendorDto`.
- `GET /api/budget/screen`: Budget metrics, category progress bars, and savings tips.

### 13.4 Trips Ledger Endpoints (`TripController.java`)
- `GET /api/trips`: List all trips for current authenticated user.
- `POST /api/trips`: Start a new trip (automatically auto-closes any existing active trip). Body: `TripDto`.
- `GET /api/trips/{id}`: Detailed summary of trip, list of associated transactions, cash/online splits, and category distribution.
- `POST /api/trips/{id}/complete`: Mark active trip as completed.
- `POST /api/trips/{id}/transactions`: Log an expense transaction directly against a trip. Body: `TransactionDto`.

### 13.5 Payment Endpoints (`PaymentController.java`)
- `POST /api/payments/prepare`: Prepares payment intent and UPI link.
  - *Enforcement:* Verified against `UserCapabilityService`. If minor account, immediately returns `HTTP 403 Forbidden` (`PAYMENT_ACTION_BLOCKED_FOR_MINOR`) and logs audit entry.

### 13.6 Privacy & Compliance Endpoints (`PrivacyController.java`)
- `GET /api/privacy/legal/{docType}`: Public endpoint serving legal policies (`terms`, `privacy`, `consent`, `cookies`, `child-privacy`, `data-retention`, `grievance`, `subprocessors`).
- `GET /api/privacy/settings`: Authenticated user consent and guardian verification status.
- `POST /api/privacy/consent`: Update purpose-separated consent preferences. Body: `ConsentUpdateRequestDto`.
- `POST /api/privacy/consent/withdraw`: Immediately withdraw a specific consent purpose (`ANALYTICS`, `MARKETING`, `LOCATION`, `AI`).
- `POST /api/privacy/guardian-consent/request`: Submit guardian name and email to issue verification token.
- `GET /api/privacy/guardian-consent/verify`: Public verification link handler validating guardian token.
- `GET /api/privacy/export`: Section 11 Right to Access personal data in machine-readable JSON format.
- `POST /api/privacy/erase`: Section 12 Right to Erasure requiring exact body `{"confirmationText": "DELETE MY DATA"}`.
- `GET /api/privacy/audit-logs`: Retrieve historical audit trail for current user.
- `POST /api/privacy/grievance`: File formal grievance ticket under Section 13.
- `GET /api/privacy/grievance`: Retrieve list and status of filed grievances.

---

## 14. Security

### 14.1 Security Invariants & Principles
1. **Zero Credential Storage:** SpeDex strictly enforces a zero-trust model regarding banking secrets. No banking passwords, UPI PINs, debit/credit card PANs, or CVVs are ever requested, processed, or persisted.
2. **Stateless JWT Authentication:** Authentication relies on signed HMAC-SHA256 tokens. Tokens expire after 24 hours (86,400,000 ms). Secret keys are injected via environment variables (`JWT_SECRET`) with deterministic development fallbacks.
3. **Password Hashing:** Passwords are hashed using `BCryptPasswordEncoder` with standard computational work factor (10 rounds). Plaintext passwords are never logged or stored.
4. **Strict IDOR Prevention:** All controllers extract user identity from the `SecurityContext` principal. Repository queries enforce tenant isolation (`findByUser` / `findByUserId`). Cross-tenant access triggers `AccessDeniedException` mapped to `HTTP 403 Forbidden`.
5. **CORS Hardening:** Configured in `SecurityConfig.java` to allow authorized frontend origins while blocking malicious cross-origin requests.

---

## 15. Privacy & Data Governance (DPDP Act 2023)

SpeDex incorporates a complete technical data governance layer supporting India's **Digital Personal Data Protection Act, 2023 (DPDP Act)** and **DPDP Rules 2025**. Under the DPDP Act, SpeDex acts as a Data Fiduciary regarding user profile information, adhering strictly to:

1. **Data Minimization (Section 6(1)):** Collects only data strictly necessary for expense journaling and travel budgeting. Banking secrets (UPI MPINs, card CVVs, net banking passwords) are never requested, transmitted, or stored.
2. **Purpose Limitation (Section 6(2)):** Personal data gathered for transactional ledgering is never repurposed for behavioural advertising or third-party data broker monetization.
3. **Purpose-Separated Consent (Section 6(1) & 6(4)):** Granular opt-in switches for Telemetry, Marketing, Geolocation, and AI. Withdrawing consent is 1-click and immediate without penalizing core ledger tracking.
4. **Minor Protection (Section 9):** Individuals under 18 operate in **Financial Learning / Journal Mode**. Live payment execution is prohibited server-side, and profiling/behavioral tracking is permanently disabled.
5. **Parental Verification Link Lifecycle:** Minimal guardian data collection (parent name and email only) with tokenized link verification (`PENDING`, `VERIFIED`, `REJECTED`, `WITHDRAWN`).
6. **Immutable Privacy Audit Trail:** All safety-critical and consent events are recorded in `privacy_audit_logs` for auditable compliance verification.
7. **Right to Access / Data Portability (Section 11):** 1-click structured machine-readable JSON export (`GET /api/privacy/export`) covering user profile, capabilities, transactions, vendors, budgets, reminders, trips, consent records, audit logs, and grievances.
8. **Right to Erasure (Section 12):** Immediate irreversible anonymization requiring exact confirmation text `DELETE MY DATA`.
9. **Grievance Redressal (Section 13):** In-app ticketing system with automated SLA tracking (48h acknowledgement, 15–30 day redressal) and Data Protection Officer contact (`dpo@spedex.internal`).
10. **Third-Party Subprocessor Registry:** Complete public transparency register disclosing Render Inc. (API hosting), Vercel Inc. (Web CDN), Supabase/AWS (PostgreSQL storage), and Google Fonts (typography).

### 15.1 Server-Side Capability Enforcement Matrix
Client-side flags (`isMinor`) are never trusted for authorization. The centralized `UserCapabilityService` enforces restrictions server-side:

| Action / Capability | Adult (18+) | Minor (<18) | Enforcement Mechanism |
|---|---|---|---|
| **Initiate Payment (`POST /api/payments/prepare`)** | Allowed | **Blocked (HTTP 403)** | `UserCapabilityService.canInitiatePayment()` checks user age; logs `PAYMENT_ACTION_BLOCKED` in audit ledger |
| **Complete Payment (`POST /api/payments/{id}/complete`)** | Allowed | **Blocked (HTTP 403)** | Server-side validation |
| **Generate UPI QR Code** | Allowed | **Suppressed / Replaced** | Web & Mobile UI hides live QR; displays educational notice |
| **Scan UPI Payment QR** | Allowed | **Suppressed** | Mobile camera suppresses UPI scanner for minors |
| **Record Manual Expense** | Allowed | **Allowed** | Full access to educational expense bookkeeping |
| **Manage Budgets & Goals** | Allowed | **Allowed** | Full access to hostel budgeting tools |
| **Telemetry Analytics** | Opt-In Required | **Permanently Blocked** | Section 9 prohibits minor tracking |
| **Educational Marketing** | Opt-In Required | **Permanently Blocked** | Section 9 prohibits targeted marketing to minors |

### 15.2 Purpose-Separated Consent Architecture (Section 6)

| Purpose Key | Category | Default Adult | Default Minor | Description |
|---|---|---|---|---|
| `TERMS_AND_PRIVACY` | Essential | Granted | Granted | Essential platform operation (authentication, password hash, encryption) |
| `ANALYTICS` | Telemetry | Opt-In (false) | **Permanently False** | Anonymous performance benchmarks and system crash diagnostics |
| `MARKETING` | Communication | Opt-In (false) | **Permanently False** | Student financial literacy digests and product release summaries |
| `LOCATION` | Contextual | Opt-In (false) | **Permanently False** | Approximate city-level tagging for active travel trips |
| `AI` | Smart Features | Opt-In (false) | **Permanently False** | Predictive heuristics for automatic expense categorization |

### 15.3 Third-Party Subprocessor Registry

| Subprocessor | Operational Purpose | Data Handled | Jurisdiction | Security Safeguards |
|---|---|---|---|---|
| **Render Inc.** | Backend API Hosting | Encrypted application payloads, JWTs | Frankfurt (EU) / Singapore (APAC) | TLS 1.3 in transit, ISO 27001, SOC 2 Type II |
| **Vercel Inc.** | Web Dashboard CDN | Static client bundle, edge caching | Global Edge / Mumbai PoP | SOC 2 Type II, edge origin shield |
| **Supabase / AWS** | Managed PostgreSQL DB | Hashed passwords, encrypted ledgers | Singapore (`ap-southeast-1`) | AES-256 at rest, VPC isolation, Automated Backups |
| **Google Fonts** | Typography Assets | CDN caching of font files | Global Anycast | Zero PII logging, cookieless domain |

### 15.4 Grievance Redressal SLA & Escalation Hierarchy (Section 13)
- **Acknowledgement SLA:** Within 48 hours of ticket submission.
- **Resolution SLA:** Within 15 to 30 calendar days.
- **Data Protection Officer (DPO) Contact:** `dpo@spedex.internal`.
- **Statutory Escalation:** If unsatisfied with internal grievance resolution, data principals have the explicit statutory right to escalate disputes to the **Data Protection Board of India (DPBI)** per DPDP Act Section 13(3).

---

## 16. Performance & Reliability

1. **High-Frequency Ledger Query Performance:** Database indexes on `transactions(user_id, timestamp)` ensure fast ledger rendering even for accounts with thousands of micro-transactions.
2. **Sub-Second Test Execution:** The full 77-case Python E2E test suite executes in **0.287 seconds** against the hermetic mock backend.
3. **Optimized Client Bundles:** Vite production build bundles the entire dashboard into **319 KB of JavaScript (91 KB gzip)**, ensuring instant mobile web loading over 3G/4G campus networks.
4. **State Machine Integrity:** The Single Active Trip Invariant prevents concurrent open trips, eliminating data corruption in travel expense aggregation.
5. **Ephemeral Storage Mitigation:** In development/free-tier container hosting, H2 in-memory databases reset upon container restart. For production deployments, configuration seamlessly switches to managed PostgreSQL via standard JDBC environment variables.

---

## 17. Testing & Verification Standard

SpeDex enforces a mandatory **4-Tier Verification Matrix**. No milestone or version release is valid without all 4 suites passing with zero errors:

| Test Tier | Scope & Framework | Command | Passing Metric | Current Status |
|---|---|---|---|---|
| **Tier 1: Backend** | Spring Boot 3 / JUnit 5 / Mockito | `./mvnw.cmd test` | 55 / 55 Passed | ✅ **100% PASS** |
| **Tier 2: Web Dashboard** | React 19 / Vitest 3 / RTL / Vite Build | `npm test -- --run` & `npm run build` | 9 / 9 Passed (0 build errors) | ✅ **100% PASS** |
| **Tier 3: Mobile App** | React Native / Jest / TypeScript | `npm test` & `npx tsc --noEmit` | 7 / 7 Passed (0 type errors) | ✅ **100% PASS** |
| **Tier 4: End-to-End** | Python 3 / Hermetic Mock Backend | `python test-trips-e2e.py` | 77 / 77 Passed | ✅ **100% PASS** |
| **TOTAL ECOSYSTEM** | Full-Stack Platform Matrix | — | **148 / 148 Tests Passed** | ✅ **100% VERIFIED** |

### Automated Test Coverage Breakdown
- `UserCapabilityServiceTest`: 3 tests verifying adult capabilities, minor restrictions, and erased user capabilities.
- `PaymentControllerTest`: 2 tests verifying HTTP 403 Forbidden for minor payment preparations and HTTP 200 for adult payments.
- `PrivacyServiceTest`: 13 tests covering consent update, consent withdrawal, guardian consent lifecycle, data export, account erasure, and grievance management.
- `TripServiceTest`: 10 tests covering trip creation, auto-closing active trips, transaction linking, and category breakdown math.
- `UserServiceTest`: 10 tests covering registration, age gating, password hashing, and profile updates.
- `GlobalExceptionHandlerTest`: 6 tests verifying HTTP 400, 401, 403, 404, 500 error code mapping.
- `JwtUtilTest` & `SecurityConfigTest`: 7 tests validating token issuance, claims extraction, expiration, and filter chains.
- `PrivacyCenter.test.tsx` & `App.test.tsx`: 4 tests validating consent UI, modal state, and navigation.
- `api.test.ts`: 5 tests validating API request formatting, auth token headers, and network fallbacks.
- `PrivacyScreen.test.tsx`, `TripsScreen.test.tsx`, `PaymentConfirmScreen.test.tsx`, `AuthProvider.test.tsx`: 7 mobile tests verifying mobile auth, privacy toggles, and trip screen renders.
- `test_trips_e2e.py`: 77 comprehensive black-box integration tests covering the entire REST API surface.

---

## 18. Historical Error Inventory & Resolution Ledger

| Error ID | Version | Description & Symptoms | Root Cause | Architectural Fix | Current Status |
|---|---|---|---|---|---|
| **ERR-001** | `2.01.00.0` | Overlapping active trips caused transactions to link to multiple trips simultaneously, corrupting travel expense totals. | Lack of active trip state transition guard in `TripService`. | Implemented auto-closure logic: creating a new active trip automatically marks any existing active trip as `COMPLETED`. Verified with E2E Tier 2 tests. | `RESOLVED` |
| **ERR-002** | `2.02.00.0` | Minor protection could be bypassed if a client forged `isMinor: false` during payment preparation. | Trusting client-supplied flags rather than server-side declared age evaluation. | Introduced centralized `UserCapabilityService` that checks age on the server, rejects minor payments with `HTTP 403 Forbidden`, and logs `PAYMENT_ACTION_BLOCKED`. | `RESOLVED` |
| **ERR-003** | `2.03.00.0` | Monolithic `App.tsx` (1,642 lines) mingled navigation, vendor state, filter pipelines, and view rendering, creating high cognitive load and fragile refactoring. | Accumulation of rapid prototype code in root component. | Modularized into domain views (`HomeView`, `PaymentsView`, `AnalyticsView`, `BudgetView`, `TripsView`, `SettingsView`, `AuthView`), reducing `App.tsx` to 290 lines while maintaining 100% test parity. | `RESOLVED` |
| **ERR-004** | `2.04.00.0` | Discrepancy between documentation version claims and manifest versions (`package.json`, `pom.xml`). | Manual versioning without strict release governance. | Adopted the Universal Version Control & Change Governance System with authoritative `A.BC.DE.F` hierarchy and mandatory `VERSION_CONTROLLER.md`. | `RESOLVED` |
| **ERR-005** | `2.04.01.0` | Live application UI had hardcoded version strings that drifted from project manifests. | Decentralized version definitions across components. | Established `dashboard_app/src/version.ts` as the single runtime source of truth, dynamically consumed by `Sidebar.tsx`, `LandingPage.tsx`, and fixed UI indicators. | `RESOLVED` |
| **ERR-006** | `2.05.00.0` | Consent withdrawal required updating the entire settings payload, risking race conditions. | Absence of an atomic single-purpose consent withdrawal endpoint. | Implemented atomic `POST /api/privacy/consent/withdraw` with immediate `CONSENT_WITHDRAWN` audit logging. | `RESOLVED` |
| **ERR-007** | `2.05.00.0` | Ephemeral H2 database on container sleep caused unexpected data loss during cloud evaluation. | In-memory database persistence lifecycle tied to container instance. | Documented production deployment architecture utilizing managed PostgreSQL (Supabase / AWS RDS) with JDBC profile switching. | `RESOLVED & DOCUMENTED` |

---

## 19. Authoritative Decisions

- **DEC-001 (Product Identity):** SpeDex is an expense ledger, travel journal, and spending awareness platform. It is **not** a bank or payment processor.
- **DEC-002 (Target Focus):** Core UX is optimized for high-frequency, small, everyday transactions (₹10–₹200) common among students and hostelites.
- **DEC-003 (Merchant Primacy):** Frequently used merchants are first-class entities with dedicated spend aggregates and saved UPI references.
- **DEC-004 (Age Gating & Minor Mode):** Users under 18 operate in **Financial Learning / Journal Mode**. Live payment execution is strictly blocked server-side, but manual transaction logging, budgeting, and educational analytics remain accessible.
- **DEC-005 (Server-Side Authorization):** Client flags (`isMinor`) are never trusted. The server-side `UserCapabilityService` is the sole authority for operational permissions.
- **DEC-006 (Zero Credential Storage):** Banking credentials (UPI PINs, card CVVs, net banking passwords) are never stored or handled.
- **DEC-007 (Single Active Trip Invariant):** A user can have at most one active trip at any time. Starting a new trip automatically completes any previous active trip.
- **DEC-008 (INR Banknote Aesthetic):** The visual system strictly adheres to Indian Rupee banknote palettes (deep emerald `#0d2818`, gold `#d4af37`, warm sand `#fdfbf7`, `Cormorant Garamond` serif headings, `Sora` sans body).
- **DEC-009 (Universal Version Governance):** The platform strictly operates under the `A.BC.DE.F` version hierarchy with root [`VERSION_CONTROLLER.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/VERSION_CONTROLLER.md) as the persistent source of truth.
- **DEC-010 (Two Authoritative PM Files):** Exactly two authoritative project-management documentation files are maintained at the root: `SPEDEX.md` (knowledge master) and `VERSION_CONTROLLER.md` (version ledger).

---

## 20. Guardrails & Rules for Future Agents

1. **Reverse Engineer Before Modifying:** Never start by coding. Inspect existing code, tests, and documentation before altering any file.
2. **Never Treat SpeDex as a Payment Provider:** Never implement direct fund transfers, custodial wallets, or payment processing. SpeDex only facilitates payment references (UPI URIs / QRs) and records expenses.
3. **Preserve Minor Protections:** Never weaken minor restrictions for convenience. Under-18 accounts must always receive `HTTP 403 Forbidden` on payment preparation endpoints.
4. **Never Disable Minor Manual Logging:** Do not lock minors out of the application. Minors must retain full access to manual transaction entry, budgeting, and educational insights.
5. **Zero Credential Storage Invariant:** Never create fields, models, or forms that capture UPI MPINs, card numbers, CVVs, or bank account passwords.
6. **Strict IDOR Tenant Isolation:** Always validate user ownership in service methods (`user.id == resource.user.id`). Never expose another user's financial data.
7. **Mandatory Version Controller Gate:** Always check [`VERSION_CONTROLLER.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/VERSION_CONTROLLER.md) before making changes, classify impact (`MAJOR`, `SUB-VERSION`, `FUNCTIONAL`, `PATCH`), and update it upon completion.
8. **Preserve 100% Test Pass Rate:** Every change must verify all 4 test suites (Backend 55, Dashboard 9, Mobile 7, E2E 77 = 148 total tests). Never commit failing tests.
9. **Maintain INR Visual Identity:** Preserve the emerald and gold currency note palette with `Cormorant Garamond` and `Sora` fonts. Avoid generic flat templates.
10. **Do Not Fabricate Completion:** Never claim a feature is implemented or verified unless demonstrated by actual code and passing tests.
11. **Do Not Overwrite Historical Intent:** Product intent recovered from historical decisions takes precedence over accidental code drift.
12. **Living Master Document:** Whenever meaningful changes occur, update `SPEDEX.md` and `VERSION_CONTROLLER.md` synchronously.

---

## 21. Current State

- **Branch:** `main` (Git working tree clean).
- **Active Version:** `2.06.00.0` (Jetpack Compose Android Native Feature Parity).
- **Backend Health:** Spring Boot 3.4.4 compiles cleanly; 57/57 JUnit tests pass.
- **Dashboard Health:** React 19 / Vite 6 builds cleanly (0 errors); 9/9 Vitest tests pass.
- **Mobile Health:** Expo React Native passes all 7 Jest tests; `npx tsc --noEmit` clean with 0 type errors.
- **Android Native Health:** Kotlin Compose Room entities, DAOs, ViewModel, TripsScreen, and PrivacyCenterScreen implemented; `NativeParityUnitTest.kt` passes.
- **E2E Suite:** Hermetic Python test harness passes all 77 test cases in 0.566 seconds.
- **Total Ecosystem Health:** 150 / 150 automated tests passing (100% pass rate).

### 21.1 Three-Layer Memory Synchronization

| Memory Layer | Key Artifacts | Synchronization Status | Notes |
| :--- | :--- | :--- | :--- |
| **Layer 1: Product Memory** | Vision, aesthetic rules, DPDP legal constraints | **SYNCHRONIZED** | INR currency note theme, non-custodial fintech bounds preserved |
| **Layer 2: Engineering Memory** | `SPEDEX.md` (Product Master), `VERSION_CONTROLLER.md` (Version Control Master) | **SYNCHRONIZED** | Mandatory persistent version controller, audit logs, and status active |
| **Layer 3: Actual State** | Git commits, source code, build configs, dependencies | **SYNCHRONIZED** | Clean working tree on `main`, all 4 verification test suites 100% passing (150/150) |

### 21.2 Component Health Matrix

| Subsystem | Tech Stack | Unit Tests | Build / Typecheck | Health State |
| :--- | :--- | :--- | :--- | :--- |
| **Backend API** | Spring Boot 3.x / Java 17 | 57 / 57 PASS | Maven compile clean | **OPERATIONAL** |
| **Dashboard App** | React 19 / Vite 6 / TS 5.8 | 9 / 9 PASS | `vite build` clean (0 errors) | **OPERATIONAL** |
| **Mobile App** | Expo SDK 53 / React Native 0.79 | 7 / 7 PASS | `tsc --noEmit` clean (0 errors) | **OPERATIONAL** |
| **Android Native**| Kotlin / Jetpack Compose | Unit tests PASS | Room schema v2 | **OPERATIONAL** |
| **E2E Test Suite** | Python 3.x / Mock Backend | 77 / 77 PASS | Clean exit code 0 | **OPERATIONAL** |

### 21.3 Risk Register & Mitigations

| Risk Factor | Probability | Impact | Mitigation Strategy |
| :--- | :--- | :--- | :--- |
| **Ephemeral H2 Database on Render** | HIGH | MEDIUM | Local disk resets on free-tier sleep cycles. Documented production configuration to connect managed PostgreSQL (Supabase / Neon / Render Postgres). |
| **JWT Key Rotation** | LOW | HIGH | Configured dynamic `JWT_SECRET` environment variable injection in `application.properties` with safe fallback. |
| **Minor Guardian Consent Verification** | LOW | HIGH | Hardcoded disabled tracking and payment blocking for minors regardless of guardian status, preventing non-compliance during pending verification. |

---

## 22. Known Limitations & Technical Debt

1. **Ephemeral In-Memory Database:** The default local and Docker configuration runs H2 in-memory storage, which resets upon container termination. Production deployments must configure external managed PostgreSQL.
2. **Manual Transaction Entry Reliance:** Automated payment detection from bank SMS is not yet implemented, requiring users to log expenses manually or initiate payments through SpeDex quick-pay links.
3. **Single Active Trip Constraint:** A user cannot maintain two concurrent active trips simultaneously (e.g., tracking a business trip and personal weekend trip concurrently).

---

## 23. Missing & Incomplete Areas

1. **Automated On-Device SMS Bank Parsing:** Background SMS receipt parsing is proposed but not yet implemented.
2. **Shared / Multi-User Hostel Ledgers:** Group expense splitting across multiple registered SpeDex accounts is currently modeled as individual trip ledgers rather than multi-tenant collaborative sessions.

---

## 24. Remaining Tasks & Priority Backlog

### P0 — Critical (Zero Open)
- *No open P0 issues. All security, IDOR, age-gating, and data-loss checks are resolved and verified.*

### P1 — Important Correctness & Infrastructure
- **P1.1:** Configure automated PostgreSQL connection pool failover for containerized production environments.

### P2 — Meaningful Functional Improvements
- **P2.1:** Add signed PDF ledger export for completed travel trips in addition to CSV export.
- **P2.2:** Webhook triggers for external bank SMS receipt parsers.

### P3 — Polish & User Experience
- **P3.1:** High-contrast light/dark theme toggle while preserving emerald and gold currency note tokens.
- **P3.2:** Quick-action expense logging widget for Android home screen.

### P4 — Future Roadmap Considerations
- **P4.1:** Privacy-preserving on-device SMS transaction parsing.
- **P4.2:** Multi-tenant shared hostel room budgeting with sub-account roles.

---

## 25. Future Roadmap

```text
2.05.02.0 (Current)
   │  Integrated Universal Modular Architecture & Change-Isolation Governance
   ▼
2.06.00.0 (Target: Sub-Version)
   │  Jetpack Compose Android Native Feature Parity (DPDP Center & Trips)
   ▼
2.07.00.0 (Target: Sub-Version)
   │  Shared Multi-Tenant Hostel & Family Ledgers with Role Governance
   ▼
2.08.00.0 (Target: Sub-Version)
   │  Privacy-Preserving On-Device AI Financial Literacy & Spending Insights
```

---

## 26. Important Lessons Learned

1. **Server Authorization is Non-Negotiable:** Client-side flags can be intercepted or bypassed. Crucial safety boundaries (such as minor payment blocking) must always be enforced in backend services with immutable audit logging.
2. **Monoliths Create Refactoring Friction:** Decomposing `App.tsx` into domain views early prevents state tangled spaghetti and allows isolated unit testing of views.
3. **Automated Hermetic E2E Tests Save Time:** The lightweight Python mock backend suite executes 77 integration tests in under 300 milliseconds, allowing instant verification of REST contracts without booting heavy external services.
4. **Aesthetic Consistency Builds Trust:** High-frequency financial tools require visual dignity. The Indian Rupee banknote palette and refined typography give SpeDex an institutional, trustworthy feel compared to generic tech templates.
5. **Living Institutional Memory:** Maintaining synchronized version ledgers (`VERSION_CONTROLLER.md`) and project master files (`SPEDEX.md`) prevents knowledge loss across agent sessions and developer handoffs.
6. **Modular Boundaries and Change Radius Control Prevent Regressions:** Organizing the platform by domain ownership with explicit public API contracts and strict change radius calculations guarantees that targeted fixes never cause cascading breakages in unrelated modules.

---

## 27. Release & Version Information

### 27.1 Authoritative Current Version
**`2.07.00.0`** (Sub-Version: SpeDex V1 Production-Ready MVP Baseline / `v1.0.0-PROD`)

### 27.2 Agent Version Authority Governance (User Mandate)
- **Major Version (`A`) — STRICTLY FORBIDDEN WITHOUT CONSENT**:
  The agent has **ZERO AUTHORITY** to increment `A` (`A+1.00.00.0`) without explicit prior consent from the project owner. Under no circumstances may major version upgrades be performed autonomously.
- **Sub-Version (`BC`) — PARTIAL AUTHORITY**:
  The agent has **PARTIAL AUTHORITY** on `BC` (`A.BC+1.00.0`) for significant milestone releases and multi-component evolutions, keeping the user informed of proposed sub-version increments.
- **Functional (`DE`) & Minor Patch (`F`) (`c.de.f`) — FULL AUTHORITY**:
  The agent has **FULL AUTHORITY** to autonomously increment and manage `DE` functional changes (`A.BC.DE+1.0`) and `F` bug/patch fixes (`A.BC.DE.F+1`).

### 27.3 Version Lineage Summary
- **`2.07.00.0` (2026-09-30):** SpeDex V1 Production-Ready MVP Baseline (`v1.0.0-PROD`): Complete 18-step user loop, RESTful transaction and payment reference controllers, RFC 4180 CSV export, Render cloud PostgreSQL profile, Vercel SPA routing fallback, and 100% test pass rate across 159/159 automated tests.
- **`2.06.00.0` (2026-09-26):** Jetpack Compose Android Native Feature Parity (Trips Ledger & DPDP Privacy Center in Room/Jetpack Compose), complete two-file documentation consolidation (`SPEDEX.md` + `VERSION_CONTROLLER.md`), and full stack verification across 150/150 passing tests.
- **`2.05.02.0` (2026-09-20):** Codified the 76-protocol Universal Modular Architecture & Change-Isolation Governance System into `.agents/AGENTS.md`, authored root `ARCHITECTURE.md` with domain ownership maps and action-to-data traceability, and verified 150/150 passing tests.
- **`2.05.01.0` (2026-09-20):** Dynamic multi-currency travel conversion for cross-border trips, offline forex cache with 8 major international currencies, live foreign currency entry mode, secondary foreign spend analytics, and 150 passing tests across all 4 suites.
- **`2.05.00.0` (2026-09-20):** Privacy, Age Verification, Consent & Financial Safety System with server-side capability authorization, DPDP Act 2023 technical controls, and 148 passing tests.
- **`2.04.01.0` (2026-09-19):** Reconstructed historical version ledger and integrated dynamic live application version display.
- **`2.04.00.0` (2026-09-19):** Adopted Universal Version Control & Change Governance System (`A.BC.DE.F` hierarchy).
- **`2.03.01.0` (2026-09-19):** Full dashboard view modularization and component architecture refactor from monolith `App.tsx`.
- **`2.03.00.0` (2026-09-11):** Established Product Intent Recovery and 4-layer persistent memory architecture.
- **`2.02.00.0` (2026-03-01):** Full-stack DPDP Act 2023 compliance, minor protection, and privacy APIs.
- **`2.01.00.0` (2026-02-15):** Trips Ledger subsystem with 5-tier E2E testing validation suite.
- **`2.00.00.0` (2026-01-20):** SpeDex 2.0 master architecture: Spring Boot 3, React 19, Expo React Native, and native Android bridge.
- **`1.04.00.0` (2025-12-28):** Weekly spending calculation with date filtering and transaction search optimization.
- **`1.03.00.0` (2025-12-15):** Hardened security with CorsFilter, GlobalExceptionHandler, and testing validation suite.
- **`1.02.00.0` (2025-12-01):** Rewrote backend to Java Spring Boot with Spring Security, JWT auth, and Dockerfile.
- **`1.01.00.0` (2025-11-20):** Transitioned to production with UPI QR payment preparation and campus wallet persona.
- **`1.00.00.0` (2025-11-10):** Initial SpeDex high-frequency hostel wallet prototype repository baseline.

---

## 28. Final System Summary & Onboarding Guide

For any new developer or AI assistant joining the SpeDex project:

1. **Understand the Identity:** SpeDex is an expense ledger, merchant directory, and travel budgeting tool for young Indian transactors. It is **not** a bank or payment processor.
2. **Understand the Age Boundary:** Adults (18+) have full transactional capability. Minors (<18) operate strictly in **Financial Learning / Journal Mode** (manual logging and budgeting active; live payment initiation blocked server-side).
3. **Inspect the Version Controller First:** Consult [`VERSION_CONTROLLER.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/VERSION_CONTROLLER.md) before making any modifications. Classify changes under `A.BC.DE.F`.
4. **Verify the 4 Suites:** Before committing, ensure:
   - Backend: `./mvnw.cmd test` (57 tests)
   - Dashboard: `npm test -- --run` & `npm run build` (9 tests, 0 build errors)
   - Mobile: `npm test` & `npx tsc --noEmit` (7 tests, 0 type errors)
   - E2E: `python test-trips-e2e.py` (77 tests)
   - Total: **150/150 tests must pass**.
5. **Keep the Memory Living:** Whenever a feature, bug fix, or architecture change is made, update `SPEDEX.md` and `VERSION_CONTROLLER.md`. If prompted with `FUP` (Full Update Project), execute a complete synchronization across these authoritative records.
