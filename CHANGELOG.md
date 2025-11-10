# Changelog

All notable changes to the "Fast Run - Terminal Command Manager" plugin will be documented in this file.

## [1.0.4] - 2025-10-27

### Added
- 🖥️ **Terminal Integration**: Fast Run now appears directly in the Terminal window!
  - Fast Run button in Terminal toolbar (top of terminal window)
  - Fast Run menu in Terminal right-click context menu
  - Access your commands without leaving the terminal
  - Seamless integration with IntelliJ Terminal
- ⭐ **Favorites Feature**: Mark important commands as favorites with star icon
  - Toggle favorite status with dedicated button
  - Filter to show only favorite commands
  - Favorites appear first in the list
- 🌍 **Environment Variables**: Set custom environment variables per command
  - Add/remove environment variables with table interface
  - Variables are automatically exported before command execution
  - Support for multiple environment variables per command
- 📋 **Command Templates**: 40+ pre-configured templates for common tasks
  - **NPM**: install, dev, build, test, clean install
  - **Docker**: build, run, compose up/down, clean
  - **Git**: status, commit, pull, push, new branch
  - **Yarn**: install, dev, build
  - **Python**: venv, pip install, run
  - **Gradle**: build, clean build, test
  - **Maven**: clean install, test
  - **Laravel/PHP**: serve, migrate, composer
  - **React Native**: start, android, ios
  - One-click template insertion with auto-filled fields
- ✏️ **Edit Command Feature**: Edit existing commands directly without deleting and recreating
- 🎨 **Color Icons**: Choose from 6 different color icons for each command (default, green, blue, red, yellow, purple)
- 📂 **Custom Working Directory**: Set a specific working directory for each command
- 🔍 **Search & Filter**: Quickly find commands with real-time search functionality
- 🔄 **Command Variables**: Use dynamic variables in commands:
  - `{project_name}` - Current project name
  - `{project_path}` - Project base path
  - `{file_name}` - Current file name
  - `{date}` - Current date
  - `{time}` - Current time
- 📊 **Execution History**: View the last 10 executed commands with timestamps
- 📥 **Import/Export**: Import and export command sets (JSON format - coming soon)
- ⌨️ **Keyboard Shortcuts**: Assign custom keyboard shortcuts to commands (field ready)

### Improved
- 🎯 Larger, more user-friendly dialog (900x700px)
- 🎨 Better UI organization with clear sections and emojis
- 💾 Enhanced command editor with more options
- 🗂️ Better command list display with icons, colors, and environment variable indicators
- ⚡ Real-time search filtering with favorites-only option
- ✅ Confirmation dialog when deleting commands
- 🎪 Clean, professional interface with better spacing
- 📚 Organized templates by category (NPM, Docker, Git, etc.)

### Changed
- Dialog title updated to "Fast Run - Terminal Command Manager"
- Commands now execute in their specified working directory
- Variables are processed before command execution
- Environment variables are exported before command execution
- History is automatically saved on each execution
- Favorites appear at the top of the list

## [1.0.3] - 2025-10-27

### Fixed
- Version bump for marketplace submission

## [1.0.2] - 2025-10-27

### Fixed
- Added deprecation suppression for Terminal API
- Note: Currently using TerminalView API which is scheduled for deprecation. JetBrains is transitioning the Terminal API, and we will update to the stable replacement once available

## [1.0.1] - 2025-10-27

### Changed
- Updated repository URL to https://github.com/ofirs1988/fast-run
- Clean commit history for production release

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

[1.0.0]: https://github.com/ofirs1988/fast-run/releases/tag/v1.0.0
