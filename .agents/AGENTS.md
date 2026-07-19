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
