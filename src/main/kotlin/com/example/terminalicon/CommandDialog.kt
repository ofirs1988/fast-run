package com.example.terminalicon

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.table.DefaultTableModel

class CommandDialog(private val project: Project) : DialogWrapper(project) {
    // UI Components
    private val commandListModel = DefaultListModel<SavedCommand>()
    private val commandList = JBList(commandListModel)
    private val storage = CommandStorageService.getInstance()

    // Editor Fields
    private val nameField = JTextField()
    private val commandArea = JTextArea(5, 30)
    private val workingDirField = TextFieldWithBrowseButton()
    private val iconComboBox = JComboBox(arrayOf("default", "green", "blue", "red", "yellow", "purple"))
    private val shortcutField = JTextField()
    private val favoriteCheckBox = JCheckBox("★ Favorite")

    // Environment Variables Table
    private val envVarsTableModel = DefaultTableModel(arrayOf("Variable", "Value"), 0)
    private val envVarsTable = JBTable(envVarsTableModel)

    // Search
    private val searchField = JTextField()
    private val showFavoritesOnly = JCheckBox("⭐ Favorites Only")

    // State
    private var editingCommand: SavedCommand? = null
    private val allCommands = mutableListOf<SavedCommand>()
    var selectedCommand: SavedCommand? = null

    init {
        title = "Fast Run - Terminal Command Manager"
        init()
        loadCommands()
        setupWorkingDirField()
        setupEnvVarsTable()
    }

    private fun setupWorkingDirField() {
        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
        workingDirField.addBrowseFolderListener(
            "Select Working Directory",
            "Choose the directory where commands will be executed",
            project,
            descriptor
        )
    }

    private fun setupEnvVarsTable() {
        envVarsTable.setShowGrid(true)
        envVarsTable.preferredScrollableViewportSize = Dimension(400, 100)
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.preferredSize = Dimension(900, 700)

        // Top panel - Command Editor
        val topPanel = createCommandEditorPanel()

        // Middle panel - Commands List
        val middlePanel = createCommandListPanel()

        // Bottom panel - History
        val bottomPanel = createHistoryPanel()

        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(middlePanel, BorderLayout.CENTER)
        panel.add(bottomPanel, BorderLayout.SOUTH)

        return panel
    }

    private fun createCommandEditorPanel(): JPanel {
        val panel = JPanel(GridBagLayout())
        panel.border = BorderFactory.createTitledBorder("📝 Command Editor")

        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0
        gbc.insets = java.awt.Insets(3, 5, 3, 5)

        commandArea.lineWrap = true
        commandArea.wrapStyleWord = true

        var row = 0

        // Row 0: Name + Favorite
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0
        panel.add(JLabel("Name:"), gbc)
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0
        panel.add(nameField, gbc)
        gbc.gridx = 3; gbc.gridwidth = 1; gbc.weightx = 0.0
        favoriteCheckBox.font = favoriteCheckBox.font.deriveFont(16f)
        panel.add(favoriteCheckBox, gbc)
        row++

        // Row 1: Commands
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0
        gbc.anchor = GridBagConstraints.NORTH
        panel.add(JLabel("Commands:"), gbc)
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.BOTH
        gbc.weighty = 0.3
        panel.add(JBScrollPane(commandArea), gbc)
        row++

        // Row 2: Help
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 3
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weighty = 0.0
        val helpLabel = JLabel("<html><i>💡 One command per line. Variables: {project_name}, {project_path}, {date}, {time}</i></html>")
        panel.add(helpLabel, gbc)
        row++

        // Row 3: Working Directory
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0
        panel.add(JLabel("Working Dir:"), gbc)
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0
        panel.add(workingDirField, gbc)
        row++

        // Row 4: Icon
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0
        panel.add(JLabel("Icon:"), gbc)
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.3
        panel.add(iconComboBox, gbc)

        // Shortcut
        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0
        panel.add(JLabel("Shortcut:"), gbc)
        gbc.gridx = 3; gbc.gridwidth = 1; gbc.weightx = 0.7
        panel.add(shortcutField, gbc)
        row++

        // Row 5: Environment Variables
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0
        gbc.anchor = GridBagConstraints.NORTH
        panel.add(JLabel("Env Vars:"), gbc)

        val envPanel = JPanel(BorderLayout())
        envPanel.add(JBScrollPane(envVarsTable), BorderLayout.CENTER)

        val envButtonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val addEnvButton = JButton("+ Add")
        val removeEnvButton = JButton("- Remove")

        addEnvButton.addActionListener { addEnvironmentVariable() }
        removeEnvButton.addActionListener { removeEnvironmentVariable() }

        envButtonPanel.add(addEnvButton)
        envButtonPanel.add(removeEnvButton)
        envPanel.add(envButtonPanel, BorderLayout.SOUTH)

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.BOTH
        gbc.weighty = 0.2
        panel.add(envPanel, gbc)
        row++

        // Row 6: Buttons
        val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val saveButton = JButton("💾 Save")
        val cancelEditButton = JButton("❌ Cancel")
        val templatesButton = JButton("📋 Templates")
        cancelEditButton.isVisible = false

        saveButton.addActionListener { saveCommand(saveButton, cancelEditButton) }
        cancelEditButton.addActionListener { cancelEdit(saveButton, cancelEditButton) }
        templatesButton.addActionListener { showTemplatesDialog() }

        buttonPanel.add(saveButton)
        buttonPanel.add(cancelEditButton)
        buttonPanel.add(templatesButton)

        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 3
        gbc.fill = GridBagConstraints.NONE
        gbc.weighty = 0.0
        panel.add(buttonPanel, gbc)

        return panel
    }

