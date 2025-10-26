package com.example.terminalicon

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(
    name = "TerminalCommandStorage",
    storages = [Storage("terminalCommands.xml")]
)
class CommandStorageService : PersistentStateComponent<CommandStorageService> {
    var savedCommands: MutableList<SavedCommand> = mutableListOf()

    override fun getState(): CommandStorageService = this

    override fun loadState(state: CommandStorageService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun addCommand(name: String, commands: List<String>) {
        savedCommands.add(SavedCommand(name, commands.toMutableList()))
    }

    fun removeCommand(command: SavedCommand) {
        savedCommands.remove(command)
    }

    companion object {
        fun getInstance(): CommandStorageService {
            return service()
        }
    }
}

data class SavedCommand(
    var name: String = "",
    var commands: MutableList<String> = mutableListOf()
) {
    // For display purposes - show all commands separated by " && "
    fun getCommandsAsString(): String {
        return commands.joinToString(" && ")
    }
}
