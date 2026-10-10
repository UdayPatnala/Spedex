# ADR 002: Managed PostgreSQL Database Engine

## Status
Accepted

## Context
SpeDex handles financial transactions and user ledgers that require ACID guarantees, relational integrity, and persistent storage across cloud service restarts. Render web services have ephemeral local disks, which wipe local files (such as H2 file stores) upon redeployment.

## Decision
Adopt managed PostgreSQL as the authoritative persistence engine in production.
- Use `org.postgresql:postgresql` driver with Spring Boot `prod` profile (`application-prod.properties`).
- Configure dynamic database connection parsing via standard cloud `DATABASE_URL`.
- Retain in-memory H2 for fast hermetic local development and automated testing suites.

## Consequences
- Guaranteed persistence and relational consistency in production.
- Production environment variables must supply valid `DATABASE_URL`.
