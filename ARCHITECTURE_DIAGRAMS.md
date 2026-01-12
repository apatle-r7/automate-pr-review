# 🎯 Project Architecture & Flow Diagrams

## 📊 System Overview

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    SMART PR AUTOMATION SYSTEM                           │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐              ┌──────────────┐      ┌──────────────┐  │
│  │  Developer  │              │ Renovate Bot │      │ GitHub Repo  │  │
│  │  Creates PR │              │  Scans Deps  │      │  (main)      │  │
│  └──────┬──────┘              └──────┬───────┘      └──────┬───────┘  │
│         │                             │                      │          │
│         └─────────────────────────────┼──────────────────────┘          │
│                                       │                                 │
│                          ┌────────────▼─────────────┐                  │
│                          │   GitHub Actions         │                  │
│                          │   (pr-automation.yml)    │                  │
│                          └────────────┬─────────────┘                  │
│                                       │                                 │
│         ┌─────────────┬───────────────┼───────────────┬─────────────┐  │
│         │             │               │               │             │  │
│    ┌────▼─────┐  ┌───▼──────┐  ┌────▼──────┐  ┌───▼──────┐  ┌───▼────┐ │
│    │   Build  │  │  Analyze │  │   Review  │  │ Add      │  │ Auto   │ │
│    │   Tests  │  │  Impact  │  │   Comment │  │ Labels   │  │ Merge  │ │
│    └────┬─────┘  └───┬──────┘  └────┬──────┘  └───┬──────┘  └───┬────┘ │
│         │             │              │             │             │      │
│         └─────────────┴──────────────┴─────────────┴─────────────┘      │
│                                       │                                 │
│                          ┌────────────▼─────────────┐                  │
│                          │  ✅ PR Merged            │                  │
│                          │  ❌ PR Blocked           │                  │
│                          │  🔍 PR Under Review      │                  │
│                          └──────────────────────────┘                  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Workflow Execution Path

### Path 1: Developer PR (Minor Code Change)

```
Developer PR
    │
    ├─→ [ 1. BUILD ]
    │   └─→ mvn clean compile test
    │       └─→ ✅ PASSED
    │
    ├─→ [ 2. ENSURE LABELS ]
    │   └─→ Create impact-* labels
    │       └─→ ✅ DONE
    │
    ├─→ [ 3. ANALYZE IMPACT ]
    │   └─→ Files changed: 2 Java files (100 lines)
    │       └─→ Classification: MINOR
    │           └─→ auto_merge = false
    │
    ├─→ [ 4. POST REVIEW ]
    │   └─→ Comment: "Minor changes. Standard code review recommended."
    │       └─→ ✅ POSTED
    │
    ├─→ [ 5. ADD LABELS ]
    │   └─→ Label: impact-minor
    │       └─→ ✅ ADDED
    │
    ├─→ [ 6. AUTO MERGE? ]
    │   └─→ NO (auto_merge = false)
    │
    └─→ 🔍 AWAITING MANUAL REVIEW
```

---

### Path 2: Renovate PR (Patch Update)

```
Renovate Bot Scan
    │
    ├─→ Detect: junit-jupiter 5.14.1 → 5.14.2 (patch)
    │
    ├─→ [ CREATE PR ]
    │   ├─→ Branch: renovate/junit-framework-monorepo
    │   ├─→ Title: "chore(deps): Update junit-jupiter to 5.14.2"
    │   ├─→ Labels: dependencies, impact-patch, auto-merge
    │   └─→ ✅ PR CREATED
    │
    ├─→ [ 1. BUILD ]
    │   └─→ mvn clean compile test
    │       └─→ ✅ PASSED
    │
    ├─→ [ 2. ANALYZE IMPACT ]
    │   └─→ Files changed: pom.xml only
    │       └─→ Classification: POM_ONLY
    │           └─→ auto_merge = true
    │
    ├─→ [ 3. AUTO MERGE? ]
    │   └─→ YES (POM_ONLY + build passed)
    │
    ├─→ [ 4. APPROVE ]
    │   └─→ Comment: "✅ Auto-approved: Trivial changes"
    │
    ├─→ [ 5. MERGE ]
    │   └─→ Merge strategy: squash
    │
    └─→ ✅ MERGED AUTOMATICALLY
```