    private fun addEnvironmentVariable() {
        val varName = JOptionPane.showInputDialog(this.rootPane, "Variable Name:", "Add Environment Variable", JOptionPane.PLAIN_MESSAGE)
        if (varName != null && varName.isNotBlank()) {
            val varValue = JOptionPane.showInputDialog(this.rootPane, "Variable Value:", "Add Environment Variable", JOptionPane.PLAIN_MESSAGE)
            if (varValue != null) {
                envVarsTableModel.addRow(arrayOf(varName.trim(), varValue.trim()))
            }
        }
    }

    private fun removeEnvironmentVariable() {
        val selectedRow = envVarsTable.selectedRow
        if (selectedRow >= 0) {
            envVarsTableModel.removeRow(selectedRow)
        } else {
            Messages.showWarningDialog("Please select a variable to remove", "Warning")
        }
    }

    private fun getEnvironmentVariablesFromTable(): Map<String, String> {
        val envVars = mutableMapOf<String, String>()
        for (i in 0 until envVarsTableModel.rowCount) {
            val key = envVarsTableModel.getValueAt(i, 0) as String
            val value = envVarsTableModel.getValueAt(i, 1) as String
            if (key.isNotBlank()) {
                envVars[key] = value
            }
        }
        return envVars
    }

    private fun setEnvironmentVariablesInTable(envVars: Map<String, String>) {
        envVarsTableModel.rowCount = 0
        envVars.forEach { (key, value) ->
            envVarsTableModel.addRow(arrayOf(key, value))
        }
    }

    private fun showTemplatesDialog() {
        val templates = CommandTemplates.templates
        val templateNames = templates.map { "${it.name} - ${it.description}" }.toTypedArray()

        val selected = JOptionPane.showInputDialog(
            this.rootPane,
            "Select a template:",
            "Command Templates",
            JOptionPane.PLAIN_MESSAGE,
            null,
            templateNames,
            templateNames[0]
        ) as? String

        if (selected != null) {
            val index = templateNames.indexOf(selected)
            if (index >= 0) {
                val template = templates[index]
                nameField.text = template.name
                commandArea.text = template.commands.joinToString("\n")
                iconComboBox.selectedItem = template.icon
                setEnvironmentVariablesInTable(template.envVars)
            }
        }
    }

