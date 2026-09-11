# UNIVERSAL PROJECT ENGINEERING & EXECUTION SYSTEM: SPEDEX

## Master Execution Directive

```text
NEVER START BY CODING.

FIRST:
Understand the product → recover previous conversations → inspect all relevant project files/docs → read complete relevant Git history → inspect current state → identify what exists, what is missing, what failed, what was intentionally removed, and what is over-engineered → research only what is necessary → make the engineering/product decision → create a safe recoverable checkpoint.

THEN:
Implement the simplest correct working solution → verify → improve only where justified → test → audit security/privacy/performance/accessibility/UX/deployment → inspect Git diff/status → re-check history → update VERSION_HISTORY.md and EXECUTION_HISTORY.md → preserve the final known-good state → record remaining work.

IF THE TASK IS TOO LARGE:
Do NOT rush or force completion in one session. Stop at a safe checkpoint, document the exact state, and continue in a later session.

IF SOMETHING FAILS:
Do not repeatedly retry blindly. Diagnose → compare alternatives → use a workaround/indirect approach where possible → rollback to the safe version if necessary → try a better approach.

IF SOMETHING CANNOT BE DONE DIRECTLY:
Do not simply give up. Find the closest technically reliable result achievable with the available tools, technologies, free resources, and constraints.

ALWAYS:
Preserve project intent.
Preserve working versions.
Preserve history.
Do not overwrite unrelated work.
Do not fabricate.
Do not over-engineer.
Do not add unnecessary features.
Do not waste model credits.
Do not introduce paid services without approval.
Do not declare completion without verification.

FINAL PIPELINE:
ANALYZE → LIST → RECOVER → RESEARCH → DECIDE → PLAN → SAFE VERSION → IMPLEMENT → CHECK → FIX → AUDIT → UPDATE → VERIFY → FINISH
```

---

## Three-Layer Project Memory Architecture

Cross-check all three layers before initiating and after completing any task:

```text
LAYER 1 — PRODUCT MEMORY
Previous conversations | Vision | Motivation | Agreed features | Rejected/deferred features | Design decisions | User requirements
        ↓
LAYER 2 — ENGINEERING MEMORY
docs/VERSION_HISTORY.md | docs/EXECUTION_HISTORY.md | ARCHITECTURE.md | PROJECT_STATUS.md | Known issues | Lessons | Warnings | Decisions
        ↓
LAYER 3 — ACTUAL STATE
Git history | Git branches/tags | Current code | Files | Dependencies | Environment | Deployment | Database | Runtime | Production
```

---

## SpeDex Domain & Architectural Boundaries

### 1. Vision, Purpose & Compliance Alignment
- Always preserve SpeDex's identity as a high-frequency smart wallet, financial intelligence platform, and non-custodial financial utility.
- Maintain INR currency note aesthetic standards and typography hierarchy (`Cormorant Garamond` + `Sora`).
- Strictly enforce DPDP Act 2023 & DPDP Rules 2025 compliance across all codebases (verifiable parental consent for minors <18, non-custodial financial disclaimers, granular opt-in/opt-out consent, data minimization, right to access/export, right to erasure, and grievance redressal).

### 2. Architectural Boundaries
- Backend logic must strictly reside in `backend/`. Maintain clean separation between Controllers, Services, and Repositories.
- Web UI components reside in `dashboard_app/src/components/` and views in `dashboard_app/src/views/`. Keep components modular and reusable.
- Mobile cross-platform screens reside in `mobile/src/screens/`. Maintain feature parity with the web dashboard.
- Native Android code resides in `mobile_native_android/app/src/main/java/`.
- Legal policies and markdown disclosures reside in `legal/`.
- Version and execution history reside in `docs/VERSION_HISTORY.md` and `docs/EXECUTION_HISTORY.md`.

### 3. Security, Privacy & Validation Rules
- All backend endpoints must validate user ownership. Always return `403 Forbidden` for IDOR attempts.
- Passwords must always be hashed with BCrypt.
- Pass token authentication headers in all client API calls.
- Never store banking passwords, MPINs, or UPI credentials.
- Minors (<18) must have behavioral tracking and marketing cookies permanently disabled.

### 4. Mandatory 4-Part Verification Standard
Before declaring any task or milestone complete:
1. Run `./mvnw.cmd test` in `backend/` (49/49 tests passing).
2. Run `npm test -- --run` and `npm run build` in `dashboard_app/` (9/9 tests passing, 0 type errors).
3. Run `npm test` and `npx tsc --noEmit` in `mobile/` (7/7 tests passing, 0 type errors).
4. Run `python test-trips-e2e.py` (77/77 tests passing).

---

## Universal Project Engineering Protocols (0–60)

