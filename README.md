# Fast Run - Terminal Command Manager

**Save and execute your favorite terminal commands with a single click!**

A powerful productivity plugin for JetBrains IDEs that lets you save, organize, and quickly run terminal commands.

## מבנה הפרויקט

```
jetbrains_extention/
├── build.gradle.kts          # קובץ הבנייה של Gradle
├── settings.gradle.kts       # הגדרות Gradle
├── gradle.properties         # מאפייני התוסף
└── src/
    └── main/
        ├── kotlin/
        │   └── com/example/terminalicon/
        │       └── TerminalIconAction.kt    # ה-Action שמפעיל את הטרמינל
        └── resources/
            ├── META-INF/
            │   └── plugin.xml               # הגדרות התוסף
            └── icons/
                └── terminal-custom.svg      # האייקון המותאם אישית
```

## איך לבנות ולהריץ

### דרישות מקדימות
- JDK 17 או גרסה חדשה יותר
- Gradle (או השתמש ב-Gradle wrapper שיורד אוטומטית)

### פקודות

1. **לבנות את התוסף:**
   ```bash
   ./gradlew buildPlugin
   ```

2. **להריץ IDE עם התוסף:**
   ```bash
   ./gradlew runIde
   ```

3. **לבדוק את התוסף:**
   ```bash
   ./gradlew verifyPlugin
   ```

## ✨ Features

- **💾 Save Multiple Commands:** Store unlimited terminal commands with custom names
- **🚀 Run Command Sequences:** Execute multiple commands in sequence with one click
- **📋 Fast Run Menu:** New dedicated menu in the main menu bar for quick access
- **🎨 Toolbar Icon:** Beautiful green icon for instant access to command manager
- **💪 Persistent Storage:** Your commands are saved automatically and persist across IDE restarts
- **📝 Easy Management:** Simple dialog to add, edit, and delete saved commands

## 🎯 Perfect For

- npm/yarn scripts (build, test, deploy)
- Docker commands
- Git workflows
- Build and deployment scripts
- Database migrations
- Any repetitive terminal tasks!

## 📦 Installation

### From JetBrains Marketplace (Coming Soon)
1. Open IntelliJ IDEA / WebStorm / PyCharm
2. Go to `Settings` → `Plugins` → `Marketplace`
3. Search for "Fast Run"
4. Click `Install`

### Manual Installation
1. Download the latest `.zip` from [Releases](https://github.com/ofirs1988/jetbrains_extensions/releases)
2. Go to `Settings` → `Plugins` → ⚙️ → `Install Plugin from Disk`
3. Select the downloaded ZIP file
4. Restart IDE

## 🚀 Usage

### Adding Commands
1. Click the green toolbar icon 🟢 OR go to **Fast Run → Manage Commands...**
2. Enter a name for your command (e.g., "Build Project")
3. Add your commands (one per line for sequences):
   ```
   npm install
   npm run build
   npm test
   ```
4. Click **Save Command**

### Running Commands
- **Option 1:** Go to **Fast Run** menu and click your saved command
- **Option 2:** Click the toolbar icon and select from saved commands

### Command Sequences
Add multiple commands (one per line) and they'll run in order:
```
git pull
npm install
npm run build
```

## 🛠️ Development
