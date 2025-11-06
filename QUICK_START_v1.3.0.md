# DocumentVault v1.3.0 - Quick Start

**Version:** 1.3.0-SNAPSHOT  
**Target:** February 2026 (12 weeks)  
**Scope:** Original v1.2.0 features (security deferred to v1.4.0)

---

## 🎯 **What We're Building**

### **The Originally Planned v1.2.0 Features:**

✅ **Dark Mode** - Professional dark/light themes  
✅ **Settings** - Comprehensive preferences dialog  
✅ **Advanced Search** - Operators, regex, saved queries  
✅ **Enhanced Categories** - Colors and icons  
✅ **Table Improvements** - Resizable columns, density modes  
✅ **Export** - JSON, CSV, Markdown  
✅ **Import** - Validation and conflict resolution  
✅ **Backup & Restore** - Automated with integrity checks

### **NOT in v1.3.0 (Deferred to v1.4.0):**

❌ Encryption  
❌ Password protection  
❌ Audit logging  
❌ Document classification

---

## 📅 **12-Week Timeline**

```
Week 1:     🎨 Dark Mode Theme System
Week 2:     ⚙️ Settings & Preferences
Week 3:     📋 Table View Enhancements
Week 4:     🔍 Advanced Search System
Week 5:     🏷️ Enhanced Categories
Week 6:     📤 Export System
Week 7:     📥 Import System
Week 8:     💾 Backup & Restore
Week 9-10:  ✅ Integration Testing
Week 11:    📚 Documentation
Week 12:    🚀 Release Prep
```

---

## 🚀 **Getting Started - Week 1**

### **Monday: Dark Theme CSS**

**Create the dark theme:**
```bash
touch src/main/resources/css/dark-theme.css
```

**Add this starter content:**
```css
/* DocumentVault Dark Theme */
.root {
    -fx-base: #1e1e1e;
    -fx-background: #252526;
    -fx-control-inner-background: #2d2d30;
    -fx-accent: #007acc;
    -fx-text-fill: #cccccc;
}

.table-view {
    -fx-background-color: #252526;
}

.table-row-cell:odd {
    -fx-background-color: #2d2d30;
}

.table-row-cell:even {
    -fx-background-color: #252526;
}
```

**Task Checklist:**
- [ ] Create dark-theme.css
- [ ] Test with existing UI
- [ ] Refine light-theme.css
- [ ] Verify contrast ratios

---

### **Tuesday-Wednesday: Theme Manager**

**Create the package:**
```bash
mkdir -p src/main/java/com/documentvault/theme
touch src/main/java/com/documentvault/theme/ThemeManager.java
```

**Implementation starter:**
```java
package com.documentvault.theme;

import javafx.scene.Scene;

public class ThemeManager {
    private static final ThemeManager INSTANCE = new ThemeManager();
    
    public enum Theme {
        LIGHT("light-theme.css"),
        DARK("dark-theme.css");
        
        private final String cssFile;
        Theme(String cssFile) { this.cssFile = cssFile; }
        public String getCssFile() { return cssFile; }
    }
    
    private Theme currentTheme = Theme.LIGHT;
    
    public static ThemeManager getInstance() {
        return INSTANCE;
    }
    
    public void applyTheme(Scene scene, Theme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
            getClass().getResource("/css/" + theme.getCssFile()).toExternalForm()
        );
        currentTheme = theme;
        saveThemePreference(theme);
    }
    
    private void saveThemePreference(Theme theme) {
        // TODO: Save to settings
    }
}
```

**Task Checklist:**
- [ ] Create ThemeManager class
- [ ] Implement theme switching
- [ ] Add fade transition
- [ ] Test with main window

---

### **Thursday: Theme UI**

**Add menu item to MainView.fxml:**
```xml
<Menu text="View">
    <MenuItem text="Toggle Theme" accelerator="Shortcut+Shift+T" 
              onAction="#handleToggleTheme"/>
</Menu>
```

