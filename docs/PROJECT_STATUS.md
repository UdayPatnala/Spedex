# SpeDex — Project Status & Component Health Matrix

## 1. Executive Summary

- **Current Version**: 2.04.00.0 (Universal Version Control & Change Governance System)
- **Active Release Branch**: `main`
- **Overall System Status**: **HEALTHY / 100% VERIFIED**
- **Last Verification Date**: 2026-09-19
- **Operating Framework**: Universal Product Reverse-Engineering + Universal Version Control & Change Governance System (35-protocol release-control specification)

---

## 2. Three-Layer Memory Synchronization

| Memory Layer | Key Artifacts | Synchronization Status | Notes |
| :--- | :--- | :--- | :--- |
| **Layer 1: Product Memory** | Vision, aesthetic rules, DPDP legal constraints | **SYNCHRONIZED** | INR currency note theme, non-custodial fintech bounds preserved |
| **Layer 2: Engineering Memory** | `VERSION_HISTORY.md`, `EXECUTION_HISTORY.md`, `ARCHITECTURE.md`, `PROJECT_STATUS.md` | **SYNCHRONIZED** | Full audit logs, architectural boundaries, and status records active |
| **Layer 3: Actual State** | Git commits, source code, build configs, dependencies | **SYNCHRONIZED** | Clean working tree on `main`, all 4 verification test suites 100% passing |

---

## 3. Component Health Matrix

| Subsystem | Tech Stack | Unit Tests | Build / Typecheck | Health State |
| :--- | :--- | :--- | :--- | :--- |
| **Backend API** | Spring Boot 3.x / Java 17 | 49 / 49 PASS | Maven compile clean | **OPERATIONAL** |
| **Dashboard App** | React 19 / Vite 6 / TS 5.8 | 9 / 9 PASS | `vite build` clean (0 errors) | **OPERATIONAL** |
| **Mobile App** | Expo SDK 53 / React Native 0.79 | 7 / 7 PASS | `tsc --noEmit` clean (0 errors) | **OPERATIONAL** |
| **E2E Test Suite** | Python 3.x / Mock Backend | 77 / 77 PASS | Clean exit code 0 | **OPERATIONAL** |
| **Android Native** | Kotlin / Jetpack Compose | Baseline | Gradle ready | **MAINTAINED** |

---

## 4. Active Feature Matrix

1. **High-Frequency Expense Tracking**:
   - Instant categorization, cash vs digital tag, payment velocity analytics.
2. **Trips Ledger Subsystem**:
   - Live travel sessions, automatic closure of overlapping active trips, category-based distributions, cash/online splits.
3. **DPDP Act 2023 Compliance**:
   - Minor protection flow (<18) with verifiable parental consent.
   - Permanent opt-out of behavioral tracking for minors.
   - Granular user consent toggles (functional, analytics, marketing).
   - Machine-readable JSON personal data export (`GET /api/privacy/export`).
   - Irreversible account anonymization and erasure (`POST /api/privacy/erase`).
   - Grievance redressal ticketing with SLA monitoring.
4. **SEO & AI Discoverability**:
   - Valid `robots.txt`, `sitemap.xml`, `llms.txt`, Open Graph, and JSON-LD structured data.

---

## 5. Audit Backlog & Issue Classification (P0 - P4)

- **P0 (Critical / Security / Data Loss)**:
  - *None observed.* IDOR checks active on all routes (`403 Forbidden`). Zero credential storage invariant respected.
- **P1 (Important / Correctness / UX / Performance)**:
  - *None observed.* All 142 total tests across all layers passing.
- **P2 (Meaningful Improvement)**:
  - Dynamic multi-currency travel conversion for cross-border trips (offline forex cache).
  - Webhook triggers for external bank SMS receipt parsers.
- **P3 (Polish)**:
  - Visual theme selector: Light/Dark mode contrast toggle preserving INR emerald & gold palette.
  - Export trips ledger as signed PDF in addition to CSV.
- **P4 (Future Considerations)**:
  - Background SQLite sync workers in `mobile_native_android` using WorkManager.
  - Multi-tenant family wallet budgeting with granular sub-account permissions.

---

## 6. Risk Register & Mitigations

| Risk Factor | Probability | Impact | Mitigation Strategy |
| :--- | :--- | :--- | :--- |
| **Ephemeral H2 Database on Render** | HIGH | MEDIUM | Local disk resets on free-tier sleep cycles. Addressed by documentation to plug in managed PostgreSQL (Supabase / Neon / Render Postgres) for production deployments. |
| **JWT Key Rotation** | LOW | HIGH | Configured dynamic `JWT_SECRET` environment variable injection in `application.properties` with safe fallback. |
| **Minor Guardian Consent Verification** | LOW | HIGH | Hardcoded disabled tracking for minors regardless of guardian status, preventing non-compliance during pending verification. |
