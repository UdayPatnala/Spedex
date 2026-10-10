# ADR 006: V1 Production Release Baseline & Continuous Evolution

## Status
Accepted

## Context
A recurring failure mode in software engineering is treating V1 as disposable throwaway code and rewriting V2 from scratch. SpeDex requires permanent codebase continuity where every subsequent version (V2, V3) builds upon the tested V1 foundation.

## Decision
Establish `v1.0.0` (SpeDex V1 Production-Ready MVP Baseline / internal `2.07.00.0`) as the permanent baseline.
- All 18 core user loop operations are verified and fully implemented.
- 159 automated tests passing across backend, dashboard, mobile, and root E2E suites.
- Future versions must strictly follow the Keep / Modify / Extend / Refactor / Add / Deprecate protocol.
- Database evolution must occur through migrations, preserving existing production data.

## Consequences
- Eliminates wasteful ground-up rewrites.
- Guarantees backward compatibility and historical traceability.
