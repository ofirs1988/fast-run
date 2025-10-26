package com.example.terminalicon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

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
            ApplicationManager.getApplication().invokeLater {
                val terminalManager = TerminalToolWindowManager.getInstance(project)

                // Create a new terminal tab with the command name
                val shellRunner = terminalManager.createLocalShellWidget(project.basePath, savedCommand.name, true, false)

                // Execute all commands in sequence
                savedCommand.commands.forEach { command ->
                    shellRunner.executeCommand(command)
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
