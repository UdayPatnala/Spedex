# SpeDex Test Infrastructure Documentation

## Overview
The SpeDex testing infrastructure provides isolated, hermetic, and fast testing environments for local development and CI pipelines.

---

## 1. Mock Backend Architecture (`e2e_tests/mock_backend.py`)
- Built using Python's `http.server.HTTPServer` with an in-memory datastore.
- Accurately mirrors all Spring Boot REST API endpoints, response schemas, and authentication headers.
- Emulates IDOR security checks by tracking multiple user identities (`user_1`, `user_2`) and verifying token subject matching.

---

## 2. Test Execution Harness (`test-trips-e2e.py`)
- Launches the mock backend server in a background subprocess on a dedicated port.
- Polls the `/api/health` endpoint until healthy before launching test suites.
- Executes all 71 test assertions in `e2e_tests/test_trips_e2e.py`.
- Gracefully shuts down the background server on exit.

---

## 3. Spring Boot Test Configuration (`backend/src/test/resources/application.properties`)
- Uses H2 in-memory database with `create-drop` DDL mode for isolated execution.
- Disables external network and database dependencies.
- Configures deterministic JWT secrets and short expiration timeouts for token tests.