---

### Path 3: Renovate PR (Major Update)

```
Renovate Bot Scan
    │
    ├─→ Detect: spring-boot 3.5.9 → 4.0.0 (major)
    │
    ├─→ [ CREATE PR ]
    │   ├─→ Branch: renovate/major-spring-boot
    │   ├─→ Title: "chore(deps): Update spring-boot to 4.0.0"
    │   ├─→ Labels: dependencies, impact-major, manual-review, spring
    │   └─→ ✅ PR CREATED
    │
    ├─→ [ 1. BUILD ]
    │   └─→ mvn clean compile test
    │       └─→ ✅ PASSED
    │
    ├─→ [ 2. ANALYZE IMPACT ]
    │   └─→ Files changed: pom.xml only
    │   └─→ BUT: Major version change detected
    │       └─→ Classification: MAJOR
    │           └─→ auto_merge = false
    │
    ├─→ [ 3. POST REVIEW ]
    │   └─→ Comment: "⚠️ Major changes. Thorough review recommended."
    │       └─→ ✅ POSTED
    │
    ├─→ [ 4. AUTO MERGE? ]
    │   └─→ NO (major updates require review)
    │
    └─→ 🔍 AWAITING MANUAL REVIEW
```

---

## 📋 Impact Classification Decision Tree

```
┌─────────────────────────────────────┐
│  Analyzing PR Changes...            │
└──────────────┬──────────────────────┘
               │
        ┌──────▼───────┐
        │ Is POM-only? │
        │ (Only        │
        │ pom.xml)     │
        └──────┬───────┘
               │
        ┌──────┴─────┬───────────┐
        │            │           │
       YES           NO          │
        │            │           │
        │     ┌──────▼─────┐    │
        │     │ Has POM +  │    │
        │     │ other files?    │
        │     └──┬──────┬──┘    │
        │        │      │       │
        │       YES     NO      │
        │        │      │       │
   ┌────▼┐ ┌────▼┐ ┌────▼──────┐
   │     │ │     │ │            │
   │     │ │     │ │ Count Java │
   │     │ │     │ │ files & lines
   │     │ │     │ └───┬────────┘
   │     │ │     │     │
   │     │ │     │  ┌──┴──────────────┐
   │     │ │     │  │                 │
   │     │ │     │  │ >10 files or    │
   │     │ │     │  │ >500 lines?     │
   │     │ │     │  │                 │
   │     │ │  ┌──┴──┴──────┬──────────┐
   │     │ │  │            │          │
   │     │ │  │           YES         NO
   │     │ │  │            │          │
   │     │ │  │       ┌─────▼─┐  ┌───▼──┐
   │     │ │  │       │        │  │      │
   │     │ │  │       │        │  │      │
   │     │ │ ◀┴─┐  ┌──▼─────┐ │ ┌┴───┐┌─┴──────┐
   │     │ │   │  │         │ │ │    ││        │
   │     │ │   │  │ 5-10 or │ │ │<50 ││50-200  │
   │     │ │   │  │ 200-500?│ │ │NO? ││lines?  │
   │     │ │   │  │         │ │ │YES!││        │
   │     │ │   │  └────┬────┘ │ └────┘└────┬───┘
   │     │ │   │       │      │     │       │
   │     │ │   │      YES     NO    │       │
   │     │ │   │       │      │     │       │
   │     │ │   │  ┌────▼──┐  ┌┴────┴──┐   │
   │     │ │   └─►│ MAJOR │  │ MINOR  │   │
   │     │ │      └───┬───┘  └────┬───┘   │
   │     │ │          │           │       │
   │     │ └─────────►┤           │       │
   │     │            │           │       │
   │     │     ┌──────┘           │       │
   │     │     │           ┌──────┘       │
   │     │     │           │              │
   │     │     │      ┌────▼──┐           │
   │     │     │      │ TRIVIAL│          │
   │     │     │      └────┬───┘          │
   │     │     │           │              │
   │ ┌───┴─────┴───┬───────┴──────┐      │
   │ │             │              │      │
   ▼ ▼             ▼              ▼      ▼
  POM_           MAJOR          MINOR   TRIVIAL
  ONLY            🟠            🟡      🟢
  📦             Manual-       Review   Auto-
              Review Only      Rec.    Merge
  ✅            ❌             ❌       ✅
 Auto-          No             No      Auto-
 Merge          Merge          Merge   Merge
```

