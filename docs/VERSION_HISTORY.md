# SpeDex — Version History & Release Changelog

All notable changes, architectural milestones, feature additions, security enhancements, and compliance implementations for the SpeDex platform are documented in this file.

The project operates under the **Universal Version Control & Change Governance System** codified in `.agents/AGENTS.md`.
Authoritative Version Format: `A.BC.DE.F` (`A` = Major Version, `BC` = Sub-Version / Release Line [01-99], `DE` = Functional Change [01-99], `F` = Minor Fix / Patch [0-9], `00` = reset state).

---

## [2.04.00.0] - 2026-09-19
Type: Sub-version
Status: Verified

### Added
- **Universal Version Control & Change Governance System**:
  - Adopted strict hierarchical release-governance mechanism (`A.BC.DE.F` authoritative version format) in `.agents/AGENTS.md`.
  - Enforced 35 non-negotiable governance rules spanning change classification, pre-change snapshot, atomic change rule, bug fix / refactor / dependency rules, and testing gates.
  - Institutionalized mandatory pre/post Command Execution Loop: Understand Intent $\rightarrow$ Reverse-Engineer Impact $\rightarrow$ Classify Change $\rightarrow$ Calculate Target Version $\rightarrow$ Check Risks/Dependencies $\rightarrow$ Implement $\rightarrow$ Test/Verify $\rightarrow$ Update Version Metadata $\rightarrow$ Update Changelog $\rightarrow$ Report Final State.
  - Documented and resolved version discrepancies across project manifests against authoritative documentation.
  - Preserved 100% verification across all 4 suites (142/142 tests passing).

---

## [2.03.01.0] (v2.3.1) - 2026-09-19
Type: Functional
Status: Verified

### Changed
- **Dashboard Component Architecture Modularization**:
  - Eliminated frontend monolith debt by decomposing `dashboard_app/src/App.tsx` (1,642 lines down to 290 lines).
  - Extracted domain views into `dashboard_app/src/views/`:
    - `HomeView.tsx` (metrics, velocity banner, vendor breakdown, recent expenses)
    - `PaymentsView.tsx` (payment log, velocity stats, search/category filtering)
    - `AnalyticsView.tsx` (category breakdown, monthly distributions, spending trends)
    - `BudgetView.tsx` (budget allocations, category progress, alert thresholds)
    - `TripsView.tsx` (active session tracking, multi-agent expense distribution, settlement ledger)
    - `SettingsView.tsx` (preference panels, DPDP Privacy Center integration)
    - `AuthView.tsx` (sign-in/sign-up forms with minor detection and parental consent modal)
  - Extracted layout components into `dashboard_app/src/components/layout/`:
    - `BrandLockup.tsx` (currency note lockup with seal motif)
    - `Sidebar.tsx` (navigation sidebar with responsive collapsible states)
    - `Topbar.tsx` (header bar with dynamic tab titling and quick-action triggers)
  - Extracted common modals and utilities:
    - `dashboard_app/src/components/vendors/AddVendorModal.tsx` (vendor modal)
    - `dashboard_app/src/components/common/SkeletonLoaderView.tsx` (INR note loading skeleton)
    - `dashboard_app/src/utils/formatters.ts` (centralized currency and numeric formatting)
  - Maintained 100% test pass rate across all 4 suites (142/142 tests passing).

---

## [2.03.00.0] (v2.3.0) - 2026-09-11
Type: Sub-version
Status: Verified

### Added
- **Product-First Engineering Governance & 4-Layer Memory Architecture**:
  - Adopted the Universal Product-First Engineering System in `.agents/AGENTS.md`.
  - Enshrined the non-negotiable principle: *"Never let the current codebase overwrite the project's historical product intent. Reconcile the code with the intent."*
  - Institutionalized Product Archaeology: Conversations $\rightarrow$ Motive $\rightarrow$ Vision $\rightarrow$ Decisions $\rightarrow$ Product Definition (Must be, Can be, Must not be) $\rightarrow$ Gap Analysis $\rightarrow$ Decision Making $\rightarrow$ Implementation.
  - Reconstituted Layer 2 persistent engineering memory with `docs/ARCHITECTURE.md` and `docs/PROJECT_STATUS.md`.
  - Synchronized the 4-layer memory architecture (Product Memory, Engineering Memory, Historical Git Log, and Runtime State).
  - Maintained 100% verification across all 4 suites (142 tests passing).

---

## [2.02.00.0] (v2.2.0) - 2026-03-01
Type: Sub-version
Status: Verified

