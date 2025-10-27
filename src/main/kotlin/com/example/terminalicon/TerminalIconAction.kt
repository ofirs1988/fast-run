package com.example.terminalicon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalView

class TerminalIconAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        // Show the command dialog
        val dialog = CommandDialog(project)
        if (dialog.showAndGet()) {
            val selectedCommand = dialog.selectedCommand
            if (selectedCommand != null) {
                // Execute the commands in terminal
                executeCommandsInTerminal(project, selectedCommand)
            }
        }
    }

    companion object {
        @Suppress("DEPRECATION")
        fun executeCommandsInTerminal(project: com.intellij.openapi.project.Project, savedCommand: SavedCommand) {
            ApplicationManager.getApplication().invokeLater {
                // Use TerminalView which is the stable API
                val terminalView = TerminalView.getInstance(project)

                // Create a new terminal tab with the command name
                val shellWidget = terminalView.createLocalShellWidget(project.basePath, savedCommand.name)

                // Execute all commands in sequence
                shellWidget?.let { widget ->
                    savedCommand.commands.forEach { command ->
                        widget.executeCommand(command)
                    }
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        // Enable the action only when a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}