---

## 🔀 Renovate Workflow Process

```
┌──────────────────────────────────────────────────────────┐
│  RENOVATE WORKFLOW: renovate.yml                         │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  TRIGGER:                                                │
│  • Scheduled: Sunday 1:10pm UTC                          │
│  • Manual: gh workflow run renovate.yml                  │
│                                                          │
│  ┌─────────────────────────────────────────────────────┐ │
│  │ 1. SCAN DEPENDENCIES                                │ │
│  │    ├─ Scan pom.xml                                  │ │
│  │    │  └─ Check Maven Central for newer versions    │ │
│  │    ├─ Scan .github/workflows/*.yml                  │ │
│  │    │  └─ Check GitHub for action updates            │ │
│  │    └─ Scan .mvn/wrapper/maven-wrapper.properties   │ │
│  │       └─ Check Maven wrapper updates                │ │
│  └──────────────────┬──────────────────────────────────┘ │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────────┐ │
│  │ 2. DETECT UPDATES                                   │ │
│  │    └─ Compare versions against package rules        │ │
│  │       • Patch? Minor? Major?                        │ │
│  │       • Should be auto-merged?                      │ │
│  │       • What labels should it get?                  │ │
│  └──────────────────┬──────────────────────────────────┘ │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────────┐ │
│  │ 3. CREATE BRANCHES & PRs                            │ │
│  │    For each update:                                 │ │
│  │    ├─ Create branch: renovate/[package-name]        │ │
│  │    ├─ Update pom.xml or workflow file               │ │
│  │    ├─ Create commit with semantic message           │ │
│  │    ├─ Create PR with:                               │ │
│  │    │  • Title: chore(deps): Update [pkg] to [v]     │ │
│  │    │  • Body: Package table + release notes         │ │
│  │    │  • Labels: based on package rules              │ │
│  │    └─ Push branch to GitHub                         │ │
│  └──────────────────┬──────────────────────────────────┘ │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────────┐ │
│  │ 4. AUTO-MERGE (if configured)                       │ │
│  │    └─ Renovate can merge automatically if:          │ │
│  │       • automerge = true for this package rule      │ │
│  │       • Status checks pass                          │ │
│  │       • No conflicts                                │ │
│  │       • Other conditions met                        │ │
│  └──────────────────┬──────────────────────────────────┘ │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────────┐ │
│  │ 5. RESULTS                                          │ │
│  │    ✅ Minor/Patch updates: Auto-merged              │ │
│  │    🔍 Major updates: Waiting for manual review      │ │
│  │    🔐 Security alerts: Urgent review needed        │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## 🎬 Complete User Journey

### First Time Setup

```
1. GitHub Repo Created
   └─→ Clone repo

2. Install Maven & Java
   └─→ java -version  (should be 17+)
   └─→ ./mvnw -v      (Maven wrapper)

3. Build Locally
   └─→ ./mvnw clean package
   └─→ ✅ Should succeed

4. Enable Actions
   └─→ GitHub Settings → Actions → Enable

5. Configure Secrets (optional)
   └─→ Add SNYK_TOKEN if using Snyk
   └─→ Add SONAR_TOKEN if using SonarQube
```

### Day-to-Day Operations

```
Developer Workflow:
1. Create feature branch
2. Make code changes
3. Push to GitHub
4. Create PR to main
   └─→ pr-automation.yml runs automatically
   └─→ Shows impact analysis + labels
5. Review feedback
6. Address comments
7. Update branch
   └─→ pr-automation.yml runs again
8. PR merged (manual or auto)

