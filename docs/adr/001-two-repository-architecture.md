# ADR 001: Two-Repository Decoupled Architecture

## Status
Accepted

## Context
SpeDex requires global CDN performance for web assets and high-security, low-latency business logic with database access. Hosting frontend and backend within a single monolith runtime leads to tight coupling and deployment bottlenecks.

## Decision
Separate the frontend (`spe-dex-web` / `dashboard_app`) and backend API (`spe-dex-api` / `backend`).
- Frontend is deployed to Vercel as a client-side Single Page Application.
- Backend is deployed to Render as a containerized Spring Boot service.
- The web client communicates exclusively via HTTPS REST calls and never possesses database credentials.

## Consequences
- Clean separation of concerns.
- Independent deployment pipelines.
- Requires explicit CORS origin configuration and synchronized API contracts.