### Added
- **DPDP Act 2023 & DPDP Rules 2025 Full-Stack Compliance**:
  - **Legal Repository (`legal/`)**:
    - `legal/terms-of-service.md`: Complete non-custodial fintech terms, UPI routing disclaimer, zero credential storage clause, student-centric financial guidelines.
    - `legal/privacy-policy.md`: Notice of personal data processing under DPDP Act Section 4-6, data categories, rights, retention periods.
    - `legal/consent-notice.md`: Specific, informed, unconditional, and granular consent notices (DPDP Act Section 6).
    - `legal/cookie-policy.md`: Categorization of Strictly Necessary, Analytics, and Functional cookies with cookie banner management.
    - `legal/child-privacy.md`: Special protections for minors (<18 years), mandatory verifiable parental consent, strict prohibition of behavioral tracking and targeted advertising (DPDP Act Section 9).
    - `legal/data-retention-policy.md`: 180-day automated rolling purge for inactive logs and transactions; data minimization standards (DPDP Act Section 8(7)).
    - `legal/grievance-redressal.md`: Tiered grievance redressal mechanism, Data Protection Officer contact, SLA of 48h acknowledgement / 15-day resolution, escalation to Data Protection Board of India (DPBI).
    - `legal/third-party-services.md`: Comprehensive subprocessor registry including NPCI UPI rails, SMS gateways, and cloud providers.
  - **Backend Layer (`backend/`)**:
    - `ConsentRecord` and `PrivacyGrievance` JPA entities with automated audit logging.
    - `PrivacyService` & `PrivacyController` implementing:
      - `GET /api/privacy/legal/{docType}` (public access)
      - `GET /api/privacy/settings` (authenticated consent retrieval)
      - `POST /api/privacy/consent` (granular opt-in / opt-out)
      - `POST /api/privacy/guardian-consent/request` & `GET /api/privacy/guardian-consent/verify` (verifiable parental consent)
      - `GET /api/privacy/export` (DPDP Act Section 11 Right to Access personal data in JSON)
      - `POST /api/privacy/erase` (DPDP Act Section 12 Right to Erasure / account anonymization)
      - `POST /api/privacy/grievance` & `GET /api/privacy/grievance` (DPDP Act Section 13 Grievance Redressal)
    - Updated `User` entity and `SignUpRequestDto` to capture minor status, age, guardian details, and initial consents.
    - Added `PrivacyServiceTest` with 12 comprehensive unit tests covering all compliance methods (49 total passing tests in Spring Boot).
  - **Web Dashboard (`dashboard_app/`)**:
    - `CookieConsentBanner.tsx` with granular preferences modal and local storage persistence.
    - `ConsentOnboardingModal.tsx` for DPDP-compliant registration consent and minor guardian capture.
    - `LegalDocViewer.tsx` for dynamic fetching and rendering of legal documents.
    - `PrivacyCenter.tsx` dedicated settings page for consent toggles, data export, grievance filing/tracking, and irreversible erasure.
    - `PrivacyCenter.test.tsx` unit test suite (9 total tests passing).
    - SEO & AI Discoverability: valid `robots.txt`, `sitemap.xml`, `llms.txt`, canonical tags, Open Graph, Twitter cards, and JSON-LD structured data (`WebSite`, `SoftwareApplication`, `Organization`).
  - **Mobile App (`mobile/`)**:
    - `PrivacyScreen.tsx` with real-time toggle switches, data export triggers, erasure modals, and legal document links.
    - `PrivacyScreen.test.tsx` unit test suite (7 total tests passing, 0 TypeScript errors).
  - **E2E Test Infrastructure (`e2e_tests/`)**:
    - Updated `mock_backend.py` with mock implementations of all DPDP endpoints.
    - Added Tier 5 DPDP Act test suite (6 new tests, bringing total to 77 passing E2E tests).
  - **Engineering Governance**:
    - Established permanent Git-history and execution-history protocol documented in `docs/VERSION_HISTORY.md`, `docs/EXECUTION_HISTORY.md`, and `.agents/AGENTS.md`.

---

## [2.01.00.0] (v2.1.0) - 2026-02-15
Type: Sub-version
Status: Verified

### Added
- **Trips Ledger Subsystem**:
  - Live multi-city trip budgeting and auto-linking of transactions to active trips.
  - Category-based expense breakdown and percentage allocation algorithms.
  - Cash vs online payment aggregation and auto-completion of overlapping trips.
  - E2E test suite (`test-trips-e2e.py`) verifying 71 business cases across 4 tiers.

### Changed
- Refactored `DashboardController` and `TripService` for strict tenant isolation and IDOR prevention (`403 Forbidden` on unauthorized trip access).
- Redesigned Web Dashboard with Cormorant Garamond & Sora typography and INR currency note color scheme.

---

## [2.00.00.0] (v2.0.0) - 2026-01-20
Type: Major
Status: Verified

### Added
- **SpeDex 2.0 Master Architecture**:
  - Full-stack transition to Spring Boot 3.4.1 (Java 17) backend with Spring Security 6 & JWT authentication.
  - React 18 + TypeScript + Vite Web Dashboard with modular component architecture.
  - React Native / Expo Mobile Application with full feature parity.
  - Native Android Application bridge in `mobile_native_android/`.
  - Comprehensive Dockerization with multi-stage jar builds.

---

## [1.00.00.0] (v1.0.0) - 2025-11-10
Type: Major
Status: Verified

### Added
- Initial release of SpeDex high-frequency smart wallet platform.
- Basic UPI payment preparation, expense tracking, and offline cash entry.
