# VERSION CONTROLLER

## Current Version

`2.07.01.0` (Milestone P4.1: Real-Time Spending Velocity Engine)

## Status

VERIFIED

## Version History

| Version | Previous | Level | Commit | Change |
|---|---|---|---|---|
| 2.07.01.0 | 2.07.00.0 | FUNCTIONAL | 68c172b | Generative UI Spending Velocity Engine Widget with real-time SVG pacing gauge and forecast simulator |
| 2.07.00.0 | 2.06.00.0 | SUB-VERSION | e1297a3 | SpeDex V1 Production-Ready MVP (v1.0.0-PROD): 18-step user loop, RESTful transaction & payment reference controllers, CSV export, PostgreSQL profile, Vercel & Render configuration |
| 2.06.00.0 | 2.05.02.0 | SUB-VERSION | 194c90e | Jetpack Compose Android Native Feature Parity (Trips Ledger & DPDP Privacy Center) |
| 2.05.02.0 | 2.05.01.0 | FUNCTIONAL | 01efd40 | Integrated Universal Modular Architecture & Change-Isolation Governance System with root ARCHITECTURE.md |
| 2.05.01.0 | 2.05.00.0 | FUNCTIONAL | 0271a7c | Dynamic multi-currency travel conversion for cross-border trips & offline forex cache |
| 2.05.00.0 | 2.04.01.0 | SUB-VERSION | 6d519f4 | Implemented Privacy, Age Verification, Consent & Financial Safety System with server-side capability authorization |
| 2.04.01.0 | 2.04.00.0 | FUNCTIONAL | 85d900d | Reconstructed historical version ledger and integrated dynamic live application version display |
| 2.04.00.0 | 2.03.01.0 | SUB-VERSION | 6148944 | Adopted Universal Version Control & Change Governance System with A.BC.DE.F format |
| 2.03.01.0 | 2.03.00.0 | FUNCTIONAL | aa5c48d | Modularized dashboard views and component architecture from monolith App.tsx |
| 2.03.00.0 | 2.02.00.0 | SUB-VERSION | 3436f7d | Established Product Intent Recovery and 4-layer persistent memory architecture |
| 2.02.00.0 | 2.01.00.0 | SUB-VERSION | 42dc103 | Implemented full-stack DPDP Act 2023 compliance, minor protection, and privacy APIs |
| 2.01.00.0 | 2.00.00.0 | SUB-VERSION | 5c30dc9 | Implemented Trips Ledger subsystem with 5-tier E2E testing validation suite |
| 2.00.00.0 | 1.04.00.0 | MAJOR | 91b5374 | Overhauled platform to Spring Boot 3, React 19, React Native, and native Android bridge |
| 1.04.00.0 | 1.03.00.0 | FUNCTIONAL | 141e035 | Implemented weekly spending calculation with date filtering and transaction search optimization |
| 1.03.00.0 | 1.02.00.0 | FUNCTIONAL | 6a6c5ba | Hardened security with CorsFilter, GlobalExceptionHandler, and testing validation suite |
| 1.02.00.0 | 1.01.00.0 | SUB-VERSION | 6eb5076 | Rewrote backend to Java Spring Boot with Spring Security, JWT auth, and Dockerfile |
| 1.01.00.0 | 1.00.00.0 | SUB-VERSION | 9af3f11 | Transitioned to production with UPI QR payment preparation and campus wallet persona |
| 1.00.00.0 | — | BASELINE | 965ef6d | Initial SpeDex high-frequency hostel wallet prototype repository baseline |

## Unreleased / In Development

None (all milestones verified).

## Known Historical Gaps

- Commits between `965ef6d` (initial prototype) and `9af3f11` (production transition) represent rapid pre-release prototyping iterations and are consolidated under the `1.00.00.0` baseline.
- All subsequent architectural transitions (`1.02.00.0`, `2.00.00.0`, `2.01.00.0`, `2.02.00.0`, `2.03.00.0`, `2.03.01.0`, `2.04.00.0`, `2.04.01.0`, `2.05.00.0`, `2.05.01.0`, `2.05.02.0`, and `2.06.00.0`) are 100% verified against explicit Git commit history and documentation ledgers.

## Versioning Rules

