# SpeDex Smart Wallet Platform (v1.0.0 / v2.07 Production MVP)

SpeDex is a production-ready personal expense tracking, financial awareness, and merchant payment directory platform. It provides high-frequency transaction logging, instant search and categorization, automated trip ledgers, non-custodial saved payment references, multi-currency budgeting, and real-time expense analytics.

The name blends **"Speed Index"** and **"Spending Index"** into a unified ecosystem spanning desktop web, cross-platform mobile, native Android, and scalable backend infrastructure.

> **Authoritative Documentation**:
> - 📘 **Project Master Knowledge File**: [`SPEDEX.md`](./SPEDEX.md) (Single source of truth for architecture, domain models, APIs, and guardrails)
> - 📜 **Version Controller & History Ledger**: [`VERSION_CONTROLLER.md`](./VERSION_CONTROLLER.md) (Mandatory persistent version governor under `A.BC.DE.F`)
> - 📋 **Release Changelog**: [`CHANGELOG.md`](./CHANGELOG.md)
> - 📚 **Architecture & System Guides**: [`docs/`](./docs/) ([Architecture](./docs/architecture.md) | [Deployment](./docs/deployment.md) | [Security](./docs/security.md) | [API](./docs/api.md) | [Database](./docs/database.md) | [Testing](./docs/testing.md) | [ADRs](./docs/adr/))

---

## 🚀 Key Features & Capabilities

- **Complete 18-Step User Journey**: Full end-to-end functionality across user registration, login, profile management, transaction creation, editing, deletion, categorized listing, search & date filtering, spending summaries, analytics, saved payment references directory, personal dashboard, RFC 4180 CSV export, account erasure, and secure sign-out.
- **Automated Trip Ledger**: Create active travel sessions, track categorized trip expenses (cash vs online/card splits), auto-close completed trips, and generate detailed breakdown summaries.
- **Non-Custodial Saved Payment References**: Securely store merchant UPI IDs (VPAs), payment links, and QR codes with strict URI scheme validation (`upi://`, `http://`, `https://`).
- **Multi-Currency Budgeting & Analytics**: Visual budget progress bars, category breakdowns, and real-time spending velocity charts.
- **Age Gating & Minor Financial Learning Mode**: Dedicated `<18` protective mode where payment preparation is blocked server-side (HTTP 403 Forbidden) while manual bookkeeping and educational budgeting remain active.
- **DPDP Act 2023 Compliance**: Purpose-separated consent toggles, verifiable parental verification links, machine-readable JSON data export (`GET /api/privacy/export`), RFC 4180 CSV export (`GET /api/export/transactions`), hard-gated erasure (`POST /api/privacy/erase`), and in-app grievance redressal.
- **Firebase Analytics & Telemetry**: Safe client-side analytics telemetry integrated with DPDP consent controls.
- **Indian Rupee Currency Note Aesthetics**: Curated design system inspired by INR currency notes paired with Cormorant Garamond headings and Sora body typography.
- **IDOR Protection & Enterprise Security**: Strict resource-ownership validation across all REST endpoints returning `403 Forbidden` ("Access denied") on unauthorized access attempts.

---

## 📁 Repository Architecture

```text
Spedex/
├── backend/                  # Java 17 / Spring Boot REST API (JPA, PostgreSQL/H2, Security, JWT)
│   ├── src/main/java/        # Controllers, Services, Repositories, Models, Security
│   └── src/test/java/        # Integration & Unit test suites (65 tests)
├── dashboard_app/            # React 19 + Vite 6 + TypeScript Web Dashboard (Vercel target)
│   ├── src/components/       # UI components (Transactions, Modals, Sidebar, PrivacyCenter)
│   ├── src/views/            # Domain views (TransactionsView, Home, Analytics, Budget, Trips, Settings, Auth)
│   ├── src/firebase.ts       # Firebase app & analytics configuration
│   └── src/api.ts            # REST API client with full error handling and auth token injection
├── mobile/                   # Cross-platform Expo React Native application
│   ├── src/screens/          # Touch-optimized mobile screens (Home, Trips, Budget, Reminders, Analytics, Privacy)
│   └── src/auth/             # Authentication & biometrics provider
├── mobile_native_android/    # Native Kotlin Android codebase (Jetpack Compose, Room DB)
├── e2e_tests/                # Python end-to-end testing suite (77 test cases)
├── docs/                     # Comprehensive architecture, deployment, API, database, and ADR docs
├── SPEDEX.md                 # Authoritative Project Master Knowledge File
├── VERSION_CONTROLLER.md     # Authoritative Version Controller Ledger
└── CHANGELOG.md              # Product release changelog
```

---

## 🛠️ Quick Start & Local Development

### 1. Backend API (Java 17 / Spring Boot)

```bash
cd backend
./mvnw.cmd spring-boot:run
```
- API Endpoint: `http://localhost:8080/api`
- Run Test Suite: `./mvnw.cmd test` (65/65 tests passing)

### 2. Web Dashboard (React + Vite + TypeScript)

```bash
cd dashboard_app
npm install
npm run dev
```
- Local URL: `http://localhost:5173`
- Run Build: `npm run build`
- Run Tests: `npm test -- --run` (10/10 tests passing)
- Run Typecheck: `npx tsc --noEmit`

### 3. Mobile App (Expo / React Native)

```bash
cd mobile
npm install
npm start
```
- Run Tests: `npm test` (5 test suites / 7 tests passing)
- Run Typecheck: `npx tsc --noEmit`

### 4. End-to-End Test Suite (Python)

```bash
python test-trips-e2e.py
```
- Executes 77 comprehensive end-to-end API and security tests with mock backend orchestration.

---

## 🚢 Production Deployment

### Frontend (Vercel)
- Root directory: `dashboard_app`
- Build command: `npm run build`
- Output directory: `dist`
- Configured via `dashboard_app/vercel.json` with catch-all SPA rewrite rules (`/(.*) -> /index.html`).

### Backend (Render)
- Runtime: Docker / Java 17
- Spring Profile: `prod` (uses `application-prod.properties` with PostgreSQL driver)
- Database: Managed PostgreSQL (Render / Supabase) via `DATABASE_URL`
- Health check: `GET /api/health`

---

## 🧪 Verification & Test Suite Summary

| Test Suite | Framework | Status | Metrics |
|---|---|---|---|
| Backend Controllers, Services, IDOR & Safety | JUnit 5 / Spring Boot | ✅ PASSING | 65 tests |
| Dashboard Web App & Ledger | Vitest / React Testing Library | ✅ PASSING | 10 tests |
| Mobile Expo App | Jest / Jest-Expo | ✅ PASSING | 5 suites / 7 tests |
| End-to-End Suite | Python `unittest` | ✅ PASSING | 77 tests |
| Frontend Typecheck | TypeScript 5.8 | ✅ PASSING | 0 errors |
| Mobile Typecheck | TypeScript 5.8 | ✅ PASSING | 0 errors |
| **TOTAL VERIFIED TEST SUITE** | Full Ecosystem | ✅ **PASSING** | **159 / 159 tests (100%)** |

---

## 🔌 AROH Ecosystem Integration Guide

SpeDex integrates with the central **AROH Platform Ecosystem** via `@aroh/asdk`:
- **Single Sign-Out Sync**: Active tabs listen for `aroh_logout_event` in `localStorage` to immediately terminate sessions upon global logout.
- **Ledger Records**: Entitlements and wallet debits/credits map directly to the AROH Ledger.
