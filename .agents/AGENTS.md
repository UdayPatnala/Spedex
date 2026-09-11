# UNIVERSAL PRODUCT REVERSE-ENGINEERING + ENGINEERING SYSTEM: SPEDEX

## Master Operational Paradigm: The Three Realities

```text
1. WHAT WE INTENDED
   ↓
   motive / vision / conversations / decisions

2. WHAT WE ACTUALLY BUILT
   ↓
   reverse engineering / code / files / runtime / deployment / git

3. WHAT WE SHOULD DO NEXT
   ↓
   gap analysis / correction / simplification / implementation
```

---

## Architectural Evolution Reconstruction Model

```text
              ┌──────────────────────┐
              │   ALL CONVERSATIONS  │
              └──────────┬───────────┘
                         ↓
              ┌──────────────────────┐
              │ MOTIVE + VISION      │
              │ PURPOSE + IDENTITY   │
              └──────────┬───────────┘
                         ↓
              ┌──────────────────────┐
              │ PREVIOUS DECISIONS   │
              └──────────┬───────────┘
                         │
                         │
     ┌───────────────────┴───────────────────┐
     │                                       │
     ↓                                       ↓
┌───────────────┐                    ┌────────────────┐
│ REVERSE       │                    │ GIT + HISTORY  │
│ ENGINEER      │◄──────────────────►│ RECONSTRUCTION │
│ CURRENT CODE  │                    └────────────────┘
└───────┬───────┘
        ↓
┌─────────────────────────────┐
│ CURRENT PRODUCT MODEL       │
│                             │
│ what exists                 │
│ what works                  │
│ what doesn't                │
│ how it works                │
│ why it probably works       │
│ architecture               │
│ UX                          │
│ data                        │
│ deployment                  │
└─────────────┬───────────────┘
              ↓
       ┌──────────────┐
       │ RECONCILE    │
       └──────┬───────┘
              ↓
 ┌─────────────────────────────┐
 │ INTENDED   ↔   ACTUAL       │
 │                             │
 │ missing                     │
 │ incorrect                   │
 │ overbuilt                   │
 │ broken                      │
 │ drifted                     │
 │ risky                       │
 └─────────────┬───────────────┘
               ↓
        RESEARCH + DECISION
               ↓
          SAFE VERSION
               ↓
         IMPLEMENTATION
               ↓
          VERIFICATION
               ↓
        HISTORY UPDATE
```

> **THE FOUR FOUNDATIONAL AXIOMS**:
> 1. **Conversation history** tells us what was wanted.
> 2. **Reverse engineering** tells us what actually exists.
> 3. **Git** tells us how it got there.
> 4. **Comparison & reconciliation** tells us what needs to happen next.
>
> **NON-NEGOTIABLE RULE**:
> **Never let the current codebase overwrite the project's historical product intent. Reconcile the code with the intent.**

---

## Absolute Order of Operations (0–29)

### 0. Absolute Order of Operations
`NEVER START BY CODING.`
`A. RECOVER PRODUCT INTENT → B. REVERSE ENGINEER CURRENT PRODUCT → C. RECONCILE INTENT VS IMPLEMENTATION → D. IDENTIFY GAPS/EXCESS/RISKS/DRIFT → E. RESEARCH → F. MAKE DECISIONS → G. PLAN → H. PRESERVE SAFE VERSION → I. IMPLEMENT → J. VERIFY → K. AUDIT → L. UPDATE HISTORY → M. PRESERVE NEW KNOWN-GOOD VERSION`.

### 1. Product Intent Recovery
Recover original motive, product purpose, vision, intended users, intended visual language, agreed/rejected/deferred features, previous architecture decisions, and explicit prohibitions.

### 2. Reverse Engineer What Has Actually Been Built
Inspect before changing:
- **2.1 Project Structure**: Directories, packages, modules, configurations, assets, scripts.
- **2.2 Technology Stack**: Actual runtime dependencies vs manifest claims.
- **2.3 Application Architecture**: Entry points, routing, server/client boundaries, state flow.
- **2.4 User Experience Flow**: Trace real user journeys (start $\rightarrow$ action $\rightarrow$ UI $\rightarrow$ state $\rightarrow$ API $\rightarrow$ data $\rightarrow$ result).
- **2.5 UI Reverse Engineering**: Component hierarchy, typography, colors, responsive behavior, dead/fake/placeholder UI.
- **2.6 Data Model**: Schemas, entities, relationships, persistence, lifecycle, deletions.
- **2.7 Dependency Graph**: Usage, dead dependencies, bloat, security risks.
- **2.8 Performance Model**: Bundles, waterfalls, re-renders, memory, timers.

### 3. Git-Based Reverse Engineering
Use Git as a historical reconstruction engine. Trace how the project evolved, what was added/removed/reverted, and where regressions originated.

### 4. Documentation Reverse Engineering
Compare `README.md`, `VERSION_HISTORY.md`, `EXECUTION_HISTORY.md`, `ARCHITECTURE.md`, `PROJECT_STATUS.md` against actual code. Identify documented-but-not-implemented and implemented-but-not-documented drift.

### 5. Build the Current-State Model
Categorize reality: What exists, what works, what partially works, what is broken, what is dead, what is fragile, what is over-engineered.

### 6. Reconcile Three States
- **State A vs State B** $\rightarrow$ Product Drift Analysis.
- **State B vs State C** $\rightarrow$ Implementation Impact Analysis.
- **State A vs State C** $\rightarrow$ Product Alignment Analysis.

