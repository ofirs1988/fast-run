# Terminal Icon Plugin

תוסף פשוט ל-JetBrains IDEs שמוסיף אייקון ליד אייקון הטרמינל.

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

## מה התוסף עושה?

התוסף מוסיף כפתור עם אייקון כחול ליד כפתור הטרמינל ב-toolbar. כשלוחצים על האייקון, הוא פותח את חלון הטרמינל.

## התאמה אישית

- **לשנות את האייקון:** ערוך את הקובץ `src/main/resources/icons/terminal-custom.svg`
- **לשנות את המיקום:** ערוך את `plugin.xml` בשורה שמתחילה ב-`<add-to-group`
- **לשנות את הפעולה:** ערוך את `TerminalIconAction.kt`
