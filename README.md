# SpeDex Smart Wallet Platform (v2.0 Enterprise)

SpeDex is a flagship, enterprise-grade Smart Wallet & Financial Intelligence Platform for high-frequency transaction tracking, automated trip ledgers, UPI vendor quick-pays, multi-currency budgeting, and real-time expense analytics.

The name blends **"Speed Index"** and **"Spending Index"** into a unified ecosystem spanning desktop web, cross-platform mobile, and scalable backend infrastructure.

---

## 🚀 Key Features & Capabilities

- **Automated Trip Ledger**: Create active trip sessions, track categorized trip expenses (cash vs online/card splits), auto-close completed trips, and generate detailed breakdown summaries.
- **UPI Vendor Quick-Pay**: Manage vendor directories, execute instant payments, and track default payment methods.
- **Multi-Currency Budgeting & Analytics**: Visual budget progress bars, category breakdowns, and real-time spending velocity charts.
- **Indian Rupee Currency Note Aesthetics**: Curated design system inspired by INR currency notes (₹2000 Magenta, ₹500 Stone Grey, ₹200 Bright Yellow, ₹100 Lavender, ₹50 Cyan, ₹20 Green, ₹10 Chocolate).
- **IDOR Protection & Security**: Strict resource-ownership validation across all REST endpoints returning `403 Forbidden` ("Access denied") on unauthorized access attempts.

---

## 📁 Repository Architecture

- `backend/`: Java 17 / Spring Boot REST API with JPA, H2/PostgreSQL database support, BCrypt password hashing, and JWT security filters.
- `dashboard_app/`: React Vite TypeScript Web Dashboard with modular component architecture, responsive glassmorphism UI, and custom theme tokens.
- `mobile/`: Cross-platform Expo React Native application featuring biometric authentication, navigation, and API sync.
- `mobile_native_android/`: Native Android Kotlin codebase.
- `e2e_tests/`: Python & Shell end-to-end testing suite for opaque-box verification.

---

## 🛠️ Quick Start & Local Development

### 1. Backend API (Java 17 / Spring Boot)

```bash
cd backend
./mvnw.cmd spring-boot:run
```
- API Endpoint: `http://localhost:8080/api`
- Run Test Suite: `.\mvnw.cmd test` (37/37 tests passing)

### 2. Web Dashboard (React + Vite)

```bash
cd dashboard_app
npm install
npm run dev
```
- Local URL: `http://localhost:5173`
- Run Build: `npm run build`
- Run Tests: `npm test` (7/7 tests passing)

### 3. Mobile App (Expo / React Native)

```bash
cd mobile
npm install
npm start
```
- Run Tests: `npm test` (5/5 tests passing)

---

## 🚢 Production Deployment Setup

### Render Deployment (Backend API)
- Built using root multi-stage [Dockerfile](file:///d:/PROJECT/Spedex/Dockerfile).
- Uses explicit target artifact `COPY --from=build /app/target/spedex-backend-1.0.0.jar /app/app.jar` to ensure deterministic Docker builds.
- Managed by [render.yaml](file:///d:/PROJECT/Spedex/render.yaml) on port `8080` with `/api/health` health checks.

### Vercel Deployment (Dashboard Monorepo)
- Configured via [dashboard_app/vercel.json](file:///d:/PROJECT/Spedex/dashboard_app/vercel.json) placed directly inside the `dashboard_app/` subdirectory.
- Includes catch-all SPA rewrite rule (`/(.*) -> /index.html`) to prevent 404 errors on browser refresh.

---

## 🧪 Verification & Test Suite Summary

- **Backend Unit & Integration Tests**: `37 Passed, 0 Failed`
- **Dashboard Web Vitest Suite**: `7 Passed, 0 Failed`
- **Mobile Expo Jest Suite**: `5 Passed, 0 Failed`
- **Dashboard Production Build**: `Clean compilation (1.12s)`

---

## 🔌 AROH Ecosystem Integration Guide

SpeDex integrates with the central **AROH Platform Ecosystem** via `@aroh/asdk`:
- **Single Sign-Out Sync**: Active tabs listen for `aroh_logout_event` in `localStorage` to immediately terminate sessions upon global logout.
- **Ledger Records**: Entitlements and wallet debits/credits map directly to the AROH Ledger.
