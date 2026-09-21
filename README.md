# SpeDex Smart Wallet Platform (v2.05 Enterprise)

SpeDex is a flagship, enterprise-grade Smart Wallet & Financial Intelligence Platform for high-frequency transaction tracking, automated trip ledgers, UPI vendor quick-pays, multi-currency budgeting, and real-time expense analytics.

The name blends **"Speed Index"** and **"Spending Index"** into a unified ecosystem spanning desktop web, cross-platform mobile, native Android, and scalable backend infrastructure.

> **Authoritative Documentation**:
> - 📘 **Project Master Knowledge File**: [`SPEDEX.md`](./SPEDEX.md) (Single source of truth for architecture, domain models, APIs, and guardrails)
> - 📜 **Version Controller & History Ledger**: [`VERSION_CONTROLLER.md`](./VERSION_CONTROLLER.md) (Mandatory persistent version governor under `A.BC.DE.F`)

---

## 🚀 Key Features & Capabilities

- **Automated Trip Ledger**: Create active trip sessions, track categorized trip expenses (cash vs online/card splits), auto-close completed trips, and generate detailed breakdown summaries.
- **UPI Vendor Quick-Pay**: Manage vendor directories, execute instant payments, and track default payment methods.
- **Multi-Currency Budgeting & Analytics**: Visual budget progress bars, category breakdowns, and real-time spending velocity charts.
- **Age Gating & Minor Financial Learning Mode**: Dedicated `<18` protective mode where payment preparation is blocked server-side (HTTP 403 Forbidden) while manual bookkeeping and educational budgeting remain active.
- **DPDP Act 2023 Compliance**: Purpose-separated consent toggles, verifiable parental verification links, machine-readable JSON data export (`GET /api/privacy/export`), hard-gated erasure (`POST /api/privacy/erase`), and in-app grievance redressal.
- **Interactive Mobile Sync Simulator**: Embedded side-by-side companion simulator in the Web Dashboard for real-time mobile sync testing.
- **Direct Frictionless Workspace Access**: Immediate access to the financial dashboard with landing page exploration option.
- **Indian Rupee Currency Note Aesthetics**: Curated design system inspired by INR currency notes (₹2000 Magenta, ₹500 Stone Grey, ₹200 Bright Yellow, ₹100 Lavender, ₹50 Cyan, ₹20 Green, ₹10 Chocolate) paired with Cormorant Garamond headings and Sora body typography.
- **IDOR Protection & Enterprise Security**: Strict resource-ownership validation across all REST endpoints returning `403 Forbidden` ("Access denied") on unauthorized access attempts.

---

## 📁 Repository Architecture

```text
Spedex/
├── backend/                  # Java 17 / Spring Boot REST API (JPA, H2/PostgreSQL, Security, JWT)
│   ├── src/main/java/        # Application source code (Controllers, Services, Repositories, Models)
│   └── src/test/java/        # Unit & integration test suites (JUnit 5, Mockito - 55 tests)
├── dashboard_app/            # React 19 + Vite 6 + TypeScript Web Dashboard
│   ├── src/components/       # UI components (Sidebar, Topbar, Modals, MobileSyncSimulator, LandingPage, PrivacyCenter)
│   ├── src/views/            # Domain view modules (Home, Payments, Analytics, Budget, Trips, Settings, Auth)
│   └── src/api.ts            # REST API client with offline/fallback support
├── mobile/                   # Cross-platform Expo React Native application
│   ├── src/screens/          # Touch-optimized mobile screens (Home, Trips, Budget, Reminders, Analytics, Privacy)
│   └── src/auth/             # Authentication & biometrics provider
├── mobile_native_android/    # Native Kotlin Android codebase (Jetpack Compose, Room DB)
├── e2e_tests/                # Python end-to-end testing suite (77 test cases)
├── Dockerfile                # Multi-stage container build configuration
├── SPEDEX.md                 # Authoritative Project Master Knowledge File
└── VERSION_CONTROLLER.md     # Authoritative Version Controller Ledger
```

---

## 🛠️ Quick Start & Local Development

### 1. Backend API (Java 17 / Spring Boot)

```bash
cd backend
./mvnw.cmd spring-boot:run
```
- API Endpoint: `http://localhost:8080/api`
- Run Test Suite: `./mvnw.cmd test` (55/55 tests passing)

### 2. Web Dashboard (React + Vite + TypeScript)

```bash
cd dashboard_app
npm install
npm run dev
```
- Local URL: `http://localhost:5173`
- Run Build: `npm run build`
- Run Tests: `npm test -- --run` (9/9 tests passing)
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

### Docker Multi-Stage Build
- Built using root multi-stage `Dockerfile`.
- Uses deterministic artifact target `COPY --from=build /app/target/spedex-backend-1.0.0.jar /app/app.jar`.
- Health check configured on `/api/health`.

### Vercel Deployment (Dashboard)
- Configured via `dashboard_app/vercel.json` with catch-all SPA rewrite rules (`/(.*) -> /index.html`) to support client-side routing.

---

## 🧪 Verification & Test Suite Summary

| Test Suite | Framework | Status | Metrics |
|---|---|---|---|
| Backend Services, IDOR & Safety | JUnit 5 / Spring Boot | ✅ PASSING | 55 tests |
| Dashboard Web App | Vitest / React Testing Library | ✅ PASSING | 9 tests |
| Mobile Expo App | Jest / Jest-Expo | ✅ PASSING | 5 suites / 7 tests |
| End-to-End Suite | Python `unittest` | ✅ PASSING | 77 tests |
| Frontend Typecheck | TypeScript 5.8 | ✅ PASSING | 0 errors |
| Mobile Typecheck | TypeScript 5.8 | ✅ PASSING | 0 errors |
| **TOTAL VERIFIED TEST SUITE** | Full Ecosystem | ✅ **PASSING** | **148 / 148 tests (100%)** |

---

## 🔌 AROH Ecosystem Integration Guide

SpeDex integrates with the central **AROH Platform Ecosystem** via `@aroh/asdk`:
- **Single Sign-Out Sync**: Active tabs listen for `aroh_logout_event` in `localStorage` to immediately terminate sessions upon global logout.
- **Ledger Records**: Entitlements and wallet debits/credits map directly to the AROH Ledger.