### 0. Core Objective
Execute full lifecycle: `ANALYZE → LIST → RESEARCH → RECOVER CONTEXT → IDENTIFY CURRENT STATE → IDENTIFY RISKS → MAKE DECISIONS → PLAN → PRESERVE SAFE VERSION → IMPLEMENT → TEST → AUDIT → FIX → VERIFY → UPDATE DOCUMENTATION/HISTORY → VERIFY GIT → VERIFY DEPLOYMENT → FINISH`.

### 1. Project Context is the First Source of Truth
Reconstruct product reality before changing code. Recover difference between intended state, current state, historical attempts, and non-negotiables.

### 2. Previous Conversations Must Be Consulted
Recover relevant decisions, constraints, and rejected approaches. Do not reinvent established decisions.

### 3. Complete Git History Protocol
Inspect git status, branches, tags, full relevant git log, commit diffs, and blame. Never overwrite unrelated uncommitted work.

### 4. Version History + Execution History
Treat `docs/VERSION_HISTORY.md` and `docs/EXECUTION_HISTORY.md` as persistent project memory. Read before task; record truthfully after task. Never rewrite history to hide errors.

### 5. Safe Version / Recovery Protocol
Create a recoverable checkpoint (`git commit`, branch, or tag) before any risky modification. If an experiment fails, revert cleanly to the baseline.

### 6. Never Destroy a Working System for a Better Idea
Prefer `WORKING → IMPROVE` over speculative rewrites.

### 7. No-Budget / Free-First Policy
Project budget = 0. Prioritize open-source, local tooling, native browser/platform capabilities, and zero-cost infrastructure.

### 8. Research Before Decision
Consult primary/official documentation. Research only what matters to make a sound decision.

### 9. Decision-Making Framework
Choose the simplest reliable solution satisfying product purpose, UX, security, performance, accessibility, and maintainability.

### 10. Simple-First Engineering
Level 1 (Simplest working) → Level 2 (Correctness & reliability) → Level 3 (Measured optimization) → Level 4 (Maintainability) → Level 5 (Advanced capability only when justified).

### 11. Problem-Solving Protocol
Define problem → Reproduce → Find root cause → Choose smallest reliable fix → Implement → Verify regressions.

### 12. If Something Cannot Be Achieved Directly
Do not say "I can't". Find platform-native workarounds, approximations, fallbacks, or indirect architectures.

### 13. Tool Selection
Select tools and libraries that produce measurable net improvements in speed, reliability, and maintenance.

### 14. Language / Framework Pragmatism
Choose the technology that best solves the problem without unnecessary ecosystem churn.

### 15. Algorithm & Complexity Discipline
Measure before optimizing. Correctness first → identify bottleneck → optimize → measure again.

### 16. Architecture Review
Audit module boundaries, state flow, API boundaries, and coupling. Avoid premature abstraction.

### 17. Feature Implementation
Understand user problem → check existing components → define states (loading, empty, error, success) → implement simple working version → verify → update history.

### 18. UI / UX / Custom Design
Design intentionally for SpeDex (Cormorant Garamond, Sora, INR currency note aesthetic). Avoid generic AI-generated aesthetics, decorative noise, and fake testimonials.

### 19. Responsive Design
Verify desktop, tablet, mobile, and narrow mobile layouts. Check touch targets, forms, and overflow.

### 20. Accessibility
Enforce semantic HTML, keyboard navigation, visible focus indicators, color contrast, and correct ARIA usage.

### 21. Content Quality
Never invent statistics, users, testimonials, or financial claims. Production copy must be authentic.

### 22. SEO & Discoverability
Maintain valid `robots.txt`, `sitemap.xml`, Open Graph tags, JSON-LD structured data, and clean URL routing.

### 23. AI / Machine Discoverability
Maintain clean `llms.txt` and semantic markup accurately representing the project.

### 24. Security
Zero secrets in code. BCrypt for passwords. Enforce least privilege, IDOR ownership checks, input validation, and CSP.

### 25. Privacy
DPDP Act 2023 compliance. Strict minor protection (<18), data minimization, right to erasure, and granular consent.

### 26. Performance
Minimize bundle size, eliminate duplicate dependencies, optimize network waterfalls, and avoid unneeded re-renders.

### 27. Development Artifact Cleanup
Purge debug logs, console errors, test mocks, and temporary flags before production builds.

### 28. Error Handling
All user-facing workflows must handle loading, empty, error, retry, and permission failure states gracefully.

### 29. Testing Standard
Test actual behavior, boundary conditions, and edge cases. Maintain 100% passing test baseline.

### 30. Build Verification
TypeScript typecheck, lint, unit tests, and production build must pass before declaring work complete.

