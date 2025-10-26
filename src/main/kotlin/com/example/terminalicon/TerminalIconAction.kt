package com.example.terminalicon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.ShellTerminalWidget
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
        fun executeCommandsInTerminal(project: com.intellij.openapi.project.Project, savedCommand: SavedCommand) {
            val terminalView = TerminalView.getInstance(project)

            ApplicationManager.getApplication().invokeLater {
                // Create a new terminal tab
                val widget = terminalView.createLocalShellWidget(project.basePath, savedCommand.name)

                // Execute all commands in sequence
                widget?.let {
                    savedCommand.commands.forEach { command ->
                        it.executeCommand(command)
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
