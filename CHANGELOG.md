# Changelog

All notable changes to the "Fast Run - Terminal Command Manager" plugin will be documented in this file.

## [1.0.0] - 2025-10-26

### Added
- Initial release of Fast Run plugin
- Save and manage unlimited terminal commands with custom names
- Execute multiple commands sequentially in a single click
- New "Fast Run" menu in the main menu bar for quick access
- Toolbar icon (beautiful green gradient with terminal symbol and + sign)
- Command management dialog with multi-line support
- Persistent storage - commands are saved automatically across IDE restarts
- Dynamic menu that updates in real-time as commands are added/removed
- Support for command sequences (multiple commands per saved item)
- One-click execution in new terminal tabs

### Features
- **Command Manager Dialog**: Easy-to-use interface for adding, editing, and deleting commands
- **Multi-Command Support**: Add multiple commands (one per line) that run in sequence
- **Fast Run Menu**: Dedicated menu in the IDE main menu bar
- **Toolbar Integration**: Quick access icon in the main toolbar and navigation bar
- **Auto-Save**: Commands persist automatically between IDE sessions
- **Terminal Integration**: Commands execute in new terminal tabs with custom names

### Technical Details
- Built with Kotlin and IntelliJ Platform SDK
- Compatible with IntelliJ IDEA 2023.2+ and other JetBrains IDEs
- Gradle-based build system
- Lightweight (22KB plugin size)

[1.0.0]: https://github.com/ofirs1988/jetbrains_extensions/releases/tag/v1.0.0
