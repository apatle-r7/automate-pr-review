# 📚 Quick Reference Guide

## 🚀 Project at a Glance

| Aspect | Details |
|--------|---------|
| **Project Name** | Smart PR Automation System |
| **Purpose** | Automated PR validation & dependency management |
| **Language** | Java 17 + Spring Boot 3.5.8 |
| **Build Tool** | Maven (via mvnw) |
| **Core Features** | PR impact analysis + Renovate automation |
| **Workflows** | 2 (pr-automation.yml, renovate.yml) |
| **Auto-Merge** | ✅ Yes (for low-risk changes) |

---

## 📂 File Directory Quick Reference

```
📦 Project Root
├── 📁 .github/workflows/
│   ├── 🔧 pr-automation.yml        ← PR validation workflow (6 jobs)
│   └── 🔧 renovate.yml             ← Dependency update workflow
│
├── 📁 src/
│   ├── main/java/...               ← Spring Boot application
│   └── test/java/...               ← Test files
│
├── 📄 pom.xml                      ← Maven dependencies
├── 📄 renovate.json                ← Renovate configuration
├── 📄 .gitignore                   ← Git ignore rules
│
├── 📖 README.md                    ← Project overview
├── 📖 PROJECT_DOCUMENTATION.md     ← Detailed documentation
└── 📖 ARCHITECTURE_DIAGRAMS.md     ← Visual diagrams
```

---

## 🔑 Key Concepts

### 1. **Impact Classification**
How PRs are categorized for safety:

```
POM_ONLY   📦 → Auto-merge ✅
TRIVIAL    🟢 → Auto-merge ✅
MINOR      🟡 → Review needed ❌
MAJOR      🟠 → Review needed ❌
CRITICAL   🔴 → Review needed ❌
```

### 2. **Two Trigger Paths**

**Path A: Developer Creates PR**
```
Developer PR → Build → Analyze → Auto-merge (if safe)
```

**Path B: Renovate Finds Update**
```
Schedule/Manual → Scan Deps → Create PR → Build → Auto-merge (if safe)
```

### 3. **Auto-Merge Criteria**
```
All of these must be true:
✓ Build passes
✓ Impact level is TRIVIAL or POM_ONLY
✓ No merge conflicts
✓ PR author approved logic
```

---

## 🎬 5-Second Workflows

### Developer Opening PR

```bash
git checkout -b feature/my-change
# Make changes
git commit -m "feat: add new feature"
git push origin feature/my-change
# → Create PR on GitHub
# → pr-automation.yml runs automatically
# → Impact comment posted
# → Labels applied
# → If TRIVIAL + build passes → Auto-merged ✅
```

### Renovate Triggered Manually

```bash
gh workflow run renovate.yml
# → Scans pom.xml & workflows
# → Creates PR for each update
# → Patch/Minor → Auto-merged ✅
# → Major → Waits for review 🔍
```

---

## 🏷️ Important Labels

| Label | Meaning | Color |
|-------|---------|-------|
| `impact-critical` | Breaking/security | 🔴 Red |
| `impact-major` | Significant change | 🟠 Orange |
| `impact-minor` | Small change | 🟡 Yellow |
| `impact-trivial` | Negligible change | 🟢 Green |
| `auto-merge` | Will merge automatically | 🟢 Green |
| `manual-review` | Needs manual review | 🟠 Orange |
| `dependencies` | Dependency update | ⚪ Gray |
| `security` | Security issue | 🔴 Red |
| `urgent` | Urgent fix needed | 🔴 Red |

---

## 🛠️ Common Commands

### Local Development

```bash
# Build project
./mvnw clean package

# Run tests
./mvnw test

# Run application
./mvnw spring-boot:run

# Check Maven version
./mvnw -v
```

### GitHub CLI Operations

```bash
# List open PRs
gh pr list

# View specific PR
gh pr view <number>

# Trigger Renovate manually
gh workflow run renovate.yml

# View workflow runs
gh run list --workflow=renovate.yml

# Merge a PR
gh pr merge <number> --squash
```

### Git Operations

```bash
# Create feature branch
git checkout -b feature/name

# Commit changes
git commit -m "type: description"

# Push to GitHub
git push origin feature/name

# Create PR (after push)
# Use GitHub UI or: gh pr create --fill
```

---

## 🔍 Troubleshooting Quick Fixes

| Problem | Cause | Solution |
|---------|-------|----------|
| Build fails | Code has errors | Fix code locally, push again |
| No PR created | Renovate disabled | Manually trigger: `gh workflow run renovate.yml` |
| Auto-merge didn't happen | Impact not TRIVIAL/POM_ONLY | Manual merge needed |
| Labels not applied | Missing ensure-labels job | Re-run workflow |
| Workflow not running | Branch is not main | Create PR to main branch |

---

## 📊 Workflow Jobs Explained

### pr-automation.yml (6 Jobs)

