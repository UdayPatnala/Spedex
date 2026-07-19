# E2E Test Infra: Spedex Trips Ledger

## Test Philosophy
- Opaque-box, requirement-driven. No dependency on implementation design.
- Methodology: Category-Partition + BVA + Pairwise + Workload Testing.

## Feature Inventory
| # | Feature | Source | Tier 1 | Tier 2 | Tier 3 |
|---|---------|--------|:------:|:------:|:------:|
| F1| Trip Lifecycle | R1, R2 | 5 | 5 | ✓ |
| F2| Auto-linking Transactions | R1 | 5 | 5 | ✓ |
| F3| Manual CASH Transactions | R1, R2 | 5 | 5 | ✓ |
| F4| Aggregated Trip Statistics | R1, R2 | 5 | 5 | ✓ |
| F5| Security Isolation | R1 | 5 | 5 | ✓ |
| F6| Custom Tags & UI | R2 | 5 | 5 | ✓ |

## Test Architecture
- **Test Runner**: We will implement a test runner script (`test-trips-e2e.py` or similar) in the root or a dedicated subfolder that uses HTTP client requests with JWT authentication headers to verify functionality.
- **Pass/Fail Semantics**: Clean exit code 0.
- **Directory Layout**:
  - `backend/src/test/...` - Unit & Integration tests for repositories/services/controllers.
  - `dashboard_app/src/...` - Component tests for UI rendering and interactions.
  - `e2e_tests/` - Scripted integration tests simulating backend API calls and E2E scenarios.

## Coverage Thresholds
- **Tier 1 (Feature Coverage)**: ≥5 test cases per feature (Total: 30 test cases).
- **Tier 2 (Boundary & Corner Cases)**: ≥5 test cases per feature covering boundaries (e.g. empty names, extreme values, multiple active trips attempt, non-existent ids, etc.) (Total: 30 test cases).
- **Tier 3 (Cross-Feature Combinations)**: ≥6 test cases covering pairs of features (e.g., active trip + automatic transaction linking, active trip + manual transaction logging, trip completion + history review).
- **Tier 4 (Real-World Application Scenarios)**: 5 application-level scenarios (e.g., a complete travel flow where a user starts a trip, does several online payments, logs cash expenses with standard and custom tags, completes the trip, and reviews the final report).
