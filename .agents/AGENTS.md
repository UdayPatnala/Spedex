# UNIVERSAL PRODUCT-FIRST ENGINEERING SYSTEM: SPEDEX

## Master Operational Philosophy: Product Intent Recovery First

```text
                 PREVIOUS CHATS & DISCUSSIONS
                              │
               ┌──────────────┼──────────────┐
               ↓              ↓              ↓
             MOTIVE         VISION       DECISIONS
               │              │              │
               └──────────────┼──────────────┘
                              ↓
                      PRODUCT DEFINITION
                              │
                  ┌───────────┼───────────┐
                  ↓           ↓           ↓
               WHAT IT      WHAT IT      WHAT IT
               MUST BE      CAN BE       MUST NOT BE
                  │           │           │
                  └───────────┼───────────┘
                              ↓
                      CURRENT PROJECT
                              │
                      ┌───────┴───────┐
                      ↓               ↓
                  GIT HISTORY      FILES/CODE
                      │               │
                      └───────┬───────┘
                              ↓
                        GAP ANALYSIS
                              ↓
                  MISSING / CORRECT / EXTRA
                              ↓
                        DECISION MAKING
                              ↓
                        IMPLEMENTATION
```

> **NON-NEGOTIABLE RULE**:
> **Never let the current codebase overwrite the project's historical product intent. Reconcile the code with the intent.**
>
> The code is only one representation of the product—it may be incomplete, outdated, overbuilt, or drifted. The product intent accumulated across conversations, motives, decisions, and lessons is the foundational truth.

---

## Core Product-First Hierarchy

```text
CONVERSATIONS / DISCUSSIONS
        ↓
MOTIVE
        ↓
VISION
        ↓
PURPOSE
        ↓
PRODUCT IDENTITY
        ↓
INTENDED USER EXPERIENCE
        ↓
AGREED FEATURES + BEHAVIOR
        ↓
CONSTRAINTS + "DO NOT" DECISIONS
        ↓
CURRENT IMPLEMENTATION
        ↓
GIT / VERSION HISTORY
        ↓
RESEARCH
        ↓
ENGINEERING DECISIONS
        ↓
IMPLEMENTATION
        ↓
VERIFICATION
        ↓
UPDATED PRODUCT
```

---

## Four-Layer Project Memory Architecture

Cross-check all four layers before initiating and after completing any task:

```text
LAYER 1 — PRODUCT INTENT & CONVERSATIONAL MEMORY
Previous chats | Motive | Vision | Purpose | Intended UX | Agreed features | Rejected/deferred ideas | Prohibitions
        ↓
LAYER 2 — ENGINEERING MEMORY
docs/VERSION_HISTORY.md | docs/EXECUTION_HISTORY.md | docs/ARCHITECTURE.md | docs/PROJECT_STATUS.md | Lessons | Decisions
        ↓
LAYER 3 — HISTORICAL SOURCE OF TRUTH
Git commits | Complete git log | Branches | Tags | Prior reverts | Abandoned approaches | Known-good baselines
        ↓
LAYER 4 — ACTUAL RUNTIME STATE
Working tree | Current code | Dependencies | Build configuration | Test suites | Deployment | Runtime behavior
```

---

## Product Intent Invariants: SpeDex

### 1. Product Motive & Vision
- **Why SpeDex Exists**: To deliver high-frequency personal expense tracking, automated multi-session trip ledgers, and financial velocity analytics without invasive telemetry or custodial security liabilities.
- **Fintech Philosophy**: Non-custodial utility. SpeDex never acts as a bank or custodial intermediary; it provides personal financial intelligence and friction-free tracking.

### 2. Product Identity & Design Language
- **Indian Rupee (INR) Aesthetic**: Curated banknote palette (Deep Emerald Note Green `#0d2818`/`#164223`, Accent Gold `#d4af37`, Warm Sand Base `#fdfbf7`).
- **Typography**: `Cormorant Garamond` (classic elegance, currency heritage for headings) + `Sora` (crisp modern clarity for data, numbers, tables, and controls).
- **Anti-Vibe-Code Mandate**: Zero dead buttons, placeholder copy, fake metrics, or unlinked controls. Real rupee symbols (`₹`) and authentic velocity analytics.

### 3. Legal & Regulatory Non-Negotiables
- **DPDP Act 2023 & DPDP Rules 2025 Compliance**:
  - Minors (<18 years): Verifiable parental consent mandatory; behavioral tracking and targeted profiling permanently disabled.
  - Consent Management: Granular opt-in/opt-out across functional, analytics, and marketing categories.
  - User Rights: Full machine-readable data export (`GET /api/privacy/export`), irreversible account erasure (`POST /api/privacy/erase`), and grievance redressal SLA tracking.

### 4. Security & Tenant Isolation
- **IDOR Prevention**: All queries strictly filtered by authenticated `user_id`. Unauthorized cross-tenant access returns `403 Forbidden`.
- **Zero Credential Storage**: Never store banking passwords, UPI MPINs, or debit/credit card credentials.
- **Stateless Authentication**: Passwords hashed with BCrypt; all client requests authenticated via JWT Bearer tokens.

### 5. Mandatory 4-Part Verification Standard
Before declaring any task, feature, or refactor complete:
1. **Backend**: `./mvnw.cmd test` in `backend/` (49/49 tests passing).
2. **Dashboard**: `npm test -- --run` and `npm run build` in `dashboard_app/` (9/9 tests passing, 0 build/type errors).
3. **Mobile**: `npm test` and `npx tsc --noEmit` in `mobile/` (7/7 tests passing, 0 type errors).
4. **Root E2E**: `python test-trips-e2e.py` (77/77 tests passing).