| # | Job | Duration | Does |
|---|-----|----------|------|
| 1 | **build** | ~30-60s | Compile & test code |
| 2 | **ensure-labels** | ~5s | Create labels if missing |
| 3 | **analyze-impact** | ~10s | Classify PR impact level |
| 4 | **post-review** | ~5s | Post comment with analysis |
| 5 | **auto-merge** | ~10s | Merge if criteria met |
| 6 | **add-labels** | ~5s | Apply impact label |

### renovate.yml (1 Job)

| # | Job | Duration | Does |
|---|-----|----------|------|
| 1 | **renovate** | ~2-5m | Scan deps & create PRs |

---

## 📈 Expected Behavior

### Week 1
✅ Renovate creates PRs for outdated dependencies  
✅ Minor/patch PRs auto-merge  
✅ Major PRs stay open  
✅ Labels appear on PRs  

### Week 2+
✅ Consistent PR creation  
✅ Automatic merges reducing manual work  
✅ Clear impact indicators  
✅ Team gets familiar with system  

---

## 🎓 Learning Path

**Beginner Level:**
1. Read README.md
2. Understand 5-Second Workflows
3. Create sample PR (watch automation)

**Intermediate Level:**
1. Read PROJECT_DOCUMENTATION.md
2. Study ARCHITECTURE_DIAGRAMS.md
3. Modify renovate.json rules

**Advanced Level:**
1. Customize pr-automation.yml jobs
2. Add Snyk/SonarQube integration
3. Create custom labels/rules

---

## 🔗 Important Links

| Resource | Purpose |
|----------|---------|
| [GitHub Actions Docs](https://docs.github.com/en/actions) | Workflow documentation |
| [Renovate Docs](https://docs.renovatebot.com/) | Dependency bot docs |
| [Spring Boot Docs](https://spring.io/projects/spring-boot) | Framework reference |
| [Maven Guide](https://maven.apache.org/guides/) | Build tool docs |
| [Semantic Versioning](https://semver.org/) | Version scheme reference |

---

## ✨ Pro Tips

1. **Use Renovate Dashboard**
   - GitHub Issues → Look for "Dependency Dashboard"
   - Central view of all pending updates

2. **Check Workflow Logs**
   - GitHub → Actions → Select workflow → See logs
   - Helps debug issues

3. **Review Release Notes**
   - Renovate includes release notes in PR body
   - Check before merging major updates manually

4. **Customize Rules**
   - Edit renovate.json to add/remove auto-merge packages
   - Changes apply next scheduled run

5. **Monitor Merge Patterns**
   - Auto-merged PRs = safer/faster delivery
   - Manual review PRs = proper risk assessment

---

## 📞 Support & Help

**Issues with workflow:**
1. Check GitHub Actions → Runs tab for logs
2. Verify pom.xml has no syntax errors
3. Ensure secrets are configured (if needed)
4. Review pr-automation.yml syntax

**Issues with Renovate:**
1. Check if renovate.json is valid JSON
2. Verify package patterns match your dependencies
3. Check Renovate logs in workflow runs
4. Review Renovate documentation

---

## 🎯 Success Checklist

- [ ] Repository has GitHub Actions enabled
- [ ] pr-automation.yml workflow exists and runs
- [ ] renovate.yml workflow exists and runs
- [ ] renovate.json is valid JSON
- [ ] pom.xml builds locally
- [ ] Test PR created and impact comment appears
- [ ] Renovate trigger works manually
- [ ] Auto-merge happens on patch/minor updates
- [ ] Manual review required for major updates
- [ ] Labels applied correctly to PRs

---

## 🚀 Next Steps

1. **Test Everything**
   ```bash
   ./mvnw clean package    # Verify build
   gh workflow run renovate.yml  # Trigger Renovate
   ```

2. **Create Test PR**
   - Make small change
   - Push and create PR
   - Watch automation

3. **Monitor Results**
   - Check dashboard issue
   - Review auto-merged PRs
   - Adjust rules as needed

4. **Team Training**
   - Share documentation
   - Show workflow benefits
   - Establish review practices

---

## 📅 Maintenance Schedule

| Task | Frequency | Action |
|------|-----------|--------|
| Review merged PRs | Weekly | Ensure quality maintained |
| Check Renovate dashboard | Weekly | Plan major updates |
| Update dependencies manually | Monthly | Major versions, security patches |
| Review & update renovate.json | Quarterly | Refine automation rules |
| Check Action versions | Quarterly | Update workflow actions |

---

## 🎉 Benefits Achieved

✅ **Faster Development** - Auto-merged trivial changes  
✅ **Better Security** - Automated vulnerability scanning  
✅ **Reduced Manual Work** - Dependency updates automated  
✅ **Clear Visibility** - Impact analysis on every PR  
✅ **Risk Management** - Major changes require review  
✅ **Consistent Quality** - Build always verified  
✅ **Easy Onboarding** - New developers understand process  

---

*Last Updated: January 12, 2026*
*For detailed info, see PROJECT_DOCUMENTATION.md and ARCHITECTURE_DIAGRAMS.md*
