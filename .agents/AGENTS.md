# Agent Operations & Multi-Agent Guidelines: SpeDex

## Autonomous Engineering Protocols

### 1. Vision & Purpose Alignment
- Always preserve SpeDex's identity as a high-frequency smart wallet and financial intelligence platform.
- Maintain INR currency note aesthetic standards and typography hierarchy (Cormorant Garamond + Sora).

### 2. Architectural Boundaries
- Backend logic must strictly reside in `backend/`. Maintain clean separation between Controllers, Services, and Repositories.
- Web UI components reside in `dashboard_app/src/components/`. Keep components modular and reusable.
- Mobile cross-platform screens reside in `mobile/src/screens/`. Maintain feature parity with the web dashboard.
- Native Android code resides in `mobile_native_android/app/src/main/java/`.

### 3. Security & Validation Rules
- All backend endpoints must validate user ownership. Always return `403 Forbidden` for IDOR attempts.
- Passwords must always be hashed with BCrypt.
- Pass token authentication headers in all client API calls.

### 4. Verification Standard
Before declaring any task or milestone complete:
1. Run `./mvnw.cmd test` in `backend/` (37 tests passing).
2. Run `npm test` and `npx tsc --noEmit` in `dashboard_app/` (7 tests passing, 0 type errors).
3. Run `npm test` and `npx tsc --noEmit` in `mobile/` (6 tests passing, 0 type errors).
4. Run `python test-trips-e2e.py` (71 tests passing).
