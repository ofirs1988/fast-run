package com.example.terminalicon

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class SavedCommandsActionGroup : ActionGroup(), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val storage = CommandStorageService.getInstance()
        val actions = mutableListOf<AnAction>()

        // Add all saved commands as actions
        storage.savedCommands.forEach { command ->
            actions.add(RunSavedCommandAction(command))
        }

        // If no commands, show a message action
        if (actions.isEmpty()) {
            actions.add(object : AnAction("No saved commands (Click toolbar icon to add)") {
                override fun actionPerformed(e: AnActionEvent) {
                    // Do nothing, this is just a placeholder
                }
                override fun update(e: AnActionEvent) {
                    e.presentation.isEnabled = false
                }
            })
        }

        return actions.toTypedArray()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }
}

class RunSavedCommandAction(private val savedCommand: SavedCommand) : AnAction(savedCommand.name, savedCommand.getCommandsAsString(), null), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        TerminalIconAction.executeCommandsInTerminal(project, savedCommand)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }
}
