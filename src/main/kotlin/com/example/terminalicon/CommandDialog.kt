package com.example.terminalicon

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class CommandDialog(private val project: Project) : DialogWrapper(project) {
    private val commandListModel = DefaultListModel<SavedCommand>()
    private val commandList = JBList(commandListModel)
    private val storage = CommandStorageService.getInstance()

    var selectedCommand: SavedCommand? = null

    init {
        title = "Run Terminal Command"
        init()
        loadCommands()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.preferredSize = Dimension(600, 500)

        // Top panel - Add new command
        val topPanel = JPanel(GridBagLayout())
        topPanel.border = BorderFactory.createTitledBorder("Add New Command")

        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0

        val nameField = JTextField()
        val commandArea = JTextArea(5, 30)
        commandArea.lineWrap = true
        commandArea.wrapStyleWord = true
        val commandScrollPane = JBScrollPane(commandArea)

        gbc.gridx = 0
        gbc.gridy = 0
        gbc.gridwidth = 1
        topPanel.add(JLabel("Name:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridwidth = 2
        topPanel.add(nameField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.gridwidth = 1
        gbc.anchor = GridBagConstraints.NORTH
        topPanel.add(JLabel("Commands:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 1
        gbc.gridwidth = 2
        gbc.fill = GridBagConstraints.BOTH
        gbc.weighty = 0.3
        topPanel.add(commandScrollPane, gbc)

        gbc.gridx = 1
        gbc.gridy = 2
        gbc.gridwidth = 2
        gbc.fill = GridBagConstraints.NONE
        gbc.weighty = 0.0
        val helpLabel = JLabel("<html><i>Tip: Enter one command per line</i></html>")
        topPanel.add(helpLabel, gbc)

        val addButton = JButton("Save Command")
        addButton.addActionListener {
            val name = nameField.text.trim()
            val commandsText = commandArea.text.trim()

            if (name.isNotEmpty() && commandsText.isNotEmpty()) {
                // Split by newlines and filter out empty lines
                val commands = commandsText.lines()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                if (commands.isNotEmpty()) {
                    storage.addCommand(name, commands)
                    loadCommands()
                    nameField.text = ""
                    commandArea.text = ""
                } else {
                    Messages.showWarningDialog("Please enter at least one command", "Warning")
                }
            } else {
                Messages.showWarningDialog("Please fill both name and commands fields", "Warning")
            }
        }

        gbc.gridx = 1
        gbc.gridy = 3
        gbc.gridwidth = 1
        gbc.fill = GridBagConstraints.NONE
        topPanel.add(addButton, gbc)

        // Middle panel - Saved commands list
        val middlePanel = JPanel(BorderLayout())
        middlePanel.border = BorderFactory.createTitledBorder("Saved Commands")

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
                    text = "${value.name}: ${value.getCommandsAsString()}"
                }
                return component
            }
        }

        val scrollPane = JBScrollPane(commandList)
        middlePanel.add(scrollPane, BorderLayout.CENTER)

        // Bottom panel - Action buttons
        val bottomPanel = JPanel()
        val runButton = JButton("Run Selected")
        val deleteButton = JButton("Delete Selected")

        runButton.addActionListener {
            val selected = commandList.selectedValue
            if (selected != null) {
                selectedCommand = selected
                close(OK_EXIT_CODE)
            } else {
                Messages.showWarningDialog("Please select a command to run", "Warning")
            }
        }

        deleteButton.addActionListener {
            val selected = commandList.selectedValue
            if (selected != null) {
                storage.removeCommand(selected)
                loadCommands()
            } else {
                Messages.showWarningDialog("Please select a command to delete", "Warning")
            }
        }

        bottomPanel.add(runButton)
        bottomPanel.add(deleteButton)
        middlePanel.add(bottomPanel, BorderLayout.SOUTH)

        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(middlePanel, BorderLayout.CENTER)

        return panel
    }

    private fun loadCommands() {
        commandListModel.clear()
        storage.savedCommands.forEach { commandListModel.addElement(it) }
    }

    override fun createActions(): Array<Action> {
        return arrayOf(cancelAction)
    }
}