    private fun createCommandListPanel(): JPanel {
        val panel = JPanel(BorderLayout(5, 5))
        panel.border = BorderFactory.createTitledBorder("📚 Saved Commands")

        // Search bar + Favorites filter
        val searchPanel = JPanel(BorderLayout(5, 5))
        val searchSubPanel = JPanel(BorderLayout())
        searchSubPanel.add(JLabel("🔍 Search: "), BorderLayout.WEST)
        searchSubPanel.add(searchField, BorderLayout.CENTER)
        searchPanel.add(searchSubPanel, BorderLayout.CENTER)

        showFavoritesOnly.addActionListener { filterCommands() }
        searchPanel.add(showFavoritesOnly, BorderLayout.EAST)

        searchField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) = filterCommands()
            override fun removeUpdate(e: DocumentEvent?) = filterCommands()
            override fun changedUpdate(e: DocumentEvent?) = filterCommands()
        })

        panel.add(searchPanel, BorderLayout.NORTH)

        // Commands list
        commandList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        commandList.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): java.awt.Component {
                val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                if (value is SavedCommand) {
                    val icon = getIconForColor(value.icon)
                    val favorite = if (value.isFavorite) "⭐ " else ""
                    val envInfo = if (value.environmentVariables.isNotEmpty()) " [${value.environmentVariables.size} env vars]" else ""
                    text = "$favorite$icon ${value.name}: ${value.getCommandsAsString()}$envInfo"
                }
                return component
            }
        }

        val scrollPane = JBScrollPane(commandList)
        panel.add(scrollPane, BorderLayout.CENTER)

        // Action buttons
        val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val runButton = JButton("▶ Run")
        val editButton = JButton("✏ Edit")
        val favoriteButton = JButton("⭐ Toggle Favorite")
        val deleteButton = JButton("🗑 Delete")
        val importButton = JButton("📥 Import")
        val exportButton = JButton("📤 Export")

        runButton.addActionListener { runSelectedCommand() }
        editButton.addActionListener { editSelectedCommand() }
        favoriteButton.addActionListener { toggleFavoriteCommand() }
        deleteButton.addActionListener { deleteSelectedCommand() }
        importButton.addActionListener { importCommands() }
        exportButton.addActionListener { exportCommands() }

        buttonPanel.add(runButton)
        buttonPanel.add(editButton)
        buttonPanel.add(favoriteButton)
        buttonPanel.add(deleteButton)
        buttonPanel.add(JSeparator(SwingConstants.VERTICAL))
        buttonPanel.add(importButton)
        buttonPanel.add(exportButton)

        panel.add(buttonPanel, BorderLayout.SOUTH)

        return panel
    }

    private fun createHistoryPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createTitledBorder("📊 Recent Executions")
        panel.preferredSize = Dimension(900, 100)

        val historyList = JBList<String>()
        val historyModel = DefaultListModel<String>()
        historyList.model = historyModel

        storage.executionHistory.take(10).forEach { entry ->
            val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(java.util.Date(entry.timestamp))
            historyModel.addElement("🕒 ${entry.commandName} - $date")
        }

        panel.add(JBScrollPane(historyList), BorderLayout.CENTER)
        return panel
    }

    private fun getIconForColor(color: String): String {
        return when (color) {
            "green" -> "🟢"
            "blue" -> "🔵"
            "red" -> "🔴"
            "yellow" -> "🟡"
            "purple" -> "🟣"
            else -> "🎯"
        }
    }

    private fun saveCommand(saveButton: JButton, cancelEditButton: JButton) {
        val name = nameField.text.trim()
        val commandsText = commandArea.text.trim()
        val workingDir = workingDirField.text.trim()
        val icon = iconComboBox.selectedItem as String
        val shortcut = shortcutField.text.trim()
        val isFavorite = favoriteCheckBox.isSelected
        val envVars = getEnvironmentVariablesFromTable()

        if (name.isEmpty() || commandsText.isEmpty()) {
            Messages.showWarningDialog("Please fill both name and commands fields", "Warning")
            return
        }

        val commands = commandsText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (commands.isEmpty()) {
            Messages.showWarningDialog("Please enter at least one command", "Warning")
            return
        }

        if (editingCommand != null) {
            storage.updateCommand(editingCommand!!, name, commands, workingDir, icon, shortcut, isFavorite, envVars)
            editingCommand = null
            saveButton.text = "💾 Save"
            cancelEditButton.isVisible = false
        } else {
            storage.addCommand(name, commands, workingDir, icon, shortcut, isFavorite, envVars)
        }

        loadCommands()
        clearFields()
    }

    private fun cancelEdit(saveButton: JButton, cancelEditButton: JButton) {
        editingCommand = null
        saveButton.text = "💾 Save"
        cancelEditButton.isVisible = false
        clearFields()
    }

    private fun clearFields() {
        nameField.text = ""
        commandArea.text = ""
        workingDirField.text = ""
        iconComboBox.selectedIndex = 0
        shortcutField.text = ""
        favoriteCheckBox.isSelected = false
        envVarsTableModel.rowCount = 0
    }

    private fun editSelectedCommand() {
        val selected = commandList.selectedValue ?: run {
            Messages.showWarningDialog("Please select a command to edit", "Warning")
            return
        }

        editingCommand = selected
        nameField.text = selected.name
        commandArea.text = selected.commands.joinToString("\n")
        workingDirField.text = selected.workingDirectory
        iconComboBox.selectedItem = selected.icon
        shortcutField.text = selected.keyboardShortcut
        favoriteCheckBox.isSelected = selected.isFavorite
        setEnvironmentVariablesInTable(selected.environmentVariables)

        val saveButton = findComponentByText("💾 Save") as? JButton
        saveButton?.text = "💾 Update"

        val cancelEditButton = findComponentByText("❌ Cancel") as? JButton
        cancelEditButton?.isVisible = true
    }

    private fun toggleFavoriteCommand() {
        val selected = commandList.selectedValue ?: run {
            Messages.showWarningDialog("Please select a command", "Warning")
            return
        }

        storage.toggleFavorite(selected)
        loadCommands()
    }

    private fun findComponentByText(text: String): java.awt.Component? {
        return findComponentRecursive(rootPane, text)
    }

    private fun findComponentRecursive(parent: java.awt.Container, text: String): java.awt.Component? {
        for (component in parent.components) {
            if (component is JButton && component.text == text) {
                return component
            }
            if (component is java.awt.Container) {
                val found = findComponentRecursive(component, text)
                if (found != null) return found
            }
        }
        return null
    }

    private fun runSelectedCommand() {
        val selected = commandList.selectedValue ?: run {
            Messages.showWarningDialog("Please select a command to run", "Warning")
            return
        }

        selectedCommand = selected
        storage.addToHistory(selected.name)
        close(OK_EXIT_CODE)
    }

    private fun deleteSelectedCommand() {
        val selected = commandList.selectedValue ?: run {
            Messages.showWarningDialog("Please select a command to delete", "Warning")
            return
        }

        val confirm = Messages.showYesNoDialog(
            "Are you sure you want to delete '${selected.name}'?",
            "Confirm Delete",
            Messages.getQuestionIcon()
        )

        if (confirm == Messages.YES) {
            storage.removeCommand(selected)
            loadCommands()
        }
    }

    private fun importCommands() {
        Messages.showInfoMessage("Import functionality coming soon! Will support JSON format.", "Info")
    }

    private fun exportCommands() {
        Messages.showInfoMessage("Export functionality coming soon! Will support JSON format.", "Info")
    }

    private fun filterCommands() {
        val searchText = searchField.text.lowercase()
        commandListModel.clear()

        allCommands
            .filter { cmd ->
                val matchesSearch = cmd.name.lowercase().contains(searchText) ||
                        cmd.commands.any { it.lowercase().contains(searchText) }
                val matchesFavorite = !showFavoritesOnly.isSelected || cmd.isFavorite
                matchesSearch && matchesFavorite
            }
            .sortedByDescending { it.isFavorite }
            .forEach { commandListModel.addElement(it) }
    }

    private fun loadCommands() {
        allCommands.clear()
        allCommands.addAll(storage.savedCommands)
        filterCommands()
    }

    override fun createActions(): Array<Action> {
        return arrayOf(cancelAction)
    }
}