### 7. Product Drift Analysis
Audit for feature creep, generic UI, lost functionality, wrong terminology, or temporary code made permanent.

### 8. Gap Analysis
Determine: Missing, Incorrect, Unnecessary, Broken, Risky, and Unknown areas.

### 9. Feature Forensics
Why does it exist? When was it introduced? What problem does it solve? What files implement it? Does it match the vision? Should it remain?

### 10. Research
Research only after intent and implementation are understood. Answer: What is possible? What is appropriate? What fits *this* product?

### 11. Decision Making
Motive + Vision + Intent + Current Implementation + Constraints $\rightarrow$ Simplest intent-aligned path.

### 12. Simple Working Model First
Build a working baseline first, verify it, preserve it, and only then optimize.

### 13. Safe Version Protocol
Preserve recoverable checkpoints (`git commit`, branch, tag) before any risky modification.

### 14. Implementation
Incremental, targeted changes. Prefer surgical refactoring over speculative rewrites.

### 15. If Direct Solution Fails
Diagnose why. Find alternative architectures, platform-native capabilities, compatibility layers, or approximations.

### 16. Free-First Constraint
Zero-budget baseline. Leverage open-source, local tooling, native browser/platform capabilities.

### 17. Engineering Quality
Evaluate correctness, algorithmic complexity, maintainability, accessibility, testability, and security.

### 18. UI / Product Authenticity
Enforce SpeDex currency note aesthetic (`Cormorant Garamond` + `Sora`, emerald `#0d2818` / gold `#d4af37` / warm sand `#fdfbf7`). No generic AI templates or decorative noise.

### 19. Security + Privacy
DPDP Act 2023 compliance. Minor protection (<18), zero credential storage, BCrypt password hashing, and strict IDOR tenant isolation (`403 Forbidden`).

### 20. Testing + Verification Standard
1. Backend: `./mvnw.cmd test` (49/49 passing).
2. Dashboard: `npm test -- --run` & `npm run build` (9/9 passing, 0 build errors).
3. Mobile: `npm test` & `npx tsc --noEmit` (7/7 passing, 0 type errors).
4. Root E2E: `python test-trips-e2e.py` (77/77 passing).

### 21. Cloud & Hosting Verification
Inspect build commands, output configurations, environment variables, and runtime logs for Vercel/Render.

### 22. Git Safety
Clean status checks, meaningful commit messages, no accidental files, zero committed secrets.

### 23. Model / Token Efficiency
Targeted searches, focused file reads, structural audits. Correctness over raw speed.

### 24. Multi-Session Continuity
Preserve complete state, history logs, and rollback baselines across sessions.

### 25. Version & Execution History
Maintain `docs/VERSION_HISTORY.md` and `docs/EXECUTION_HISTORY.md` as permanent, immutable ledgers.

### 26. Final Forensic Audit
Multi-dimensional review: Product Intent, Code Quality, Architecture, UI/UX, Security, Privacy, Performance, Testing, Deployment, Git, Documentation.

### 27. Continuous Discovery & Classification
Classify findings: P0 (Critical/Security/Data Loss), P1 (Correctness/UX/Perf), P2 (Valuable Improvement), P3 (Polish), P4 (Future Roadmap).

### 28. Final Execution Loop
`RECOVER CONVERSATIONS → RECOVER MOTIVE → RECOVER VISION → RECOVER DECISIONS → REVERSE ENGINEER CURRENT PRODUCT → INSPECT CODE/ARCHITECTURE → INSPECT RUNTIME/UX/DATA/DEPLOYMENT → INSPECT COMPLETE GIT HISTORY → INSPECT VERSION/EXECUTION HISTORY → COMPARE INTENDED VS ACTUAL → IDENTIFY DRIFT → IDENTIFY GAPS → RESEARCH → DECIDE → PLAN → PRESERVE SAFE VERSION → BUILD SIMPLE WORKING MODEL → VERIFY → REFINE → TEST → SECURITY/PRIVACY CHECK → PERF CHECK → UX CHECK → DEPLOYMENT CHECK → GIT DIFF CHECK → UPDATE HISTORY → PRESERVE KNOWN-GOOD → FINAL PRODUCT-VISION CHECK → FINISH`.

### 29. Non-Negotiable Core Principles
1. Understand the motive before the feature.
2. Understand the vision before the implementation.
3. Reverse engineer the existing product before changing it.
4. Treat conversations as accumulated product knowledge.
5. Treat Git as historical evidence.
6. Treat code as evidence of actual implementation, not proof of intent.
7. Reconcile intent, history, and implementation.
8. Search before creating.
9. Preserve known-good versions.
10. Build simple working versions before advanced ones.
11. Fix root causes.
12. Prefer reversible changes.
13. Do not overwrite unrelated work.
14. Do not invent product facts.
15. Do not fabricate completion.
16. Do not over-engineer.
17. Do not feature-bloat.
18. Do not introduce costs without approval.
19. Use model/tool resources efficiently.
20. Large work may continue across sessions.
21. Maintain project history after every meaningful task.
22. Always leave the project in a recoverable state.
23. When direct implementation fails, investigate alternatives.
24. Preserve product identity through every technical change.
25. The ultimate question is: *"Does the resulting product move closer to the product we originally intended, while becoming more correct, maintainable, secure, efficient, and usable?"*
