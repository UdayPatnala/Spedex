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

---

# UNIVERSAL VERSION CONTROLLER + CHANGE GOVERNANCE SYSTEM

This project operates under a mandatory, persistent, hierarchical version-control and change-governance system.

This is an **engineering control mechanism**, not an optional documentation practice.

It applies to **EVERY project-changing command, prompt, task, feature, fix, refactor, configuration change, dependency change, architecture change, database change, API change, deployment change, security change, privacy change, documentation change, and automated action.**

The agent MUST follow this system for every applicable operation.

---

# 1. AUTHORITATIVE VERSION FORMAT

The project version MUST use:

`A.BC.DE.F`

Where:

```text
A  = Major Version
BC = Sub-Version / Release Line
DE = Functional Change
F  = Minor Fix / Bug / Error
```

Example:

```text
2.07.14.3
```

Meaning:

```text
Major      = 2
Sub        = 07
Functional = 14
Patch      = 3
```

---

# 2. VERSION HIERARCHY

The hierarchy is:

```text
A
└── BC
    └── DE
        └── F
```

Higher-level changes reset all lower levels.

### Major

```text
A+1.00.00.0
```

### Sub-version

```text
A.BC+1.00.0
```

### Functional

```text
A.BC.DE+1.0
```

### Patch

```text
A.BC.DE.F+1
```

---

# 3. RANGE

```text
A  = 0–∞
BC = 01–99
DE = 01–99
F  = 0–9
```

`00` represents a reset state.

Do not create:

```text
1.05.07.10
```

Instead escalate according to the appropriate change level.

---

# 4. MANDATORY VERSION CONTROLLER

Every project MUST maintain a persistent artifact called the:

# VERSION CONTROLLER

Preferred location:

```text
VERSION_CONTROLLER.md
```

If the project already has an established equivalent such as:

```text
CHANGELOG.md
VERSION_HISTORY.md
RELEASE_HISTORY.md
docs/version-controller.md
```

the agent MUST inspect it first and determine whether it can serve as the authoritative Version Controller.

Do not create duplicate version-history systems unnecessarily.

The Version Controller must contain the project's historical version transitions.

---

# 5. VERSION CONTROLLER IS THE HISTORICAL SOURCE OF TRUTH

The Version Controller records:

```text
Version
Previous Version
Date
Change Level
Git Commit
One-line Change Description
Status
```

Minimum record:

```text
VERSION | PREVIOUS | COMMIT | CHANGE
```

Example:

```text
1.00.00.0 | Initial | a1b2c3d | Initial project baseline
1.00.00.1 | 1.00.00.0 | b2c3d4e | Fixed login validation error
1.00.01.0 | 1.00.00.1 | c3d4e5f | Added password reset workflow
1.01.00.0 | 1.00.01.0 | d4e5f6a | Introduced account management release
```

The description MUST normally be a **single concise line**.

---

# 6. VERSION CONTROLLER STRUCTURE

Use this structure unless the project has a better established equivalent:

```markdown
# VERSION CONTROLLER

## Current Version

`1.00.00.0`

## Current Status

VERIFIED

## Version History

| Version | Previous | Level | Commit | Change |
|---|---|---|---|---|
| 1.00.00.0 | — | BASELINE | abc1234 | Initial project baseline |
| 1.00.00.1 | 1.00.00.0 | PATCH | def5678 | Fixed login validation error |
| 1.00.01.0 | 1.00.00.1 | FUNCTIONAL | ghi9012 | Added password reset workflow |
```

The exact format may be adapted to the project, but the required information MUST remain available.

---

# 7. VERSION CONTROLLER MUST BE VISITED FIRST

This is a mandatory execution rule.

Before executing ANY project-changing prompt:

## STEP 1 — READ VERSION CONTROLLER

The agent MUST first inspect:

```text
VERSION_CONTROLLER.md
```

or the project's authoritative equivalent.

Then determine:

```text
Current version
Recent versions
Recent relevant changes
Related commits
Known previous fixes
Previous architectural decisions
Potential regressions
```

The agent MUST specifically search the Version Controller for changes relevant to the current task.

---

# 8. RELEVANT HISTORY ANALYSIS

Do not merely read the latest version.

Search the history for:

```text
same feature
same module
same component
same bug
same error
same API
same dependency
same architecture
same file
same subsystem
same user workflow
related previous fixes
```

Example:

User asks:

> Fix CineMorph screening session synchronization.

The agent should inspect historical entries containing concepts such as:

```text
CineMorph
screening
session
synchronization
ticket
playback
state
```

before making changes.

---

# 9. WHY HISTORY MUST BE READ FIRST

The agent must avoid:

```text
reintroducing fixed bugs
duplicating previous work
contradicting architecture decisions
reverting previous fixes
creating incompatible implementations
breaking established constraints
changing behavior intentionally preserved by earlier releases
```

Previous version history is an engineering dependency.

Treat it accordingly.

---

# 10. PRE-EXECUTION GATE

Before implementation, the agent MUST establish:

```text
CURRENT VERSION
RELEVANT PREVIOUS CHANGES
CURRENT PROJECT STATE
REQUESTED CHANGE
EXPECTED IMPACT
PROPOSED VERSION LEVEL
```

Internal model:

```text
VERSION CONTROLLER
        ↓
RELEVANT HISTORY
        ↓
CURRENT IMPLEMENTATION
        ↓
REQUEST ANALYSIS
        ↓
CHANGE CLASSIFICATION
        ↓
IMPLEMENTATION
```

Do not skip the Version Controller because the requested change appears small.

---

# 11. CHANGE CLASSIFICATION

Every change MUST be classified as one of:

```text
MAJOR
SUB-VERSION
FUNCTIONAL
PATCH
```

### MAJOR

Use for:

* breaking architecture changes
* incompatible APIs
* incompatible data models
* fundamental platform changes
* major product restructuring
* breaking migrations
* removal of fundamental capabilities

Result:

```text
A+1.00.00.0
```

---

### SUB-VERSION

Use for:

* significant release milestones
* coherent release stages
* substantial product evolution
* completed development phases
* substantial non-breaking project evolution

Result:

```text
A.BC+1.00.0
```

---

### FUNCTIONAL

Use for:

* new features
* meaningful feature changes
* new modules
* new workflows
* new integrations
* meaningful behavioral improvements

Result:

```text
A.BC.DE+1.0
```

---

### PATCH

Use for:

* bug fixes
* minor corrections
* small UI corrections
* validation fixes
* small performance corrections
* typo/documentation corrections where versioning is required
* compatible dependency maintenance

Result:

```text
A.BC.DE.F+1
```

---

# 12. ACTUAL IMPACT OVERRIDES REQUESTED LABEL

The user may say:

> "Just make a small fix."

That does NOT automatically make it a patch.

The agent must classify the actual engineering impact.

Likewise:

> "Increase the version."

does not specify which level.

Determine the correct level from the change.

---

# 13. PRE-CHANGE VERSION CALCULATION

Before implementation determine:

```text
Current:
A.BC.DE.F

Change:
<description>

Classification:
MAJOR / SUB-VERSION / FUNCTIONAL / PATCH

Expected:
A.BC.DE.F
```

Do not arbitrarily change multiple version levels.

---

# 14. MULTIPLE CHANGES IN ONE REQUEST

Break the request into atomic changes.

Example:

```text
Feature A → FUNCTIONAL
Bug B     → PATCH
UI fix C  → PATCH
```

The resulting version follows the highest applicable level:

```text
FUNCTIONAL
```

Therefore:

```text
1.05.12.7
→
1.05.13.0
```

Do not create unnecessary intermediate versions unless the changes are independently released.

---

# 15. IMPLEMENTATION

After historical analysis and classification:

1. Understand the existing implementation.
2. Identify dependencies.
3. Preserve existing behavior unless intentionally changing it.
4. Implement the smallest safe change.
5. Avoid unrelated modifications.
6. Maintain architectural consistency.

---

# 16. VERIFICATION GATE

After implementation, the agent MUST verify the change.

Depending on the project:

```text
Unit tests
Integration tests
E2E tests
Type checking
Lint
Build
Static analysis
Security checks
Performance checks
Migration checks
Manual verification
Deployment checks
```

The version transition is NOT complete until appropriate verification has been performed.

---

# 17. FAILURE RULE

If implementation or verification fails:

DO NOT falsely record the new version as verified.

Instead record the actual state.

Example:

```text
1.05.13.0 — IN DEVELOPMENT
```

rather than claiming:

```text
1.05.13.0 — VERIFIED
```

---

# 18. VERSION UPDATE

After successful verification:

Update the project's authoritative version metadata.

Depending on the project, this may include:

```text
package.json
pyproject.toml
pom.xml
Cargo.toml
application metadata
manifest
build configuration
release metadata
```

Do not update unrelated version locations unnecessarily.

---

# 19. VERSION CONTROLLER UPDATE IS MANDATORY

After successful implementation and verification, the agent MUST update the Version Controller.

This is NOT optional.

The new record MUST contain:

```text
New Version
Previous Version
Change Level
Git Commit
One-line description
Status
```

Example:

```text
| 1.05.13.0 | 1.05.12.7 | FUNCTIONAL | 7ac92ef | Added screening-session synchronization |
```

---

# 20. GIT COMMIT REQUIREMENT

Each recorded released/verified version SHOULD correspond to a Git commit.

Preferred relationship:

```text
Version
↓
Implementation
↓
Verification
↓
Git Commit
↓
Version Controller Entry
```

If Git writes are authorized, create an appropriate commit.

Recommended commit format:

```text
v1.05.13.0: Add screening-session synchronization
```

or:

```text
release: v1.05.13.0
```

The Version Controller MUST contain the resulting commit identifier.

---

# 21. GIT WRITE AUTHORIZATION

Version control does NOT automatically authorize Git writes.

Unless Git modification has been explicitly authorized by the project owner:

```text
git add
git commit
git push
git tag
git merge
git rebase
git reset
branch creation
branch deletion
```

are prohibited.

In READ-ONLY mode:

* inspect Git
* inspect commits
* inspect tags
* inspect branches
* calculate version impact

but do not modify Git.

If a commit is required but Git writes are unauthorized, record:

```text
Commit: PENDING — Git write not authorized
```

Do not fabricate a commit hash.

---

# 22. COMMIT INTEGRITY

Never fabricate:

```text
commit hash
release tag
deployment ID
build ID
test result
version
```

Every recorded identifier must come from the actual project state.

---

# 23. END-OF-COMMAND GOVERNANCE CHECK

Before declaring the task complete, verify:

```text
[ ] Implementation completed
[ ] Relevant tests completed
[ ] Build/typecheck/lint checked where applicable
[ ] Correct version calculated
[ ] Version metadata updated
[ ] Version Controller updated
[ ] Commit recorded if authorized
[ ] Changelog updated if applicable
[ ] No unrelated changes introduced
[ ] Current project state is accurately reported
```

Only then may the agent declare completion.

---

# 24. MANDATORY END-OF-TASK LOGGING

Every project-changing command MUST end with a Version Controller update.

The agent MUST NOT finish the task before attempting this update.

Execution order:

```text
READ VERSION CONTROLLER
        ↓
ANALYSE HISTORY
        ↓
ANALYSE REQUEST
        ↓
CLASSIFY VERSION
        ↓
IMPLEMENT
        ↓
TEST
        ↓
VERIFY
        ↓
UPDATE VERSION
        ↓
COMMIT IF AUTHORIZED
        ↓
UPDATE VERSION CONTROLLER
        ↓
FINAL STATE REPORT
```

---

# 25. IF THE TASK DOES NOT REQUIRE A VERSION CHANGE

Not every operation necessarily requires a version increment.

Examples:

```text
read-only inspection
research
debugging without modification
status checks
log inspection
architecture analysis
```

However, the agent MUST still determine whether the operation changed project state.

If no project state changed:

```text
Version: unchanged
Version Controller: no new version entry required
```

If project state changed:

```text
Version transition required
```

---

# 26. VERSION COLLISION CHECK

Before recording a new version, search:

```text
Version Controller
Git tags
Git history
release metadata
package metadata
deployment records
```

Never reuse an existing version for a different project state.

---

# 27. OVERFLOW RULES

### Patch overflow

```text
1.05.08.9
```

cannot become:

```text
1.05.08.10
```

Escalate appropriately:

```text
1.05.09.0
```