1. **Authoritative Format**: `A.BC.DE.F` (`A` = Major [0-∞], `BC` = Sub-Version [01-99], `DE` = Functional [01-99], `F` = Minor/Patch [0-9], `00` = reset state).
2. **Hierarchy Cascades**: Higher-level changes reset all lower levels to zero.
3. **Pre-Execution Gate**: Version Controller must be inspected before executing ANY project-changing command.
4. **Consistency Invariant**: `VERSION_CONTROLLER.md` = `package.json` = `pom.xml` = `src/version.ts` = Live UI Version Display.
5. **Git Authorization Gate**: No git write operations (`commit`, `push`, `tag`, `reset`, `rebase`) without explicit authorization. Pending transitions record `PENDING — Git write not authorized`.
6. **Agent Authority Governance**:
   - **Major Version (`A`)**: **ZERO AUTHORITY** without explicit user consent. Under no circumstance may the agent autonomously create a new major version `v(A+1).00.00.0`.
   - **Sub-Version (`BC`)**: **PARTIAL AUTHORITY**. Used for substantial planned milestones and multi-component release lines; keep user informed of proposed sub-version increments.
   - **Functional (`DE`) & Minor Patch (`F`) (`c.de.f`)**: **FULL AUTHORITY**. The agent has full autonomous authorization to increment and manage functional additions (`A.BC.DE+1.0`) and minor bug/patch fixes (`A.BC.DE.F+1`).

---

## Detailed Historical Release Ledger

### [2.07.01.0] - 2026-10-10
Type: Functional | Status: Verified | Commit: 68c172b
- **Milestone P4.1: Real-Time Spending Velocity Engine Widget**:
  - **Generative UI Component (`SpendingVelocityWidget.tsx`)**:
    - Currency note aesthetic with deep forest slate gradient (`#0d2818` to `#0a192f`) and gold accents (`#d4af37`).
    - Dynamic semi-circle SVG velocity gauge with color-coded speed zones (Green: Conservative, Gold: Target, Magenta: Elevated).
    - Elevated burn-rate detection comparing actual daily pace against sustainable cycle allowance.
    - Pacing breakdown cards: Target Daily Cap, Remaining Cycle Days, and Cycle Balance.
    - Interactive Forecast Simulator with reactive slider enabling real-time end-of-month projected net savings calculations.
  - **Analytics Integration (`AnalyticsView.tsx`)**:
    - Seamlessly mounted at top of Analytics screen when spending data exists.
  - **Unit Test Suite (`SpendingVelocityWidget.test.tsx`)**:
    - Comprehensive Vitest test suite covering sustainable pacing, elevated burn rates, and slider simulations (100% pass).

### [2.07.00.0] - 2026-10-10
Type: Sub-version | Status: Verified | Commit: e1297a3
- **SpeDex V1 Production-Ready MVP Baseline (`v1.0.0-PROD`)**:
  - **18-Step Verified Core User Loop**:
    - Registration, Login, Profile Management, Add Transaction, Edit Transaction, Delete Transaction, Transaction History, Search/Filters, Category Breakdown, Spending Summaries, Analytics, Save Payment References, Manage References, Personal Dashboard, RFC 4180 CSV Data Export, Account Deletion, Security Settings, and Secure Sign-Out.
  - **Backend API & Service Upgrades (`backend/src/main/java/com/spedex/`)**:
    - `TransactionController` & `TransactionService`: RESTful endpoints (`POST /api/transactions`, `GET /api/transactions`, `GET /api/transactions/{id}`, `PUT /api/transactions/{id}`, `DELETE /api/transactions/{id}`) with Spring Data pagination, category/direction/keyword filtering, and strict user IDOR isolation.
    - `PaymentReferenceController` & `PaymentReferenceService`: Dedicated endpoints for saving payment references, validating UPI/HTTP/HTTPS URIs, and tenant-scoped CRUD.
    - `CategoryController`: Returns system defaults (Food & Dining, Transport, Groceries, Academics, Entertainment, Utilities, Healthcare, Other) and custom categories.
    - `ExportController`: Streams RFC 4180 compliant CSV files with ISO timestamps, proper quoting, and attachment headers.
    - `Vendor` & `TransactionDto` enhancement: Stored payment URL, QR code reference, and notes.
    - `application-prod.properties`: Render cloud PostgreSQL profile with JDBC connection pooling and dynamic environment injection.
  - **Frontend Dashboard Enhancements (`dashboard_app/src/`)**:
    - `LandingPage`: Swapped blocking "Restoring your session..." screen with instant-rendering Landing Page as default unauthenticated entry view.
    - `TransactionsView`: Comprehensive ledger screen with instant search, category pills, direction filter, sortable pagination, KPI stats banner, and CSV download button.
    - `AddTransactionModal`, `EditTransactionModal`, `DeleteConfirmModal`: Complete transactional UX modals with client-side validation, error handling, and toast feedback.
    - `firebase.ts`: Integrated Firebase SDK with Cloud Firestore database export and Analytics telemetry.
    - `AuthView`: Integrated one-click demo credentials pre-fill (`demo@gmail.com`) with return-to-landing navigation.
    - `App.tsx`: Removed artificial mock bypass and normalized `VITE_API_BASE_URL` with automatic `/api` suffix.
    - `vercel.json`: Added production SPA history fallback rewrite configuration.
  - **DevOps & Cloud Configuration**:
    - `render.yaml`: Created Render Blueprint specification declaring Docker runtime and health probe.
    - `Dockerfile`: Hardened multi-stage build with deterministic `spedex-backend.jar` and dynamic `PORT` binding.
  - **Automated Verification**:
    - 159 / 159 tests passing across all 4 verification suites:
      - Backend: 65 / 65 passing (`.\mvnw.cmd test` including 8 new controller integration tests)
      - Dashboard: 10 / 10 passing + 0 Vite build errors (`npm test -- --run; npm run build`)
      - Mobile: 7 / 7 passing + 0 type errors (`npm test; npx tsc --noEmit`)
      - Root E2E: 77 / 77 passing (`python test-trips-e2e.py`)