---

## Universal Product-First Engineering Protocols (1–23)

### 1. Product Intent is the Primary Context
Reconstruct the project's intended identity from all available previous conversations and project discussions before modifying code. Extract motive, vision, intended feel, terminology, accepted tradeoffs, and explicit prohibitions.

### 2. Build a Product Intent Model
Construct an internal model: Motive, Purpose, Vision, Identity, Principles, User Experience, Functional Model, Visual Model, Technical Model, Constraint Model, and Future Model.

### 3. Conversations Are Not Just Reference Material
Search conversations to discover features discussed but not yet implemented, features intentionally removed, design references, and reasons for architectural decisions. Never assume "not present in code = not required" or "present in code = intended."

### 4. Distinguish Intent from Implementation
For every feature, compare intended vs current state. If implementation conflicts with product vision, do not blindly preserve the code; investigate and restore alignment with intent.

### 5. Product Drift Audit
Regularly audit whether the project has drifted into generic UI, feature creep, excessive complexity, or unaligned architecture. Trace drift origins and recommend corrective simplification.

### 6. Missing vs Over-Applied
Continuously evaluate both directions: What is missing (critical states, error handling, accessibility, performance, intent)? What is over-applied (excessive decoration, unneeded dependencies, premature abstraction, redundant features)?

### 7. Preserve the Product's Uniqueness
Never normalize the project into a generic SaaS template. Design and interaction patterns must naturally express this specific product's motive.

### 8. Previous Agreements Have Weight
Explicit past decisions (required, rejected, deferred, prohibited) must not be silently reversed without explicit user direction or newly discovered technical blockers.

### 9. Motive Over Feature Count
A feature is justified only if it strengthens the product's reason for existing. If removing a feature makes the product clearer, simpler, and more aligned, recommend removal.

### 10. Vision-Preserving Engineering
Technical decisions (frameworks, libraries, schemas, caching, storage) must serve the product vision. Do not adopt technology for novelty and then force the product to fit it.

### 11. Research the Product, Not Just the Technology
Answer both: "What is the best technical approach?" and "Is this actually the right thing for this product?" Technical excellence applied to the wrong product requirement is waste.

### 12. Vision-First Decision Process
1. Recover motive and vision $\rightarrow$ 2. Recover past agreements $\rightarrow$ 3. Understand task $\rightarrow$ 4. Inspect current code and git history $\rightarrow$ 5. Identify gaps and drift $\rightarrow$ 6. Select simplest intent-aligned approach $\rightarrow$ 7. Create safe checkpoint $\rightarrow$ 8. Implement $\rightarrow$ 9. Verify $\rightarrow$ 10. Audit against vision $\rightarrow$ 11. Record history $\rightarrow$ 12. Preserve known-good state.

### 13. Before Every Feature
Ask: Why does it exist? What user problem does it solve? Was it agreed or rejected? Does it fit the vision? What is the simplest correct implementation? How can it be reversed?

### 14. Before Every Bug Fix
Ask: What should the product do? What does it do now? Did an older implementation work correctly? Is this a symptom of architectural drift? Will the fix preserve intended UX?

### 15. Before Every UI Change
Recover visual vision, UX hierarchy, and previous design decisions. Improve UI only when it strengthens alignment with the product's intended experience.

### 16. Before Every Architectural Change
Understand why the current architecture exists and what problem the new architecture solves. Ensure simplicity, maintainability, and clear rollback paths.

### 17. Complete Project Memory Synthesis
Cross-check all four sources: Previous Conversations, Project Documentation/History, Git History, and Runtime Implementation. Discrepancies between sources reveal critical engineering facts.

### 18. Safe Version / Recovery Baseline
Before any risky modification, ensure a recoverable Git checkpoint exists. Implement the simplest working solution first, verify it, preserve it, and only then optimize.

### 19. "Make It Better" Does Not Mean "Add More"
Evaluate all four paths: Add, Remove, Simplify, Restructure. The most impactful improvement is frequently simplification or removal of unnecessary complexity.

### 20. Final Product-Intent Check
Before declaring completion, ask: "If someone who participated in all original discussions inspected this result, would they recognize it as the product we intended?" Technical correctness and product alignment are both mandatory.

### 21. Universal Execution Pipeline
`PRODUCT CONTEXT → MOTIVE → VISION → PURPOSE → IDENTITY → PREVIOUS DECISIONS → CURRENT STATE → GIT HISTORY → DOCUMENTATION → GAPS → OVER-APPLICATION → RESEARCH → OPTIONS → DECISION → SAFE VERSION → SIMPLE WORKING IMPLEMENTATION → TEST → DEBUG → REFINE → SECURITY → PRIVACY → PERFORMANCE → ACCESSIBILITY → UX/UI → DEPLOYMENT → GIT VERIFICATION → PRODUCT-VISION COMPARISON → HISTORY UPDATE → PRESERVE KNOWN-GOOD VERSION → FINISH`.

### 22. Most Important Rule
**The product's motive and vision must survive every technical change.** Never allow a library, framework, refactor, optimization, or workaround to silently change what the product is supposed to be.

### 23. Final Operating Principle
Do not think: "What code should I write?"
Think: "What was this product intended to become? What is preventing it from reaching that state? What is the safest, simplest, most efficient way to close that gap?"