### Functional overflow

```text
1.05.99.9
```

next functional release:

```text
1.06.00.0
```

### Sub-version overflow

```text
1.99.99.9
```

next sub-version:

```text
2.00.00.0
```

unless the project explicitly defines another governance rule.

---

# 28. CHANGE DESCRIPTION RULE

The Version Controller's change description must normally be:

**ONE LINE.**

Good:

```text
Fixed CineMorph screening-session synchronization.
```

Good:

```text
Added local media queue support.
```

Good:

```text
Removed main-thread EBML scanning bottleneck.
```

Bad:

```text
This release includes several changes that were made after extensive investigation...
```

Detailed information belongs in the changelog/release notes, not the Version Controller's one-line ledger.

---

# 29. VERSION CONTROLLER IS NOT THE CHANGELOG

Keep responsibilities separate.

### Version Controller

Answers:

> "What versions existed, when did they change, what commit represents them, and what changed?"

### Changelog

Answers:

> "What exactly changed in this release?"

### Git

Answers:

> "What exact code state produced this version?"

### Tests/build system

Answers:

> "Was this state verified?"

These systems complement each other.

---

# 30. VERSION ↔ COMMIT ↔ CODE STATE

Maintain this invariant:

```text
VERSION
   ↕
VERSION CONTROLLER
   ↕
GIT COMMIT
   ↕
CODE STATE
   ↕
VERIFICATION RESULT
```

A released version must be traceable backward to the exact implementation state.

---

# 31. REVERSE ENGINEERING BEFORE CHANGE

For an unfamiliar project, the agent MUST reverse-engineer the relevant implementation before modifying it.

Inspect:

```text
Project structure
Architecture
Entry points
Relevant modules
Dependencies
Data flow
State management
APIs
Storage
Tests
Build system
Existing version history
Relevant Git commits
```

Do not implement based solely on filenames or assumptions.

---

# 32. REGRESSION PROTECTION

Before changing a component, inspect its historical changes.

If the Version Controller shows previous fixes for the same subsystem:

```text
READ THOSE CHANGES
↓
UNDERSTAND WHY THEY WERE MADE
↓
PRESERVE THEIR INTENT
↓
VERIFY THAT THE NEW CHANGE DOES NOT REINTRODUCE THE OLD BUG
```

Historical fixes are constraints unless deliberately superseded.

---

# 33. DESTRUCTIVE CHANGE PROTECTION

Before:

```text
delete
replace
rewrite
migrate
rename
move
remove
restructure
```

the agent MUST evaluate:

```text
Dependencies
Consumers
Tests
Historical fixes
Backward compatibility
Data impact
Deployment impact
Rollback
```

Do not perform destructive changes merely because they appear cleaner.

---

# 34. SECURITY / PRIVACY / COMPLIANCE CHANGES

Changes involving:

```text
authentication
authorization
personal data
cookies
consent
privacy
security
encryption
secrets
payments
logging
tracking
data retention
```

require explicit impact analysis.

Evaluate:

```text
Security impact
Privacy impact
Compliance impact
Backward compatibility
Migration
Testing
```

Do not classify them solely based on the number of changed lines.

---

# 35. DATABASE / DATA CHANGES

Any persistent-data change requires evaluation of:

```text
Schema
Migration
Existing records
Rollback
Data integrity
Data loss
Backward compatibility
Forward compatibility
Production impact
```

Breaking data changes may require MAJOR version escalation.

---

# 36. API CHANGES

Evaluate:

```text
Request contract
Response contract
Required fields
Optional fields
Status codes
Authentication
Errors
Consumer compatibility
```

Backward-compatible additions can normally be FUNCTIONAL.

Breaking API contracts require MAJOR consideration.

---

# 37. DEPENDENCY CHANGES

Never classify dependency updates solely by the dependency's own semantic version.

Determine the actual project impact.

```text
No meaningful behavioral impact → PATCH

Meaningful behavior/capability impact → FUNCTIONAL

Breaking project impact → MAJOR
```

---

# 38. DEPLOYMENT TRACEABILITY

Every deployed build must be traceable to:

```text
Project Version
Git Commit
Build Artifact
Environment
Configuration
Database Version
Deployment State
Rollback Target
```

Never deploy an unidentified project state.

---

# 39. CURRENT VERSION MUST ALWAYS BE DISCOVERABLE

At any time the agent should be able to answer:

```text
What is the current version?
What changed in the previous release?
What commit represents it?
What relevant historical fixes exist?
What is currently in development?
```

The Version Controller must make these answers discoverable.

---

# 40. FINAL RESPONSE

After completing a project-changing operation, report:

```text
CHANGE COMPLETED

Previous Version:
A.BC.DE.F

New Version:
A.BC.DE.F

Change Level:
MAJOR / SUB-VERSION / FUNCTIONAL / PATCH

Change:
<one-line description>

Verification:
<actual verification result>

Commit:
<actual commit hash OR PENDING/NOT AUTHORIZED>

Version Controller:
UPDATED / NOT UPDATED — <reason>

Status:
VERIFIED / PARTIALLY VERIFIED / BLOCKED

Known Issues:
<if any>
```

Do not claim anything that was not actually performed.

---

# 41. ABSOLUTE MANDATORY RULE

The following rule applies to EVERY project-changing command:

> **THE AGENT MUST VISIT THE AUTHORITATIVE VERSION CONTROLLER BEFORE EXECUTION TO UNDERSTAND RELEVANT PREVIOUS CHANGES, AND MUST UPDATE THE VERSION CONTROLLER AFTER SUCCESSFUL IMPLEMENTATION AND VERIFICATION.**

This rule cannot be skipped because:

* the change is small
* the change is urgent
* the user asks to skip versioning
* the agent believes the change is obvious
* the agent already remembers the previous version
* the change appears unrelated
* the agent has performed a similar task before

The Version Controller must be treated as persistent project memory.

---

# 42. MASTER EXECUTION RULE

For every project-changing command, enforce:

```text
┌─────────────────────────────────────────┐
│ 1. READ VERSION CONTROLLER              │
├─────────────────────────────────────────┤
│ 2. FIND RELEVANT HISTORICAL CHANGES     │
├─────────────────────────────────────────┤
│ 3. INSPECT CURRENT IMPLEMENTATION       │
├─────────────────────────────────────────┤
│ 4. UNDERSTAND USER REQUEST              │
├─────────────────────────────────────────┤
│ 5. CLASSIFY CHANGE                      │
│    MAJOR / BC / DE / F                  │
├─────────────────────────────────────────┤
│ 6. CALCULATE TARGET VERSION             │
├─────────────────────────────────────────┤
│ 7. ANALYSE RISKS / DEPENDENCIES         │
├─────────────────────────────────────────┤
│ 8. IMPLEMENT                            │
├─────────────────────────────────────────┤
│ 9. TEST                                 │
├─────────────────────────────────────────┤
│ 10. VERIFY                              │
├─────────────────────────────────────────┤
│ 11. UPDATE VERSION                      │
├─────────────────────────────────────────┤
│ 12. COMMIT IF AUTHORIZED                │
├─────────────────────────────────────────┤
│ 13. UPDATE VERSION CONTROLLER           │
├─────────────────────────────────────────┤
│ 14. FINAL CONSISTENCY CHECK             │
├─────────────────────────────────────────┤
│ 15. REPORT FINAL STATE                  │
└─────────────────────────────────────────┘
```

---

# 43. GOVERNANCE INVARIANT

The project MUST maintain this invariant:

```text
Every meaningful project state
        ↓
has a version
        ↓
has an explanation
        ↓
has a traceable implementation
        ↓
has verification evidence
        ↓
has a historical record
```

Therefore:

**No unexplained versions.**

**No untraceable releases.**

**No fabricated commits.**

**No silent version changes.**

**No forgotten historical changes.**

**No project-changing command that bypasses the Version Controller.**

---

# 44. UNIVERSAL RULE

Treat this entire system as a permanent engineering rule for the project.

For every future task, automatically synchronize:

```text
USER REQUEST
+
VERSION CONTROLLER
+
CURRENT CODEBASE
+
GIT HISTORY
+
ARCHITECTURE
+
PREVIOUS FIXES
+
CURRENT VERSION
+
TEST STATUS
+
PROJECT CONSTRAINTS
```

before making decisions.

Then synchronize the resulting state back into:

```text
CODE
+
VERSION
+
GIT
+
VERSION CONTROLLER
+
CHANGELOG
+
VERIFICATION STATE
```

after completion.

The Version Controller is therefore both:

**the first checkpoint before change**

and

**the final ledger after change.**

---

# UNIVERSAL MODULAR ARCHITECTURE & CHANGE-ISOLATION GOVERNANCE

This project MUST be designed, organized, maintained, and modified using strict modular architecture and change-isolation principles.

The objective is:

> **Every page, feature, function, component, workflow, service, API, data model, button, interaction, and system capability must have a clearly identifiable ownership boundary so that modifying one thing does not unnecessarily modify, overwrite, delete, or destabilize unrelated parts of the project.**

This architecture applies to:
* Web applications
* Mobile applications
* Desktop applications
* APIs
* Full-stack applications
* SaaS products
* Data/AI applications
* Libraries
* Developer tools
* Multi-product repositories
* Monorepos
* Complex projects with multiple modules

Adapt the exact folder names to the technology, but preserve the architectural principles.

---

# 1. ARCHITECTURE IS A SYSTEM OF BOUNDARIES

Do NOT organize the project only according to file type:

```text
components/
utils/
services/
pages/
```

This often creates large shared folders where unrelated functionality becomes tightly coupled.

Prefer **domain/feature ownership**.

The primary question must be:

> "Which part of the system owns this behavior?"

rather than:

> "What type of file is this?"

---

# 2. ARCHITECTURE HIERARCHY

Use the following conceptual hierarchy:

```text
PROJECT
│
├── APP / SHELL
│
├── DOMAINS
│   ├── DOMAIN A
│   │   ├── PAGE
│   │   ├── FEATURE
│   │   ├── COMPONENT
│   │   ├── FUNCTION
│   │   ├── SERVICE
│   │   ├── DATA
│   │   └── TESTS
│   │
│   └── DOMAIN B
│       └── ...
│
├── SHARED
│
├── INFRASTRUCTURE
│
├── CONFIGURATION
│
├── TESTING
│
└── DOCUMENTATION
```

The exact implementation depends on the technology.

---

# 3. FIRST PRINCIPLE — OWNERSHIP

Every meaningful piece of code must have an owner.

Examples:

```text
Login Page
→ Authentication domain

Search button
→ Search feature

Playback controller
→ Playback feature

Payment API
→ Payments domain

User profile state
→ Profile domain

Notification delivery
→ Notifications domain
```

Do not allow functionality to become "orphan code" whose ownership is unclear.

---

# 4. SECOND PRINCIPLE — LOCALITY

Code that changes together should live together.

If a feature requires:

```text
UI
state
business logic
API calls
validation
types
tests
```

prefer keeping those pieces within the feature's boundary rather than scattering them across unrelated global directories.

Example:

```text
features/
└── search/
    ├── components/
    ├── hooks/
    ├── services/
    ├── state/
    ├── validation/
    ├── types/
    └── tests/
```

This makes the feature independently understandable and maintainable.

---

# 5. THIRD PRINCIPLE — CHANGE ISOLATION

When the user asks:

> "Fix the search button."

the agent MUST NOT automatically modify:

```text
authentication
payments
profile
database
navigation
unrelated UI
```

unless dependency analysis proves those areas are affected.

The default assumption is:

> **Change the smallest possible ownership boundary.**

---

# 6. CHANGE RADIUS

Before modifying anything, calculate the expected change radius.

Classify files as:

```text
DIRECT
RELATED
DEPENDENT
SHARED
UNRELATED
```

### DIRECT

Files that implement the requested functionality.

### RELATED

Files directly supporting the functionality.

### DEPENDENT

Files consuming the functionality.

### SHARED

Infrastructure used by multiple modules.

### UNRELATED

Everything else.

Only DIRECT and necessary RELATED files should normally be modified.

DEPENDENT files require explicit impact analysis.

SHARED files require additional caution.

UNRELATED files MUST NOT be modified.

---

# 7. ARCHITECTURAL OWNERSHIP MAP

Maintain an ownership map.

Example:

```text
Authentication
├── LoginPage
├── RegisterPage
├── AuthForm
├── authService
├── authStore
├── authValidation
└── authTests

Profile
├── ProfilePage
├── ProfileEditor
├── profileService
├── profileStore
├── profileValidation
└── profileTests

Search
├── SearchPage
├── SearchInput
├── SearchResults
├── searchService
├── searchState
├── searchValidation
└── searchTests
```

This should be documented so an agent can determine where a change belongs.

---

# 8. PAGE OWNERSHIP

Every application page/screen must have a defined ownership boundary.

Example:

```text
pages/
├── home/
├── login/
├── dashboard/
├── settings/
└── profile/
```

But pages should not become giant containers.

A page should primarily compose:

```text
Page
↓
Sections
↓
Features
↓
Components
```

Business logic should generally live below the page layer.

---

# 9. PAGE STRUCTURE

A complex page should be decomposed:

```text
Dashboard
│
├── Header
├── Navigation
├── SummarySection
├── AnalyticsSection
├── ActivitySection
└── Footer
```

If a section becomes independently meaningful:

```text
sections/
└── analytics/
```

If it has behavior:

```text
features/
└── analytics/
```

Do not allow pages to become 1,000+ line monoliths unless there is a strong reason.

---

# 10. FEATURE OWNERSHIP

Each major feature should have its own boundary.

Example:

```text
features/
├── authentication/
├── search/
├── notifications/
├── payments/
├── media-player/
└── settings/
```

A feature should contain its own:

```text
components
logic
state
services
validation
types
tests
```

where appropriate.

---

# 11. FUNCTION OWNERSHIP

Functions must have clear ownership.

Avoid:

```text
utils/
└── everything.ts
```

or:

```text
helpers/
└── common.ts
```

containing unrelated business logic.

Instead:

```text
features/
└── search/
    ├── searchQuery.ts
    ├── normalizeSearchQuery.ts
    └── ranking.ts
```

A function belongs close to the domain that owns its behavior.

---

# 12. BUTTON OWNERSHIP

Buttons are UI manifestations of actions.

Do not create a huge global button-handler file:

```text
buttonHandlers.ts
```

Instead:

```text
SearchButton
→ Search feature

LoginButton
→ Authentication feature

SaveProfileButton
→ Profile feature

PlayButton
→ Playback feature
```

The UI component may be shared, but its action logic belongs to the feature that owns the action.

---

# 13. COMPONENT OWNERSHIP

Distinguish between:

### Domain-specific components

```text
features/search/components/SearchResults.tsx
```

and:

### Truly shared components

```text
shared/ui/Button.tsx
shared/ui/Modal.tsx
shared/ui/Input.tsx
```

Do NOT put a component in `shared` simply because two components currently use it.

Promote something to shared infrastructure only when there is a genuine reusable contract.

---

# 14. SHARED CODE RULE

Shared code is high-risk code.

Because multiple features depend on it:

```text
Shared
   ↓
Feature A
Feature B
Feature C
```

Therefore:

> **Changing shared code requires dependency analysis and regression verification across its consumers.**

Do not casually modify shared infrastructure to solve a local feature problem.

Prefer adapting the feature locally when practical.

---

# 15. DEPENDENCY DIRECTION

Use controlled dependency direction.

Preferred:

```text
Application
↓
Domain
↓
Feature
↓
Shared infrastructure
```

Avoid uncontrolled:

```text
Feature A ↔ Feature B
Feature B ↔ Feature C
Feature C ↔ Feature A
```

Circular dependencies are architectural warning signs.

---

# 16. FEATURE-TO-FEATURE COMMUNICATION

Features should communicate through explicit contracts.

Prefer:

```text
Feature A
   ↓
public interface / service / event / contract
   ↓
Feature B
```

Avoid importing another feature's internal implementation.

Bad:

```text
featureA/
  imports:
featureB/internal/privateState
```

Better:

```text
featureB/
└── index.ts
```

exports only the intended public API.

---

# 17. PUBLIC VS PRIVATE MODULES

Every feature should conceptually have:

```text
PUBLIC
PRIVATE
```

Example:

```text
features/search/
├── index.ts          ← public interface
├── components/
├── services/
├── state/
├── internal/
└── tests/
```

Other features should consume:

```text
features/search
```

rather than:

```text
features/search/internal/someFile
```

This protects internal implementation from accidental coupling.

---

# 18. CONTRACTS

For important modules define explicit contracts.

Examples:

```text
API contracts
Component props
Service interfaces
State interfaces
Data schemas
Events
Database interfaces
Repository interfaces
```

A change to an internal implementation should not require unrelated consumers to change if the contract remains stable.

---

# 19. DATA OWNERSHIP

Every important piece of state/data must have an owner.

Examples:

```text
Authentication state
→ Authentication

Playback state
→ Playback

Profile state
→ Profile

Cart state
→ Commerce
```

Avoid duplicate sources of truth.

Prefer:

```text
ONE OWNER
↓
controlled access
↓
consumers
```

rather than:

```text
same state duplicated in five components
```

---

# 20. SINGLE SOURCE OF TRUTH

For every critical piece of information determine:

```text
Where is it created?
Where is it stored?
Who owns it?
Who can modify it?
Who can read it?
How is it synchronized?
```

Examples:

```text
Version
User profile
Authentication state
Membership
Application configuration
Feature flags
Database schema
```

Do not maintain conflicting copies without a deliberate synchronization mechanism.

---

# 21. CONFIGURATION ISOLATION

Separate:

```text
Development
Testing
Production
```

configuration.

Do not scatter environment-specific values throughout feature code.

Prefer centralized configuration with controlled access.

---

# 22. API ISOLATION

Organize APIs by domain.

Example:

```text
api/
├── auth/
├── users/
├── search/
├── media/
├── payments/
└── notifications/
```

Avoid one giant:

```text
api.ts
```

containing every endpoint.

---

# 23. DATABASE ISOLATION

Organize persistence by domain where appropriate.

Example:

```text
data/
├── auth/
├── users/
├── media/
├── payments/
└── notifications/
```

Database models, repositories, queries, and migrations should have clear ownership.

---

# 24. TEST OWNERSHIP

Tests should follow the architecture.

Example:

```text
features/
└── search/
    ├── components/
    ├── services/
    ├── state/
    └── tests/
        ├── search.unit.test.ts
        ├── search.integration.test.ts
        └── search.e2e.test.ts
```

When a feature changes, its tests should be immediately discoverable.

---

# 25. TEST PROXIMITY

Where practical:

> **Tests should live close to the code they protect.**

This reduces the risk of modifying code without noticing its test coverage.

---

# 26. PAGE → FEATURE → COMPONENT → FUNCTION

Use a traceable relationship:

```text
PAGE
 ↓
SECTION
 ↓
FEATURE
 ↓
COMPONENT
 ↓
ACTION
 ↓
FUNCTION
 ↓
SERVICE
 ↓
DATA
```

An agent should be able to trace a user interaction through the entire system.

Example:

```text
ProfilePage
 ↓
ProfileEditor
 ↓
SaveProfileButton
 ↓
saveProfile()
 ↓
profileService.update()
 ↓
Profile API
 ↓
Database
```

---

# 27. TRACEABILITY

For every important capability, maintain a trace:

```text
User Action
→ UI
→ Feature
→ Logic
→ Service
→ API
→ Data
→ Result
```

This makes debugging and controlled modification significantly safer.

---

# 28. CHANGE MANIFEST

Before modifying a project, create an internal change manifest.

Example:

```text
CHANGE MANIFEST

Requested:
Fix profile save button.

Direct:
- ProfileEditor
- SaveProfileButton
- profileService

Related:
- profile validation

Dependent:
- ProfilePage

Shared:
- Button component

Unrelated:
- Authentication
- Search
- Payments

Allowed modification radius:
Direct + required Related
```

The agent should use this manifest to prevent scope creep.

---

# 29. MODIFICATION BOUNDARY

Before editing a file ask:

> "Why does this file need to change?"

Every changed file must have a traceable reason.

If there is no reason:

**Do not modify it.**

---

# 30. NO CASCADE EDITING

Do not make changes like:

```text
"While I'm here, I'll clean this file."
```

or:

```text
"This architecture could be better, so I'll restructure everything."
```

unless the user explicitly requested refactoring or the change is technically necessary.

Unrelated cleanup increases regression risk.

---

# 31. PRESERVATION RULE

When fixing something:

> **Preserve all unrelated functionality.**

Do not:

* delete working code
* replace complete files unnecessarily
* rewrite unrelated modules
* remove unknown code without analysis
* simplify by deleting behavior
* replace working implementations merely for style

If replacement is necessary:

1. Understand the old implementation.
2. Identify all behaviors it provides.
3. Preserve required behavior.
4. Migrate consumers.
5. Test before removing the old implementation.

---

# 32. FILE REPLACEMENT PROTECTION

Never overwrite an entire file simply to change one small section when a targeted modification is possible.

Prefer:

```text
small targeted modification
```

over:

```text
complete file rewrite
```

unless a rewrite is genuinely justified.

---

# 33. UNKNOWN CODE PROTECTION

If the agent encounters code it does not understand:

Do NOT immediately delete it.

Classify it:

```text
KNOWN
RELATED
UNKNOWN
OBSOLETE
DUPLICATE
DEAD
```

Only remove code after establishing sufficient evidence that it is obsolete or harmful.

---

# 34. DEAD CODE RULE

Do not label code as dead merely because it is not referenced from an obvious location.

Check:

```text
Dynamic imports
Routes
Reflection
Configuration
Plugins
Build scripts
API consumers
Tests
External consumers
Runtime registration
```

before deletion.

---

# 35. LARGE FILE DETECTION

Continuously detect architectural hotspots.

Warning thresholds may include:

```text
Very large page
Very large component
Very large service
Very large state module
Very large utility file
Too many responsibilities
Too many imports
Too many dependencies
```

Do not split files purely based on line count.

Split according to **responsibility and ownership**.

---

# 36. SINGLE RESPONSIBILITY

Each module should have a clear responsibility.

Bad:

```text
Dashboard.tsx
```

containing:

```text
UI
API
database logic
authentication
analytics
notifications
formatting
routing
```

Prefer:

```text
Dashboard
├── UI composition
├── analytics feature
├── notification feature
├── data service
└── auth service
```

---

# 37. DOMAIN BOUNDARIES

Identify the project's major domains before restructuring.

Examples:

```text
Authentication
Users
Content
Media
Commerce
Payments
Notifications
Analytics
Administration
Settings
```

Do not force every project into these names.

Discover the project's actual domains.

---

# 38. PRODUCT / MODULE BOUNDARIES

For projects containing multiple products or major subsystems:

```text
products/
├── product-a/
├── product-b/
└── product-c/
```

Each product should own its product-specific implementation.

Shared infrastructure belongs outside product-specific boundaries.

---

# 39. MONOREPO RULE

For monorepos, separate:

```text
apps/
├── web/
├── admin/
└── mobile/

packages/
├── ui/
├── auth/
├── types/
└── config/

services/
├── api/
├── worker/
└── processing/
```

Do not allow arbitrary cross-package imports.

---

# 40. ROUTING OWNERSHIP

Routes should have clear ownership.

Example:

```text
routes/
├── auth/
├── dashboard/
├── profile/
└── settings/
```

A feature should not secretly register routes belonging to another domain.

---

# 41. STATE OWNERSHIP

Separate:

```text
Local UI state
Feature state
Domain state
Global application state
Server state
Persistent state
```

Do not put everything into a global store.

Global state should be reserved for genuinely global concerns.

---

# 42. EVENT OWNERSHIP

Events must have clear producers and consumers.

Document:

```text
Event
Producer
Payload
Consumers
Purpose
```

Avoid invisible side effects.

---

# 43. SIDE-EFFECT ISOLATION

Keep external side effects in controlled boundaries.

Examples:

```text
API calls
Database writes
File system
Browser storage
Notifications
Analytics
External SDKs
```

UI components should not unnecessarily contain low-level side-effect logic.

---

# 44. ERROR OWNERSHIP

Errors should be handled at the appropriate layer.

Example:

```text
API error
→ service layer

validation error
→ validation/domain layer

rendering error
→ UI boundary

unexpected application failure
→ application error boundary
```

Do not scatter arbitrary error handling everywhere.

---

# 45. SHARED UTILITIES GOVERNANCE