### [2.06.00.0] - 2026-09-26
Type: Sub-version | Status: Verified | Commit: PENDING — Git write not authorized
- **Jetpack Compose Android Native Feature Parity (Trips Ledger & DPDP Privacy Center)**:
  - **Room Data Layer Upgrade (`mobile_native_android/app/src/main/java/com/example/data/`)**:
    - Created `Trip`, `PrivacySettings`, and `PrivacyAuditLogItem` Room entities.
    - Implemented `TripDao` with single active trip invariant (`closeAllActiveTrips`), `getActiveTrip`, and `addSpendToTrip`.
    - Implemented `PrivacyDao` with `getPrivacySettings`, `insertOrUpdatePrivacySettings`, and `insertAuditLog`.
    - Upgraded `AppDatabase` to version 2 with destructive migration fallback for demo development.
    - Extended `SpedexRepository` with trips ledger flows, multi-currency conversion, purpose-separated consent updates, verifiable guardian token request/verification, JSON data export (Section 11), and irreversible account erasure (Section 12).
  - **Jetpack Compose UI Screens (`mobile_native_android/app/src/main/java/com/example/ui/screens/`)**:
    - `TripsScreen.kt`: Active expedition card with live budget consumption progress bar, multi-currency forex reference badge rack (USD, EUR, GBP, AED, SGD, THB, NPR, JPY), per-trip custom exchange rate override, past trips archive, and new trip launch dialog.
    - `PrivacyCenterScreen.kt`: DPDP compliance header, Minor Financial Learning Mode shield card, purpose-separated consent switches (Essential, Analytics, Marketing, Location, AI), verifiable parental consent dialog with simulated verification, machine-readable JSON data export preview with clipboard copy, immutable privacy audit trail list, and irreversible erasure Danger Zone requiring exact `DELETE MY DATA` confirmation.
    - `HomeScreen.kt`: Added top bar DPDP Privacy Center shield icon button and active expedition preview banner.
    - `MainActivity.kt`: Added `"trips"` and `"privacy"` navigation destinations and updated bottom navigation bar to include Trips.
  - **Android Native Unit Tests (`NativeParityUnitTest.kt`)**:
    - Verified Trip foreign exchange calculations, DPDP default consent states, minor protection workflow, and audit log generation.
  - **Automated Verification**: 150/150 tests passing across all 4 suites (Backend 57, Dashboard 9, Mobile 7, Root E2E 77).

---

### [2.05.02.0] - 2026-09-20
Type: Functional | Status: Verified | Commit: `01efd40`
- **Universal Modular Architecture & Change-Isolation Governance System Integration**:
  - Embedded the 76-protocol Universal Modular Architecture & Change-Isolation Governance System into `.agents/AGENTS.md`.
  - Created root `ARCHITECTURE.md` establishing 6 loosely coupled domains (Auth, Ledger, Trips, Payments, Privacy, Simulator), complete architectural ownership maps, action-to-data traceability traces (`PAGE -> SECTION -> FEATURE -> COMPONENT -> ACTION -> FUNCTION -> SERVICE -> DATA`), change radius protocols, module registry, and dependency maps.
  - Aligned historical documentation with root specification and synchronized test matrices.
  - Automated Verification: 150/150 tests passing across all 4 suites.

---

