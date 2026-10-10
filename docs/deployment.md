# SpeDex Deployment Guide (Vercel + Render)

This document provides exact, production-verified deployment instructions for the **SpeDex V1 Production-Ready MVP**.

---

## Architecture Overview

```text
┌──────────────────────────────────────────────────────────┐
│                   VERCEL GLOBAL CDN                      │
│   • Dashboard Single Page Application (React 19, Vite 6)  │
│   • Static assets, caching headers, HTTPS edge routing   │
│   • vercel.json rewrite rule: /* -> /index.html           │
└────────────────────────────┬─────────────────────────────┘
                             │
                             ▼  HTTPS REST + Bearer JWT
┌──────────────────────────────────────────────────────────┐
│                     RENDER CONTAINER                     │
│   • Java 17 Spring Boot 3 API Service                    │
│   • Multi-stage Docker container build from /Dockerfile  │
│   • Dynamic PORT binding ($PORT)                         │
│   • Health probe: /api/health                            │
└────────────────────────────┬─────────────────────────────┘
                             │
                             ▼  Encrypted JDBC Connection
┌──────────────────────────────────────────────────────────┐
│                 MANAGED POSTGRESQL                       │
│   • Render Postgres or Supabase Managed Instance         │
│   • Injected via DATABASE_URL                            │
│   • Fallback: file-backed H2 for local development       │
└──────────────────────────────────────────────────────────┘
```

---

## 1. Local Development Setup

### Prerequisites
- JDK 17+ (Eclipse Temurin recommended)
- Node.js 20+ and npm
- Python 3.10+ (for E2E tests)

### Step 1: Clone & Configure Environment
```bash
# Clone the repository
git clone https://github.com/UdayPatnala/Spedex.git
cd Spedex

# Copy environment template
cp .env.example .env
```

### Step 2: Run Backend API
```bash
cd backend
./mvnw.cmd spring-boot:run
# Server starts on http://localhost:8080 (or $PORT)
# Health check: http://localhost:8080/api/health
```

### Step 3: Run Frontend Web Dashboard
```bash
cd ../dashboard_app
npm install
npm run dev
# Dashboard launches on http://localhost:5173
```

### Step 4: Run Tests
```bash
# Backend suite (65 tests)
cd backend && ./mvnw.cmd test

# Dashboard suite (10 tests)
cd ../dashboard_app && npm test -- --run

# Mobile suite (7 tests)
cd ../mobile && npm test

# Root E2E suite (77 tests)
python test-trips-e2e.py
```

---

## 2. Vercel Deployment (Frontend Web Dashboard)

### Vercel Project Settings
- **Framework Preset**: `Vite`
- **Root Directory**: `dashboard_app`
- **Build Command**: `npm run build`
- **Output Directory**: `dist`
- **Install Command**: `npm install`

### Vercel Environment Variables
| Variable | Value | Description |
|---|---|---|
| `VITE_API_BASE_URL` | `https://spedex.onrender.com/api` | The live URL of the Render backend |

### Client-Side SPA Routing (`vercel.json`)
The application includes `dashboard_app/vercel.json`:
```json
{
  "rewrites": [
    {
      "source": "/(.*)",
      "destination": "/index.html"
    }
  ]
}
```
This ensures direct navigation, browser refresh, and deep links (such as `/trips`, `/ledger`, `/privacy`) route properly to the SPA index.

---

## 3. Render Deployment (Backend API)

### Option A: Docker Service (Recommended)
Render natively detects the root `Dockerfile`.

1. **Create New Web Service on Render**:
   - Connect repository `Spedex`.
   - **Environment**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Docker Context**: `.`

2. **Environment Variables on Render**:
   | Key | Example Value | Notes |
   |---|---|---|
   | `PORT` | `8080` | Render will automatically inject its own internal port |
   | `JWT_SECRET` | `at_least_256_bits_long_cryptographically_secure_random_key` | Secret used for signing JWT tokens |
   | `SPRING_PROFILES_ACTIVE` | `prod` | Activates PostgreSQL configuration |
   | `DATABASE_URL` | `jdbc:postgresql://<host>:<port>/<db_name>` | Connection string for Render PostgreSQL or Supabase |
   | `DB_USER` | `postgres` | Database username |
   | `DB_PASSWORD` | `secret_password` | Database password |
   | `SPEDEX_CORS_ALLOWED_ORIGINS` | `https://spedex.vercel.app,https://spe-dex.vercel.app,http://localhost:5173` | Allowed CORS origins |

3. **Health Check**:
   - Path: `/api/health`
   - Response: `{"status":"ok","timestamp":...}`

### ⚠️ Common Render Deployment Error: `Couldn't find a package.json file`
If your deploy log displays:
```text
==> Running 'yarn start'
error Couldn't find a package.json file in "/opt/render/project/src"
==> Exited with status 1
```
**Cause**: Render defaulted the service **Environment / Runtime** to `Node` instead of `Docker`.
**Fix**:
1. In your Render Dashboard, click **Settings** in the left sidebar for your service.
2. Under **Build & Deploy**, look for **Environment / Runtime**.
3. Change it from **Node** to **Docker**.
4. Set **Dockerfile Path** to `./Dockerfile` (or leave as default `./Dockerfile`).
5. Set **Docker Build Context** to `.` (the repository root).
6. Click **Save Changes** and trigger **Manual Deploy** -> **Clear build cache & deploy**.

---

## 4. Verification Checklist Before Marking Live

- [x] Backend responds to `/api/health` with `{"status":"ok"}`.
- [x] Unauthenticated users see the landing page and can register or login.
- [x] Real JWT token stored securely in `localStorage` upon authentication.
- [x] All 18 core user loops operational against database persistence.
- [x] User isolation strictly enforced (`403 Forbidden` on IDOR attempts).
- [x] CSV transaction export downloads RFC 4180 file with correct headers.
- [x] Account erasure permanently cascades and clears personal records.
- [x] 159 / 159 tests passing across all 4 verification layers.