Before adding a utility to `shared/utils` ask:

```text
Is it truly generic?
Does it have multiple legitimate consumers?
Does it contain domain-specific assumptions?
Could it belong to a feature instead?
```

If it is domain-specific:

**keep it inside the domain.**

---

# 46. ARCHITECTURE DOCUMENTATION

Maintain an architecture document:

```text
ARCHITECTURE.md
```

It should describe:

```text
System overview
Architecture layers
Domains
Modules
Ownership
Dependency rules
Data flow
State ownership
API boundaries
Shared infrastructure
Testing strategy
Deployment structure
Important constraints
```

Do not document imaginary architecture.

The document must reflect the actual repository.

---

# 47. MODULE REGISTRY

Maintain a lightweight registry when the project becomes sufficiently complex.

Example:

```text
MODULE REGISTRY

Authentication
Owner: features/auth
Public API: auth/index
Dependencies: shared/ui, api/client

Search
Owner: features/search
Public API: search/index
Dependencies: api/client

Profile
Owner: features/profile
Public API: profile/index
Dependencies: api/client, shared/ui
```

This allows agents to locate ownership quickly.

---

# 48. DEPENDENCY MAP

For complex projects maintain:

```text
ARCHITECTURE.md
```

or:

```text
DEPENDENCY_MAP.md
```

showing major relationships.

Example:

```text
App Shell
 ├── Auth
 ├── Profile
 ├── Search
 └── Media

Search
 ├── API Client
 └── Shared UI

Profile
 ├── API Client
 └── Shared UI
```

---

# 49. CHANGE IMPACT ANALYSIS

Before modifying a shared module:

Determine:

```text
Who imports it?
Who calls it?
Who depends on its types?
Which pages use it?
Which features use it?
Which tests protect it?
```

Then calculate the regression surface.

---

# 50. CHANGE IMPACT LEVELS

Classify impact:

```text
ISOLATED
LOCAL
CROSS-FEATURE
SHARED
SYSTEM-WIDE
```

### ISOLATED

One function/component.

### LOCAL

One feature/page.

### CROSS-FEATURE

Multiple domains.

### SHARED

Shared infrastructure.

### SYSTEM-WIDE

Architecture, configuration, database, authentication, deployment, etc.

The larger the impact, the larger the verification requirement.

---

# 51. ARCHITECTURAL FREEZE DURING LOCAL FIXES

When fixing a local issue:

Do not perform unrelated architectural restructuring.

Example:

```text
Task:
Fix search button.

Allowed:
Search button
Search feature
Necessary search dependencies

Not automatically allowed:
Rewrite application architecture
Replace state management
Reorganize unrelated pages
Upgrade framework
```

Architecture changes require explicit justification.

---

# 52. SAFE REFACTORING

When refactoring:

```text
OLD IMPLEMENTATION
↓
UNDERSTAND
↓
DEFINE CONTRACT
↓
CREATE NEW IMPLEMENTATION
↓
MIGRATE CONSUMERS
↓
RUN TESTS
↓
COMPARE BEHAVIOR
↓
REMOVE OLD IMPLEMENTATION
```

Never delete the old implementation first and hope the replacement works.

---

# 53. BACKWARD COMPATIBILITY

When modifying a public module:

Determine:

```text
Who uses it?
What contract do they expect?
Can the change remain backward-compatible?
```

Prefer additive changes when practical.

---

# 54. FEATURE FLAGS

For risky features, consider:

```text
feature flag
```

when appropriate.

This allows:

```text
code deployment
≠
feature activation
```

and reduces deployment risk.

---

# 55. MIGRATION STRATEGY

For major architectural changes:

Prefer incremental migration:

```text
OLD
 ↓
BRIDGE
 ↓
NEW
 ↓
MIGRATE
 ↓
VERIFY
 ↓
REMOVE OLD
```

rather than:

```text
DELETE OLD
 ↓
WRITE EVERYTHING AGAIN
```

---

# 56. ROLLBACK AWARENESS

Every significant change should have a rollback strategy.

Determine:

```text
What changed?
How can it be reverted?
What data changed?
Can database changes be reversed?
Can deployment be rolled back?
Can feature flags disable it?
```

---

# 57. BACKUP / RECOVERY

Before destructive operations:

Ensure there is a recoverable state through appropriate mechanisms such as:

```text
Git
backup
migration rollback
database snapshot
deployment rollback
```

Do not rely solely on memory.

---

# 58. VERSION CONTROLLER INTEGRATION

This architecture system must work together with the project's Version Controller.

Before a structural change:

```text
READ VERSION CONTROLLER
↓
READ ARCHITECTURE
↓
READ RELEVANT HISTORY
↓
CALCULATE CHANGE RADIUS
↓
IMPLEMENT
↓
VERIFY
↓
UPDATE VERSION
↓
UPDATE VERSION CONTROLLER
↓
UPDATE ARCHITECTURE DOCUMENTATION
```

---

# 59. ARCHITECTURE CHANGES MUST BE VERSIONED

If architecture itself changes materially:

Record:

```text
Old architecture
New architecture
Reason
Affected modules
Migration
Version
Commit
Verification
```

Do not silently restructure the project.

---

# 60. AUTOMATIC ARCHITECTURE SYNCHRONIZATION

After significant structural changes, verify:

```text
Actual repository structure
        =
ARCHITECTURE.md
        =
Module Registry
        =
Dependency assumptions
```

If documentation becomes stale, update it.

---

# 61. NO PHANTOM FILES

Never document files, modules, services, or components that do not actually exist.

Architecture documentation must reflect reality.

---

# 62. NO ORPHAN FILES

Periodically identify files that appear to have no owner.

For each:

```text
Determine purpose
Find references
Determine whether runtime-loaded
Determine whether obsolete
Assign ownership
or remove only with evidence
```

---

# 63. NO DUPLICATE IMPLEMENTATIONS

Before implementing a new function/feature:

Search the project for existing implementations.

Ask:

```text
Does this already exist?
Is there a related implementation?
Can it be reused?
Should it be extended?
Would a duplicate create conflicting sources of truth?
```

Do not create duplicate systems unnecessarily.

---

# 64. DISCOVER BEFORE CREATE

Before creating:

```text
component
function
service
hook
API
utility
state
page
feature
```