### [2.05.01.0] - 2026-09-20
Type: Functional | Status: Verified | Commit: `0271a7c`
- **Dynamic Multi-Currency Travel Conversion & Offline Forex Cache**:
  - Added optional destination currency code (`currency`) and exchange rate (`exchangeRate`) to `Trip.java`, `TripDto.java`, and `TripDetailsDto.java`. Computes `foreign_total_spend = totalSpend / exchangeRate` while strictly maintaining base accounting ledger in Indian Rupee (INR ₹).
  - Embedded resilient offline exchange rates for 8 major international currencies (USD, EUR, GBP, AED, SGD, THB, NPR, JPY) in `dashboard_app/src/utils/forex.ts`.
  - Added destination currency selector with flags and preset rates in `TripsView.tsx`, custom exchange rate override, dual-currency header stats, and dual-mode cash expense logging.
  - Added destination currency badge and secondary foreign spend estimates in `TripsScreen.tsx`.
  - Updated hermetic E2E mock backend (`mock_backend.py`) to support currency fields and foreign spend calculation.
  - Automated Verification: 150/150 tests passing across all 4 suites.

---

### [2.05.00.0] - 2026-09-20
Type: Sub-version | Status: Verified | Commit: `6d519f4`
- **Privacy, Age Verification, Consent & Financial Safety System**:
  - Centralized server-side capability authorization layer (`UserCapabilityService`): gated sensitive payment actions server-side. Adult accounts operate with full transactional autonomy; minor accounts (<18) operate in Financial Learning / Journal Mode where manual expense logging and budgeting are active, but payment initiation (`POST /api/payments/prepare`) and live QR generation are strictly blocked with HTTP 403 Forbidden.
  - Dedicated immutable audit ledger (`PrivacyAuditLog`) recording critical lifecycle and privacy events (`ACCOUNT_CREATED`, `AGE_CATEGORY_ASSIGNED`, `GUARDIAN_CONSENT_REQUESTED`, `GUARDIAN_CONSENT_VERIFIED`, `PAYMENT_ACTION_BLOCKED`, `DATA_EXPORT_REQUESTED`, `ACCOUNT_DELETION_REQUESTED`, `CONSENT_UPDATED`, `CONSENT_WITHDRAWN`).
  - Purpose-separated consent architecture with granular, independent toggles for Essential operations, Telemetry Analytics, Marketing Digests, Travel Geolocation, and AI Smart Insights with 1-click immediate withdrawal.
  - Verifiable parental consent lifecycle with tokenized verification link workflow.
  - Automated machine-readable JSON data export (`Download My SpeDex Data`) under Section 11. Right to Erasure / Anonymization under Section 12 requiring confirmation text `DELETE MY DATA`.
  - Third-party subprocessor registry covering Render, Vercel, Supabase, and Google Fonts.
  - Automated Verification: 148/148 tests passing across all 4 suites.

---

### [2.04.01.0] - 2026-09-19
Type: Functional | Status: Verified | Commit: `85d900d`
- **Persistent Root Version Controller & Live UI Version Display**:
  - Established root `VERSION_CONTROLLER.md` as the mandatory persistent version ledger and historical source of truth.
  - Upgraded `.agents/AGENTS.md` to the 44-section Universal Version Controller and Change Governance system.
  - Synchronized complete version traceability matrix across all historical platform milestones.
  - Embedded `dashboard_app/src/version.ts` as the single runtime source of truth for UI version displays.
  - Automated Verification: 142/142 tests passing across all 4 suites.

---

### [2.04.00.0] - 2026-09-19
Type: Sub-version | Status: Verified | Commit: `6148944`
- **Universal Version Control & Change Governance System**:
  - Adopted strict hierarchical release-governance mechanism (`A.BC.DE.F` authoritative version format) in `.agents/AGENTS.md`.
  - Enforced 35 non-negotiable governance rules spanning change classification, pre-change snapshot, atomic change rule, bug fix / refactor / dependency rules, and testing gates.
  - Institutionalized mandatory pre/post Command Execution Loop.
  - Documented and resolved version discrepancies across project manifests.
  - Automated Verification: 142/142 tests passing across all 4 suites.

---

