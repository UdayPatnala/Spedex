# SpeDex — System Architecture Specification

## 1. System Overview & Architectural Topology

SpeDex (**Speed Index + Spending Index**) is an enterprise-grade financial intelligence utility, automated travel expense ledger, and non-custodial smart wallet platform.

The system is built on a multi-tier client-server architecture with strict domain boundaries, tenant isolation, and regulatory compliance.

```text
                      +------------------------------------------+
                      |         CLIENT APPLICATION LAYER         |
                      |  - Web Dashboard (React 19 / Vite 6)     |
                      |  - Mobile App (Expo SDK 53 / RN 0.79)    |
                      |  - Android Native (Kotlin Compose / Room)|
                      +--------------------+---------------------+
                                           |
                                   HTTPS / REST API
                                   Bearer JWT Auth
                                           |
                                           v
                      +--------------------+---------------------+
                      |         BACKEND APPLICATION LAYER        |
                      |            (Spring Boot 3.x)             |
                      |  +------------------------------------+  |
                      |  | Security & Auth (JWT, BCrypt)      |  |
                      |  +------------------------------------+  |
                      |  | Controllers (REST API Endpoints)   |  |
                      |  +------------------------------------+  |
                      |  | Services (Business & IDOR Checks)  |  |
                      |  +------------------------------------+  |
                      |  | Repositories (Spring Data JPA)     |  |
                      |  +------------------------------------+  |
                      |  | DPDP Compliance & Privacy Engine   |  |
                      |  +------------------------------------+  |
                      +--------------------+---------------------+
                                           |
                                      JPA / JDBC
                                           |
                                           v
                      +--------------------+---------------------+
                      |          DATA PERSISTENCE LAYER          |
                      |  - H2 Database (File-based local)        |
                      |  - PostgreSQL Support (Production)       |
                      |  - Rolling 180-day retention purge       |
                      +------------------------------------------+
```

---

## 2. Layered Architectural Boundaries

### 2.1 Backend Application Layer (ackend/)
- **Runtime**: Java 17, Spring Boot 3.x.
- **Pattern**: Strict Controller -> Service -> Repository separation.
- **Access Control & IDOR**:
  - Every service method accepts an authenticated User context.
  - Queries must explicitly constrain records by user_id.
  - IDOR violations immediately trigger AccessDeniedException mapping to 403 Forbidden.
- **Security Invariants**:
  - Zero storage of banking passwords, UPI MPINs, or debit/credit card credentials.
  - Password hashing via BCryptPasswordEncoder.
  - Stateless JWT token authentication with expiration (86400000 ms / 24h).

### 2.2 Web Dashboard Application (dashboard_app/)
- **Runtime**: React 19, TypeScript 5.8, Vite 6.
- **Styling Architecture**: Modular CSS with INR currency note design tokens, glassmorphism containers, and high-contrast typography hierarchy (Cormorant Garamond for headers, Sora for data/body).
- **Core Modules**:
  - Overview: Live financial metrics, recent transactions, spending velocity.
  - Trips Ledger: Session-based travel budgeting, expense linking, cash vs digital aggregation.
  - Vendors: Frequently used UPI payees and routing presets.
  - Analytics: Category breakdown, day-of-week trends, velocity gauges.
  - Privacy Center: DPDP Act 2023 consent management, data export, grievance filing, account erasure.
  - Mobile Sync Simulator: Dynamic QR code generator and push sync simulator.

### 2.3 Mobile Applications Layer
- **Cross-Platform App (mobile/)**:
  - Expo SDK 53, React Native 0.79, React Navigation 7.
  - Full feature parity with Web Dashboard: Authentication, Dashboard Overview, Trips Management, Vendor Directory, Payment Confirmation, and Privacy Center.
  - Offline-first storage using @react-native-async-storage/async-storage.
- **Native Android (mobile_native_android/)**:
  - Modern Kotlin Jetpack Compose interface with Material 3 theming.
  - Local caching via Android Room SQLite persistence.

---

## 3. Regulatory & Privacy Compliance Subsystem (DPDP Act 2023)

SpeDex incorporates a complete full-stack compliance engine adhering to the **Digital Personal Data Protection Act, 2023** and **DPDP Rules 2025**:

1. **Notice & Consent (Section 5 & 6)**:
   - Granular opt-in/opt-out for functional, analytics, and marketing data processing.
   - Immutable audit trail via ConsentRecord entity.
2. **Minor Protection (Section 9)**:
   - Users under 18 require verifiable parental/guardian consent before registration.
   - Behavioral tracking, profiling, and targeted advertising are permanently disabled for minors.
3. **Right to Access (Section 11)**:
   - GET /api/privacy/export provides machine-readable (JSON) export of all user personal and financial data.
4. **Right to Erasure (Section 12)**:
   - POST /api/privacy/erase executes immediate anonymization, invalidates JWT tokens, and schedules secondary cleanup.
5. **Grievance Redressal (Section 13)**:
   - PrivacyGrievance ticketing system with automated SLA tracking (48h acknowledgement, 15-day resolution) and escalation path to the Data Protection Board of India (DPBI).

---

## 4. Aesthetic & Design System Invariants

- **Primary Colors**: Inspired by the Indian Rupee banknotes:
  - Deep Emerald Note Green (#0d2818, #164223)
  - Accent Gold (#d4af37, #f3e5ab)
  - Warm Sand Base (#fdfbf7, #f4efe6)
- **Typography**:
  - Display / Headings: Cormorant Garamond, serif (classic elegance, currency heritage).
  - Body / Numeric / Controls: Sora, sans-serif (crisp readability for high-frequency financial data).
- **Anti-Vibe-Code Rules**:
  - Zero dead buttons, placeholder copy, or unlinked controls.
  - Every transaction display must reflect real calculated balances and currency symbols (₹).

---

## 5. Verification & Testing Matrix

The platform enforces a mandatory 4-part verification standard:
1. **Backend**: `./mvnw.cmd test` — 57/57 unit/integration tests passing.
2. **Dashboard**: `npm test -- --run` & `npm run build` — 9/9 tests passing, 0 TypeScript/build errors.
3. **Mobile**: `npm test` & `npx tsc --noEmit` — 7/7 tests passing, 0 TypeScript errors.
4. **End-to-End**: `python test-trips-e2e.py` — 77/77 tests passing.
**Total Automated Verification**: **150 / 150 tests passing (100% pass rate)**.

---

## 6. Authoritative Modular Governance

For the complete, exhaustive **Universal Modular Architecture & Change-Isolation Governance System**, domain ownership maps, traceability traces, change radius protocols, module registry, and dependency maps, consult the root authoritative specification:
[`ARCHITECTURE.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/ARCHITECTURE.md) and [`.agents/AGENTS.md`](file:///d:/PROJECT/AROH%20Open%20Source/Products/Spedex/.agents/AGENTS.md).

