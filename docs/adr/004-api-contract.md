# ADR 004: RESTful API Contract & Evolution Strategy

## Status
Accepted

## Context
With two independent repositories, API contract drift between frontend and backend leads to breaking runtime errors.

## Decision
Establish an authoritative RESTful contract under `/api/*`:
- Standard HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`).
- Dedicated resource paths (`/api/transactions`, `/api/payment-references`, `/api/categories`, `/api/export`).
- Strict JSON request and response payloads.
- Future versions (V2, V3) must extend rather than discard endpoints. Any backward-incompatible modifications require explicit deprecation cycles.

## Consequences
- Predictable interfaces for web, mobile, and third-party integrations.
- Frontend and backend can evolve independently as long as the contract is preserved.