Dependency Update Workflow:
1. Sunday 1:10pm UTC (or manual trigger)
2. Renovate scans dependencies
3. Creates PRs for updates
4. PR automation runs on each
5. Patch/Minor: ✅ Auto-merged
6. Major: 🔍 Awaits review
7. Developer reviews when ready
```

---

## 📊 Label Flow

```
┌──────────────────────────────┐
│  PR Created                  │
└────────────┬─────────────────┘
             │
      ┌──────▼─────────────────────────┐
      │ ensure-labels Job              │
      │ Creates missing labels         │
      └──────┬──────────────────────────┘
             │
      ┌──────▼──────────────────────────────────────────┐
      │ analyze-impact Job                             │
      │ Classifies PR as:                              │
      │ • CRITICAL → impact-critical                  │
      │ • MAJOR    → impact-major                     │
      │ • MINOR    → impact-minor                     │
      │ • TRIVIAL  → impact-trivial                   │
      │ • POM_ONLY → impact-trivial                   │
      └──────┬──────────────────────────────────────────┘
             │
      ┌──────▼──────────────────────────────────────────┐
      │ add-labels Job                                 │
      │ Applies label from classify                    │
      └──────┬──────────────────────────────────────────┘
             │
             ▼
    ✅ PR now has visual indicator
       of impact level
```

---

## 🔐 Security & Protection

```
┌─────────────────────────────────────────────────────┐
│  SAFETY MECHANISMS                                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  1. Build MUST Pass                                │
│     └─→ No merge if build fails                    │
│                                                     │
│  2. Impact Analysis                                │
│     └─→ Only auto-merge TRIVIAL/POM_ONLY           │
│                                                     │
│  3. Renovate Limitations                           │
│     ├─→ Max 10 open PRs (prConcurrentLimit)        │
│     ├─→ Max 10 per hour (prHourlyLimit)            │
│     ├─→ Squash merge (cleaner history)             │
│     └─→ Semantic commits                           │
│                                                     │
│  4. Major Version Protection                       │
│     └─→ NEVER auto-merge major updates             │
│                                                     │
│  5. Security Alerts                                │
│     └─→ Get urgent label, never auto-merged        │
│                                                     │
│  6. Branch Protection (Optional)                   │
│     └─→ Can require reviews before merge           │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 📈 Metrics & Monitoring

```
┌──────────────────────────────────────────────────────┐
│  WHAT TO MONITOR                                     │
├──────────────────────────────────────────────────────┤
│                                                      │
│  Pull Requests:                                      │
│  ├─ Total PRs created (by Renovate)                 │
│  ├─ Auto-merged PRs (count)                         │
│  ├─ Manual review PRs                               │
│  └─ Average time to merge                           │
│                                                      │
│  Workflow Performance:                               │
│  ├─ Build success rate                              │
│  ├─ Workflow execution time                         │
│  └─ Job durations                                   │
│                                                      │
│  Dependencies:                                       │
│  ├─ Total dependencies tracked                      │
│  ├─ Outdated packages                               │
│  ├─ Security vulnerabilities                        │
│  └─ Update frequency                                │
│                                                      │
│  Quality:                                            │
│  ├─ Failed builds                                   │
│  ├─ Failed merges                                   │
│  └─ Approval count                                  │
│                                                      │
│  Access: GitHub → Repository → Insights             │
│           or Check Renovate Dashboard issue         │
│                                                      │
└──────────────────────────────────────────────────────┘
```

---

## 🎯 Success Indicators

```
✅ System Working Well When:

1. Renovate PRs appear weekly
   └─→ Shows dependency scanning is active

2. Minor/patch PRs auto-merge
   └─→ Shows safe automation is working

3. Major PRs stay open for review
   └─→ Shows protection is working

4. Labels are applied correctly
   └─→ Shows impact analysis is working

5. Build passes on all PRs
   └─→ Shows code quality is maintained

6. No manual merge needed for trivial changes
   └─→ Shows automation is effective

7. Developer PRs get detailed comments
   └─→ Shows feedback system is working
```

---

*This documentation provides visual representations of how the Smart PR Automation System works.*