**Add to MainController.java:**
```java
@FXML
private void handleToggleTheme() {
    ThemeManager tm = ThemeManager.getInstance();
    Theme newTheme = tm.getCurrentTheme() == Theme.LIGHT ? Theme.DARK : Theme.LIGHT;
    tm.applyTheme(stage.getScene(), newTheme);
}
```

**Task Checklist:**
- [ ] Add menu items
- [ ] Add keyboard shortcut
- [ ] Test switching
- [ ] Verify persistence

---

### **Friday: Week 1 Wrap-up**

**Testing Checklist:**
- [ ] All UI components styled in both themes
- [ ] Theme switch feels instant (< 200ms)
- [ ] Theme preference saved and loaded
- [ ] No visual glitches

**Commit & Push:**
```bash
git add src/main/resources/css/dark-theme.css
git add src/main/java/com/documentvault/theme/
git add src/main/java/com/documentvault/controller/MainController.java
git commit -m "feat: add dark mode theme system

- Created professional dark theme CSS
- Implemented ThemeManager singleton
- Added theme switcher to View menu
- Added Cmd/Ctrl+Shift+T keyboard shortcut
- Theme preference persisted in settings"
git push origin develop
```

---

## 📦 **Dependencies Needed**

**Week 6 (Export System):**
```xml
<!-- CSV Export -->
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.9</version>
</dependency>
```

**That's it!** No encryption, PDF, or Markdown parsing libs needed.

---

## 📊 **Progress Tracking**

### **Sprint 1 Checklist (Weeks 1-3)**

**Week 1: Dark Mode** ⬜
- [ ] Dark theme CSS complete
- [ ] Light theme refined
- [ ] ThemeManager implemented
- [ ] UI integration done
- [ ] Testing complete

**Week 2: Settings** ⬜
- [ ] Settings framework done
- [ ] Settings dialog created
- [ ] All tabs implemented
- [ ] Persistence working
- [ ] Integration complete

**Week 3: Table** ⬜
- [ ] Column resize/reorder done
- [ ] Density modes implemented
- [ ] Visual polish complete
- [ ] State persistence working
- [ ] Testing done

---

## 🎯 **Key Milestones**

| Week | Milestone | Demo |
|------|-----------|------|
| 1 | Dark mode working | Theme switching demo |
| 3 | UI foundation complete | Dark mode + settings + table |
| 5 | Search & categories done | Advanced search demo |
| 8 | Data features complete | Export/import/backup demo |
| 12 | **Release v1.3.0** | 🚀 Ship it! |

---

## 💡 **Development Tips**

1. **One Feature at a Time** - Complete each feature before moving on
2. **Test Early** - Don't wait until the end
3. **Commit Often** - Small, focused commits
4. **Follow the Plan** - Resist scope creep
5. **Ask for Help** - Don't waste time being stuck

---

## 📚 **Key Documents**

- 📘 **ROADMAP_v1.3.0.md** - Complete feature specifications
- 📗 **SPRINT_PLAN_v1.3.0.md** - Day-by-day execution plan
- 📙 **USER_GUIDE.md** - User documentation (update as you go)
- 📕 **CHANGELOG.md** - Version history (update weekly)

---

## ✅ **Weekly Routine**

**Monday:**
- Review week's goals
- Create feature branch
- Start first task

**Tuesday-Thursday:**
- Code and test
- Commit daily
- Update documentation

**Friday:**
- Complete testing
- Code review
- Merge to develop
- Plan next week

---

## 🚀 **Ready to Start?**

**Step 1:** Read ROADMAP_v1.3.0.md (understand the features)  
**Step 2:** Review Week 1 in SPRINT_PLAN_v1.3.0.md  
**Step 3:** Create dark-theme.css (Monday's task)  
**Step 4:** Start coding! 🎨

---

**v1.3.0 - The Features You Were Promised** ✨

*12 weeks. Focused scope. Quality delivery.*
