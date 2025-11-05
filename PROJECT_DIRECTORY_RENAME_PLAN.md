# DocumentVault Project Directory Renaming Plan

## 🎯 **Current Issue**
The root project directory is still named `StudyLibrary` while we've successfully rebranded to DocumentVault and renamed all packages to `com.documentvault.*`. This creates an inconsistency in the project structure.

## 📁 **Current vs Target Structure**

### **Current State**
```
/Users/wahidulazam/projects/StudyLibrary/  ❌ (Old name)
├── src/main/java/com/documentvault/       ✅ (Updated)
├── pom.xml (documentvault)                ✅ (Updated)
├── README.md (DocumentVault)              ✅ (Updated)
└── All other files                        ✅ (Updated)
```

### **Target State**
```
/Users/wahidulazam/projects/DocumentVault/  🎯 (New name)
├── src/main/java/com/documentvault/        ✅ (Already done)
├── pom.xml (documentvault)                 ✅ (Already done)
├── README.md (DocumentVault)               ✅ (Already done)
└── All other files                         ✅ (Already done)
```

---

## 🚀 **Renaming Strategy**

### **Option 1: Simple Directory Rename (Recommended)**
**Pros**: 
- Simple and straightforward
- Preserves all Git history and branches
- No configuration changes needed
- Quick to execute

**Cons**:
- Need to update any local development environment paths
- IDE workspace settings may need updating

### **Option 2: Git Clone to New Directory**
**Pros**: 
- Clean approach
- Can verify everything works before switching

**Cons**:
- More complex process
- Need to handle Git remotes carefully

---

## 📋 **Step-by-Step Implementation (Option 1)**

### **Step 1: Prepare for Rename**
```bash
# Navigate to parent directory
cd /Users/wahidulazam/projects/

# Verify current project directory
ls -la StudyLibrary/

# Check Git status to ensure clean state
cd StudyLibrary && git status
```

### **Step 2: Execute Directory Rename**
```bash
# Go back to parent directory
cd /Users/wahidulazam/projects/

# Rename the directory
mv StudyLibrary DocumentVault

# Verify the rename
ls -la DocumentVault/
```

### **Step 3: Update Development Environment**
```bash
# Navigate to new directory
cd DocumentVault

# Verify Git repository is intact
git status
git log --oneline -5

# Verify compilation still works
mvn clean compile
mvn test
```

### **Step 4: Update IDE and Local Settings**
- **VS Code**: Update workspace settings and recent folders
- **IntelliJ/Eclipse**: Update project location in IDE
- **Terminal**: Update bookmarks and shortcuts
- **Scripts**: Update any build or deployment scripts that reference the old path

---

## ⚠️ **Important Considerations**

### **Before Renaming**
- [ ] **Commit all changes**: Ensure clean Git state
- [ ] **Close IDE**: Close VS Code and any other IDEs
- [ ] **Note the path**: Record current path for reference
- [ ] **Check running processes**: Stop any running Maven/Node processes

### **After Renaming**
- [ ] **Verify Git**: Check that Git history is intact
- [ ] **Test build**: Run `mvn clean compile test` 
- [ ] **Update IDE**: Reopen project in IDE with new path
- [ ] **Update shortcuts**: Update terminal shortcuts and bookmarks
- [ ] **Verify remote**: Check Git remote is still correct

### **Potential Issues & Solutions**

| Issue | Solution |
|-------|----------|
| **Git history lost** | Git history is preserved in directory renames |
| **IDE can't find project** | Reopen project using "Open Folder" with new path |
| **Terminal shortcuts broken** | Update terminal profiles and bookmarks |
| **Build scripts fail** | Update any hardcoded paths in scripts |
| **VS Code workspace issues** | Update `.vscode/settings.json` if needed |

---

## 🎯 **Success Criteria**

### **Functional Verification**
- [ ] Git repository works normally (`git status`, `git log`)
- [ ] Maven build succeeds (`mvn clean compile`)
- [ ] All tests pass (`mvn test`) 
- [ ] Application launches (`mvn javafx:run`)
- [ ] IDE opens project correctly

### **Consistency Check**
- [ ] Directory name: `DocumentVault` ✅
- [ ] Package structure: `com.documentvault.*` ✅ 
- [ ] Maven coordinates: `com.documentvault:documentvault:2.0.0` ✅
- [ ] Application name: DocumentVault ✅
- [ ] All branding: DocumentVault ✅

---

## 📝 **Alternative: Repository Rename**

If you also want to rename the **GitHub repository** from "STUDYLIBRARY" to "DocumentVault":

### **GitHub Repository Rename**
1. Go to repository Settings on GitHub
2. Scroll to "Repository name" 
3. Change from "STUDYLIBRARY" to "DocumentVault"
4. Update local Git remote:
   ```bash
   git remote set-url origin https://github.com/bmsujon/DocumentVault.git
   ```

### **Branch Names**
Consider renaming branches for consistency:
- `main` → keep as is
- `develop` → keep as is (already descriptive)

---

## ⏱️ **Execution Time Estimate**
- **Directory rename**: 1 minute
- **Verification steps**: 5 minutes  
- **IDE/environment updates**: 5 minutes
- **Total estimated time**: ~10 minutes

---

## 🎉 **Expected Result**

After completion, you'll have:

```
/Users/wahidulazam/projects/DocumentVault/  ✅ Perfect naming consistency
├── Full DocumentVault branding             ✅ 
├── Package: com.documentvault.*            ✅
├── Maven: documentvault:2.0.0              ✅
├── Git history preserved                   ✅
└── All functionality working               ✅
```

**Complete brand and structural consistency achieved!** 🔒📚✨

---

**Ready to proceed with directory rename?**