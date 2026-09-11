# Agent Operations & Multi-Agent Guidelines: SpeDex

## Autonomous Engineering Protocols

### 1. Vision, Purpose & Compliance Alignment
- Always preserve SpeDex's identity as a high-frequency smart wallet, financial intelligence platform, and non-custodial financial utility.
- Maintain INR currency note aesthetic standards and typography hierarchy (Cormorant Garamond + Sora).
- Strictly enforce DPDP Act 2023 & DPDP Rules 2025 compliance across all codebases (verifiable parental consent for minors <18, non-custodial financial disclaimers, granular opt-in/opt-out consent, data minimization, right to access/export, right to erasure, and grievance redressal).

### 2. Architectural Boundaries
- Backend logic must strictly reside in `backend/`. Maintain clean separation between Controllers, Services, and Repositories.
- Web UI components reside in `dashboard_app/src/components/` and views in `dashboard_app/src/views/`. Keep components modular and reusable.
- Mobile cross-platform screens reside in `mobile/src/screens/`. Maintain feature parity with the web dashboard.
- Native Android code resides in `mobile_native_android/app/src/main/java/`.
- Legal policies and markdown disclosures reside in `legal/`.
- Version and execution history reside in `docs/VERSION_HISTORY.md` and `docs/EXECUTION_HISTORY.md`.

### 3. Security, Privacy & Validation Rules
- All backend endpoints must validate user ownership. Always return `403 Forbidden` for IDOR attempts.
- Passwords must always be hashed with BCrypt.
- Pass token authentication headers in all client API calls.
- Never store banking passwords, MPINs, or UPI credentials.
- Minors (<18) must have behavioral tracking and marketing cookies permanently disabled.

### 4. Mandatory Git & Execution History Protocol
For EVERY task executed in this repository:
1. **Before Task**:
   - Inspect `git status`, branch, current commit hash, and complete git log.
   - Read `docs/VERSION_HISTORY.md` and `docs/EXECUTION_HISTORY.md` to extract historical patterns and constraints.
2. **During Implementation**:
   - Preserve existing architecture and backward compatibility.
   - Maintain strict typing and modular separation.
3. **After Task**:
   - Run the 4-part Verification Standard.
   - Record the execution entry in `docs/EXECUTION_HISTORY.md` and update `docs/VERSION_HISTORY.md`.

### 5. Verification Standard
Before declaring any task or milestone complete:
1. Run `./mvnw.cmd test` in `backend/` (49 tests passing).
2. Run `npm test -- --run` and `npm run build` in `dashboard_app/` (9 tests passing, 0 type errors).
3. Run `npm test` and `npx tsc --noEmit` in `mobile/` (7 tests passing, 0 type errors).
4. Run `python test-trips-e2e.py` (77 tests passing).