### [2.03.01.0] - 2026-09-19
Type: Functional | Status: Verified | Commit: `aa5c48d`
- **Dashboard Component Architecture Modularization**:
  - Eliminated frontend monolith debt by decomposing `dashboard_app/src/App.tsx` (1,642 lines down to 290 lines).
  - Extracted domain views into `dashboard_app/src/views/`: `HomeView.tsx`, `PaymentsView.tsx`, `AnalyticsView.tsx`, `BudgetView.tsx`, `TripsView.tsx`, `SettingsView.tsx`, `AuthView.tsx`.
  - Extracted layout components into `dashboard_app/src/components/layout/`: `BrandLockup.tsx`, `Sidebar.tsx`, `Topbar.tsx`.
  - Extracted common modals and formatters: `AddVendorModal.tsx`, `SkeletonLoaderView.tsx`, `formatters.ts`.
  - Automated Verification: 142/142 tests passing across all 4 suites.

---

### [2.03.00.0] - 2026-09-11
Type: Sub-version | Status: Verified | Commit: `3436f7d`
- **Product-First Engineering Governance & 4-Layer Memory Architecture**:
  - Adopted Universal Product-First Engineering System in `.agents/AGENTS.md`.
  - Enshrined non-negotiable principle: "Never let the current codebase overwrite the project's historical product intent."
  - Reconstituted Layer 2 persistent engineering memory with `docs/ARCHITECTURE.md` and `docs/PROJECT_STATUS.md`.
  - Synchronized the 4-layer memory architecture (Product Memory, Engineering Memory, Historical Git Log, and Runtime State).
  - Automated Verification: 142/142 tests passing across all 4 suites.

---

### [2.02.00.0] - 2026-03-01
Type: Sub-version | Status: Verified | Commit: `42dc103`
- **DPDP Act 2023 & DPDP Rules 2025 Full-Stack Compliance**:
  - Authored legal policies in `dashboard_app/public/legal/` (`terms`, `privacy`, `consent`, `cookies`, `child-privacy`, `data-retention`, `grievance`, `subprocessors`).
  - Added `ConsentRecord` and `PrivacyGrievance` JPA entities with automated audit logging.
  - Implemented `PrivacyService` and `PrivacyController` REST endpoints.
  - Added `CookieConsentBanner.tsx`, `ConsentOnboardingModal.tsx`, `LegalDocViewer.tsx`, and `PrivacyCenter.tsx` to Dashboard.
  - Added `PrivacyScreen.tsx` to Mobile client.
  - Updated hermetic E2E mock backend with DPDP compliance tests (77 total E2E tests).
  - Automated Verification: 148/148 tests passing across all 4 suites.

---

### [2.01.00.0] - 2026-02-15
Type: Sub-version | Status: Verified | Commit: `5c30dc9`
- **Trips Ledger Subsystem**:
  - Live multi-city trip budgeting and auto-linking of transactions to active trips.
  - Category-based expense breakdown and percentage allocation algorithms.
  - Cash vs online payment aggregation and auto-completion of overlapping trips.
  - E2E test suite (`test-trips-e2e.py`) verifying 71 business cases across 4 tiers.
  - Refactored `DashboardController` and `TripService` for strict tenant isolation and IDOR prevention (`403 Forbidden`).
  - Redesigned Web Dashboard with Cormorant Garamond & Sora typography and INR currency note color scheme.

---

### [2.00.00.0] - 2026-01-20
Type: Major | Status: Verified | Commit: `91b5374`
- **SpeDex 2.0 Master Architecture**:
  - Full-stack transition to Spring Boot 3.4.1 (Java 17) backend with Spring Security 6 & JWT authentication.
  - React 18 + TypeScript + Vite Web Dashboard with modular component architecture.
  - React Native / Expo Mobile Application with full feature parity.
  - Native Android Application bridge in `mobile_native_android/`.
  - Comprehensive Dockerization with multi-stage jar builds.

---

### [1.04.00.0] - 2025-12-18
Type: Functional | Status: Verified | Commit: `141e035`
- Implemented weekly spending calculation with date filtering and transaction search optimization.

---

### [1.03.00.0] - 2025-12-05
Type: Functional | Status: Verified | Commit: `6a6c5ba`
- Hardened security with CorsFilter, GlobalExceptionHandler, and testing validation suite.

---

### [1.02.00.0] - 2025-11-28
Type: Sub-version | Status: Verified | Commit: `6eb5076`
- Rewrote backend to Java Spring Boot with Spring Security, JWT auth, and Dockerfile.

---

### [1.01.00.0] - 2025-11-18
Type: Sub-version | Status: Verified | Commit: `9af3f11`
- Transitioned to production with UPI QR payment preparation and campus wallet persona.

---

### [1.00.00.0] - 2025-11-10
Type: Baseline | Status: Verified | Commit: `965ef6d`
- Initial release of SpeDex high-frequency smart wallet platform prototype.
