# SpeDex V1 Security & Privacy Controls

## 1. Authentication & Session Management
- **Password Storage**: BCrypt hashing with strong salt factors. Passwords are never logged or stored in plain text.
- **JWT Authorization**: Stateless JSON Web Tokens signed with server-side HMAC secret (`jwt.secret`).
- **Session Revocation**: Tokens have configured lifetimes; client logout removes the local token.

## 2. Insecure Direct Object References (IDOR) & Tenant Isolation
Every data mutation and query strictly checks user identity on the backend:
- `transactionRepository.findByIdAndUserId(id, user.getId())`
- `vendorRepository.findByIdAndUserId(id, user.getId())`
- `tripRepository.findByIdAndUserId(id, user.getId())`
Attempts by User A to view, update, or delete User B's resources fail immediately with `403 Forbidden` (`Access denied`).

## 3. Input Validation & Injection Defense
- **SQL Injection**: Handled via parameterized queries and Spring Data JPA / Hibernate ORM specifications.
- **URI Validation**: Payment reference URLs must match safe schemes (`upi://`, `http://`, `https://`). Arbitrary script protocols (`javascript:`) are rejected.
- **CORS Defense**: Unrestricted wildcard origins (`*`) are prohibited in production. Explicit origins (`https://spe-dex-web.vercel.app`) are mapped in `SecurityConfig`.

## 4. DPDP Act 2023 & Minor Protection
- **Age Gating**: Users `<18` are assigned `ROLE_USER` with `isMinor = true`.
- **Financial Learning Mode**: Server-side interceptors block live payment initiation and QR code generation for minors (`403 Forbidden`), while preserving manual expense logging and educational budgeting.
- **Purpose-Separated Consent**: Explicit individual consent flags for Essential, Telemetry, Marketing, Geolocation, and AI.
- **Data Erasure**: Complete, hard-gated deletion of all transactions, trips, vendor references, and user records upon double confirmation.

## 5. Secret Hygiene & Git Push Security Controls
- **Zero Committed Secrets**: Real API keys, database credentials, JWT secrets, private certificates, and cloud tokens are strictly prohibited in Git.
- **Environment Externalization**: All secrets are loaded through environment variables (`.env`, platform secret managers, or cloud environment settings).
- **Template Synchronization**: Every component provides a sanitized `.env.example` template with placeholder values.
- **Multi-Tier Git Exclusions**: Comprehensive `.gitignore` configurations exclude `.env*`, `*.pem`, `*.key`, `*.jks`, `*.keystore`, and service account JSON files across root, frontend, and backend packages.
- **Automated Pre-Commit Scanner**: Integrated automated scanner (`scripts/scan-secrets.py`) audits staged files for regex patterns (Firebase, AWS, GitHub tokens, private keys) before every commit and git push.

