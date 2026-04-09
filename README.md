# Ledger

Ledger is a fintech starter built from the exported design pack in this workspace. It includes:

- `backend/`: FastAPI API with SQLite-by-default and PostgreSQL-ready configuration
- `mobile/`: Expo React Native app that matches the mobile Ledger screens
- `dashboard_app/`: React dashboard that mirrors the desktop design exports
- design asset folders such as `payment_handoff/`, `dashboard/`, and `desktop_dashboard/` for reference

## Quick Start

### 1. Backend

```bash
cd backend
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload
```

The API defaults to SQLite at `backend/ledger.db`. Set `DATABASE_URL` to a PostgreSQL connection string to switch databases.

Demo credentials:

- Email: `alex@ledger.dev`
- Password: `ledger123`

### 2. Mobile

```bash
cd mobile
npm install
npm start
```

By default the app points at `http://localhost:8000`. For Android emulators it automatically uses `http://10.0.2.2:8000`.

### 3. Dashboard

```bash
cd dashboard_app
npm install
npm run dev
```

## API Highlights

- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/mobile/home`
- `GET /api/mobile/budgets`
- `GET /api/mobile/vendors`
- `GET /api/mobile/analytics`
- `GET /api/dashboard/overview`
- `POST /api/payments/prepare`
- `POST /api/payments/{transaction_id}/complete`

## Design Notes

The implementation follows the Ledger design brief in [`indigo_ledger/DESIGN.md`](/D:/PROJECT/Spendex/indigo_ledger/DESIGN.md), especially the indigo-led tonal surfaces, Manrope headline typography, and the "no-line" sectioning rule.