### 31. Git Safety Checks
Inspect `git status` and `git diff`. Never execute destructive git commands (`reset --hard`, `clean -fd`, force push) without explicit justification and safe recovery points.

### 32. Dependency Management
Evaluate size, security, maintenance, and native alternatives before introducing any third-party dependency.

### 33. Vercel / Cloud Frontend Checklist
Inspect build command, output directory, environment variables, rewrites, and runtime errors.

### 34. Render / Cloud Backend Checklist
Verify start command, port binding, environment variables, health checks, and database connections.

### 35. Deployment Discipline
Verify locally first. Inspect deployment logs, check production console/network, and maintain rollback paths.

### 36. Database / Data Safety
Additive migrations over destructive changes. Back up state prior to schema modifications.

### 37. API Design
Enforce validation, ownership checks (`403`), standardized JSON responses, appropriate HTTP status codes, and backward compatibility.

### 38. Code Quality
Prioritize readability, small cohesive modules, explicit data flows, and strong typing. Avoid clever hacks.

### 39. "Vibe-Code" Prevention
No dead buttons, broken links, placeholder copy, fake statistics, or unexplained code.

### 40. Feature Bloat Control
Resist unnecessary features that introduce maintenance overhead without solving core user problems.

### 41. Permitted Autonomous Actions
Refactor, fix bugs, optimize performance, harden security, update documentation, and clean dead code autonomously while preserving product intent.

### 42. Actions Requiring Explicit Approval
Never change core product vision, remove agreed features, expose sensitive data, or introduce paid services without user authorization.

### 43. Token & Resource Efficiency
Targeted searches, focused file reads, and concise reporting. Correctness first, but eliminate wasteful dumps.

### 44. Multi-Session Execution
Large initiatives divide into safe checkpoints. Never leave the repository in a broken state to rush a single turn.

### 45. Resume Protocol
On resume, inspect git status, recent commits, history ledgers, and actual implementation before proceeding.

### 46. Continuous Audit
Classify observed issues: P0 (Critical/Security/Production), P1 (Correctness/UX/Perf), P2 (Improvement), P3 (Polish), P4 (Future). Fix in-scope issues; record others.

### 47. Find What is Missing
Systematically check edge cases, error states, mobile responsiveness, empty states, and test coverage.

### 48. Find What is Over-Applied
Audit for excess dependencies, over-abstraction, and redundant state synchronization. Simplify where beneficial.

### 49. Preserve Product Identity
Honor SpeDex's distinct financial utility identity, design motifs, and user expectations.

### 50. Documentation Integrity
Document architectural decisions, setup steps, rollback procedures, and lessons learned in persistent project files.

### 51. Final Forensic Review
Conduct multi-dimensional audit: Product, Code, Architecture, UI/UX, Security, Privacy, Performance, Testing, Deployment, Git, and Documentation.

### 52. Required Completion Loop
Follow the 32-step execution pipeline from context analysis to verified completion.

### 53. Handling Failures
Document failed attempts, root causes, and lessons learned. Revert cleanly and pursue alternative verified paths.

### 54. Practical Engineering Heuristics
Use binary-search regression hunting, network inspection, git diff, and minimal reproductions.

### 55. No False Confidence
Use strictly truthful status indicators: VERIFIED, PARTIALLY VERIFIED, NOT VERIFIED, BLOCKED, DEFERRED, FAILED.

### 56. User Communication
Concise, structured reporting: WHAT WAS FOUND, WHAT WAS DECIDED, WHAT WAS CHANGED, WHAT WAS VERIFIED, WHAT REMAINS, NEXT SAFE STEP.

### 57. Priority Hierarchy
1. Safety → 2. Security → 3. Data Integrity → 4. User Instruction → 5. Vision/Intent → 6. Architecture/Compatibility → 7. Correctness → 8. Reliability → 9. Maintainability → 10. Performance → 11. Accessibility → 12. UX → 13. Discoverability → 14. Convenience → 15. Polish.

### 58. Golden Rules (1–25)
Understand before changing. Search before creating. Check history before reinventing. Preserve safe versions. Build simple first. Fix root causes. Avoid over-engineering. Zero budget default. Test before declaring completion. Leave recoverable history.

### 59. Default Execution Mode
Always execute: `[CONTEXT RECOVERY] → [AUDIT] → [PROBLEM DEFINITION] → [RESEARCH] → [DECISION] → [SAFE CHECKPOINT] → [IMPLEMENTATION] → [VERIFICATION] → [FORENSIC AUDIT] → [HISTORY UPDATE] → [FINAL STATE]`.

### 60. Final Principle
Deliver verified, resilient, documented, and recoverable software that advances the product's true purpose.

