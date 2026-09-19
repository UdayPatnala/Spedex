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
