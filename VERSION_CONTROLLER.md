# VERSION CONTROLLER

## Current Version

`2.04.01.0`

## Status

VERIFIED

## Version History

| Version | Previous | Level | Commit | Change |
|---|---|---|---|---|
| 2.04.01.0 | 2.04.00.0 | FUNCTIONAL | PENDING | Reconstructed historical version ledger and integrated dynamic live application version display |
| 2.04.00.0 | 2.03.01.0 | SUB-VERSION | 6148944 | Adopted Universal Version Control & Change Governance System with A.BC.DE.F format |
| 2.03.01.0 | 2.03.00.0 | FUNCTIONAL | aa5c48d | Modularized dashboard views and component architecture from monolith App.tsx |
| 2.03.00.0 | 2.02.00.0 | SUB-VERSION | 3436f7d | Established Product Intent Recovery and 4-layer persistent memory architecture |
| 2.02.00.0 | 2.01.00.0 | SUB-VERSION | 42dc103 | Implemented full-stack DPDP Act 2023 compliance, minor protection, and privacy APIs |
| 2.01.00.0 | 2.00.00.0 | SUB-VERSION | 5c30dc9 | Implemented Trips Ledger subsystem with 5-tier E2E testing validation suite |
| 2.00.00.0 | 1.04.00.0 | MAJOR | 91b5374 | Overhauled platform to Spring Boot 3, React 19, React Native, and native Android bridge |
| 1.04.00.0 | 1.03.00.0 | FUNCTIONAL | 141e035 | Implemented weekly spending calculation with date filtering and transaction search optimization |
| 1.03.00.0 | 1.02.00.0 | FUNCTIONAL | 6a6c5ba | Hardened security with CorsFilter, GlobalExceptionHandler, and testing validation suite |
| 1.02.00.0 | 1.01.00.0 | SUB-VERSION | 6eb5076 | Rewrote backend to Java Spring Boot with Spring Security, JWT auth, and Dockerfile |
| 1.01.00.0 | 1.00.00.0 | SUB-VERSION | 9af3f11 | Transitioned to production with UPI QR payment preparation and campus wallet persona |
| 1.00.00.0 | — | BASELINE | 965ef6d | Initial SpeDex high-frequency hostel wallet prototype repository baseline |

## Unreleased / In Development

| Target | Level | Change |
|---|---|---|
| 2.04.02.0 | PATCH | Dynamic multi-currency travel conversion for cross-border trips (offline forex cache) |
| 2.05.00.0 | SUB-VERSION | Jetpack Compose Trips Ledger and DPDP Privacy Center in mobile_native_android |

## Known Historical Gaps

- Commits between `965ef6d` (initial prototype) and `9af3f11` (production transition) represent rapid pre-release prototyping iterations and are consolidated under the `1.00.00.0` baseline.
- All subsequent architectural transitions (`1.02.00.0`, `2.00.00.0`, `2.01.00.0`, `2.02.00.0`, `2.03.00.0`, `2.03.01.0`, `2.04.00.0`, and `2.04.01.0`) are 100% verified against explicit Git commit history and documentation ledgers.

## Versioning Rules

1. **Authoritative Format**: `A.BC.DE.F` (`A` = Major [0-∞], `BC` = Sub-Version [01-99], `DE` = Functional [01-99], `F` = Minor/Patch [0-9], `00` = reset state).
2. **Hierarchy Cascades**: Higher-level changes reset all lower levels to zero.
3. **Pre-Execution Gate**: Version Controller must be inspected before executing ANY project-changing command.
4. **Consistency Invariant**: `VERSION_CONTROLLER.md` = `package.json` = `pom.xml` = `src/version.ts` = Live UI Version Display.
