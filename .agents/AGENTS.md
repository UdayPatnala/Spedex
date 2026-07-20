# Spedex Workspace Development Rules

## 1. Security & Data Isolation
- **Insecure Direct Object Reference (IDOR) Prevention**: For every controller or service endpoint that accesses or modifies a user-owned resource (e.g., Trips, Accounts, Transactions) by ID, you MUST explicitly verify that the resource belongs to the authenticated user.
- **Appropriate Error Codes**:
  - If a resource exists but does not belong to the user, throw an exception that resolves to a `403 Forbidden` response (message: `"Access denied"`). Do not return `401 Unauthorized` (which implies missing credentials) or leak details.
  - If a resource does not exist, return a `404 Not Found` response.

## 2. Integration Test Isolation
- **Prevent Database Pollution**: Do not use hardcoded email addresses, usernames, or unique keys across integration tests (e.g., `DatabaseModelsIntegrationTest`).
- **Dynamic Values**: Always generate unique email addresses (e.g., `"test_user_" + System.currentTimeMillis() + "@example.com"`) during test user setup to prevent unique index constraint collisions.

## 3. Scripting Scope Cleanliness
- **Python Scope Rules**: In python helper scripts or mock servers, ensure that `global` or `nonlocal` statements are declared once at the absolute beginning of the function body, preceding any variables reference or assignments.

## 4. Build & Deployment Isolation
- **Docker Copy Determinism**: When writing Dockerfiles for multi-stage Java builds, avoid using wildcard copy operations (e.g. `COPY --from=build /app/target/*.jar`) to copy into a single-file destination. Always target the specific jar name (`spedex-backend-1.0.0.jar`) to avoid build failures from multiple matching outputs.
- **Vercel SPA Monorepo Rewrites**: In monorepos where the Vercel project's Root Directory is configured to a subdirectory (e.g. `dashboard_app/`), place `vercel.json` directly inside the subdirectory rather than the repository root. Ensure it includes a catch-all rewrite rule mapping to `/index.html` to prevent 404 errors on browser page reloads.
- **Port 8080 Exclusivity**: E2E test scripts start a python mock backend on port 8080. Before initiating any E2E runs, ensure that any running local Spring Boot backend instance on port 8080 is paused or stopped to prevent port binding exceptions.