search the existing project.

Prefer:

```text
reuse
extend
refactor safely
```

over:

```text
duplicate
```

---

# 65. FILE NAMING

Use predictable names.

Examples:

```text
SearchPage
SearchResults
searchService
searchStore
searchTypes
searchValidation
```

Avoid meaningless names:

```text
helper2
newComponent
finalComponent
temp
misc
utils2
```

---

# 66. NO TEMPORARY PRODUCTION CODE

Do not leave:

```text
TODO hacks
temporary bypasses
debug code
console logging
dead experiments
unused imports
placeholder implementations
```

in production code unless explicitly documented and intentional.

---

# 67. DEBUGGING ISOLATION

When debugging:

1. Reproduce the issue.
2. Locate the owning module.
3. Trace the execution path.
4. Identify root cause.
5. Modify the smallest responsible boundary.
6. Verify the original issue.
7. Verify adjacent behavior.

Do not randomly modify multiple modules until the problem disappears.

---

# 68. ROOT-CAUSE RULE

Fix the root cause where practical.

Do not mask symptoms with unrelated changes.

Example:

Bad:

```text
API returns invalid data
→ modify five UI components
```

Better:

```text
API returns invalid data
→ identify API/data contract problem
→ fix responsible layer
→ verify consumers
```

---

# 69. PRESERVE FUNCTIONALITY DURING RESTRUCTURING

When restructuring:

Create an explicit behavior inventory.

Record:

```text
Existing feature
Existing interaction
Existing state
Existing API
Existing edge case
Existing validation
Existing error behavior
Existing persistence
```

Then verify that the new architecture preserves the required behavior.

---

# 70. "NOTHING DISAPPEARS" RULE

When modifying a project:

> **No existing functionality may disappear merely because its implementation was moved, refactored, or rewritten.**

Before removing anything determine:

```text
What does it do?
Who uses it?
Is its behavior still represented?
Where did that behavior move?
Is its test still present?
```

---

# 71. FILE CHANGE REPORT

After implementation, internally determine:

```text
ADDED
MODIFIED
DELETED
MOVED
```

For each changed file identify the reason.

If an unexpected unrelated file changed:

Investigate before completion.

---

# 72. FINAL CHANGE-SCOPE AUDIT

Before completing a task:

```text
Requested change:
...

Files directly changed:
...

Files related:
...

Unexpected files:
...

Deleted files:
...

Moved files:
...

Unrelated changes:
...
```

Any unexplained modification is a warning.

---

# 73. FINAL ARCHITECTURE CHECK

Before completion verify:

```text
[ ] Ownership is clear
[ ] Dependencies are understood
[ ] No unrelated modules changed
[ ] No functionality disappeared
[ ] No duplicate implementation introduced
[ ] Shared code changes were evaluated
[ ] Tests updated where necessary
[ ] Architecture documentation remains accurate
[ ] Version Controller updated
[ ] Version correctly reflects architectural impact
```

---

# 74. MASTER WORKFLOW

For every meaningful project task:

```text
┌───────────────────────────────────────────┐
│ 1. READ VERSION CONTROLLER                │
├───────────────────────────────────────────┤
│ 2. READ ARCHITECTURE                      │
├───────────────────────────────────────────┤
│ 3. SEARCH PROJECT                         │
├───────────────────────────────────────────┤
│ 4. FIND EXISTING IMPLEMENTATION           │
├───────────────────────────────────────────┤
│ 5. IDENTIFY OWNER                         │
├───────────────────────────────────────────┤
│ 6. TRACE DEPENDENCIES                     │
├───────────────────────────────────────────┤
│ 7. CREATE CHANGE MANIFEST                 │
├───────────────────────────────────────────┤
│ 8. CLASSIFY CHANGE RADIUS                 │
├───────────────────────────────────────────┤
│ 9. DETERMINE VERSION IMPACT               │
├───────────────────────────────────────────┤
│ 10. IMPLEMENT SMALLEST SAFE CHANGE       │
├───────────────────────────────────────────┤
│ 11. TEST                                  │
├───────────────────────────────────────────┤
│ 12. VERIFY NO FUNCTIONALITY DISAPPEARED  │
├───────────────────────────────────────────┤
│ 13. VERIFY CHANGE RADIUS                  │
├───────────────────────────────────────────┤
│ 14. UPDATE VERSION                        │
├───────────────────────────────────────────┤
│ 15. UPDATE ARCHITECTURE IF REQUIRED       │
├───────────────────────────────────────────┤
│ 16. UPDATE VERSION CONTROLLER             │
├───────────────────────────────────────────┤
│ 17. COMMIT IF AUTHORIZED                  │
├───────────────────────────────────────────┤
│ 18. FINAL CONSISTENCY CHECK               │
└───────────────────────────────────────────┘
```

---

# 75. ABSOLUTE RULE

For every future command:

> **DO NOT MODIFY CODE UNTIL YOU KNOW WHERE THAT CODE BELONGS, WHO OWNS IT, WHAT DEPENDS ON IT, WHAT IT DEPENDS ON, WHAT PREVIOUS CHANGES AFFECTED IT, AND WHAT THE MINIMUM SAFE CHANGE BOUNDARY IS.**

And after modification:

> **DO NOT COMPLETE THE TASK UNTIL YOU HAVE VERIFIED THAT THE REQUESTED CHANGE WORKS, UNRELATED FUNCTIONALITY HAS BEEN PRESERVED, THE ARCHITECTURE REMAINS CONSISTENT, AND THE VERSION/CHANGE HISTORY HAS BEEN UPDATED.**

---

# 76. PRIMARY OBJECTIVE

The architecture must make future development behave like controlled surgery rather than uncontrolled rewriting.

The desired result is:

```text
REQUEST
   ↓
IDENTIFY OWNER
   ↓
IDENTIFY DEPENDENCIES
   ↓
ISOLATE CHANGE
   ↓
MODIFY ONLY NECESSARY CODE
   ↓
VERIFY
   ↓
PRESERVE EVERYTHING ELSE
```

The project should become progressively:

```text
more modular
more traceable
more testable
more replaceable
more understandable
more maintainable
less coupled
less fragile
less dependent on individual agent memory
```

The architecture exists primarily to protect the project from accidental destruction during future development.

