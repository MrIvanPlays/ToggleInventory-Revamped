package com.mrivanplays.toggleinventory

import co.aikar.commands.PaperCommandManager
import com.mrivanplays.toggleinventory.commands.ToggleInventoryCommand
import com.mrivanplays.toggleinventory.listeners.PlayerJoinListener
import com.mrivanplays.toggleinventory.listeners.PlayerQuitListener
import com.mrivanplays.toggleinventory.storage.Storage
import com.mrivanplays.toggleinventory.storage.YamlStorage
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class ToggleInventory : JavaPlugin() {

    lateinit var config: Configuration
    lateinit var storage: Storage
    lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        config = Configuration(dataFolder)
        // todo: more storage types, configurable storage type
        storage = YamlStorage(dataFolder.toPath())

        commandManager = PaperCommandManager(this)
        commandManager.registerCommand(ToggleInventoryCommand(this))

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
        server.pluginManager.registerEvents(PlayerQuitListener(this), this)
    }

    override fun onDisable() {

    }

    fun colorize(text: String?): String {
        return ChatColor.translateAlternateColorCodes('&', text!!)
    }
}