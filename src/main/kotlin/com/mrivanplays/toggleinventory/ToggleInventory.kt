package com.mrivanplays.toggleinventory

import co.aikar.commands.PaperCommandManager
import com.mrivanplays.toggleinventory.commands.ToggleInventoryCommand
import com.mrivanplays.toggleinventory.listeners.PlayerJoinListener
import com.mrivanplays.toggleinventory.listeners.PlayerQuitListener
import com.mrivanplays.toggleinventory.storage.Storage
import com.mrivanplays.toggleinventory.storage.YamlStorage
import com.mrivanplays.toggleinventory.util.TIUtils
import com.mrivanplays.toggleinventory.util.UpdateChecker
import com.mrivanplays.toggleinventory.util.VersionInfo
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ToggleInventory : JavaPlugin() {

    lateinit var config: Configuration
    lateinit var storage: Storage
    lateinit var commandManager: PaperCommandManager
    lateinit var versionInfo: VersionInfo
    lateinit var updateChecker: UpdateChecker

    override fun onEnable() {
        config = Configuration(dataFolder)
        // todo: more storage types, configurable storage type
        storage = YamlStorage(dataFolder.toPath())

        commandManager = PaperCommandManager(this)
        commandManager.commandCompletions.registerCompletion("inventoryrange") { context ->
            val maxInventories = TIUtils.getMaxFromPermission(
                context.player,
                config.conf.getInt("max-inventories")
            )
            if (maxInventories == 0) {
                Collections.emptyList<String>()
            }

            val num = 1
            val completions: MutableList<String> = mutableListOf()
            for (x in num .. maxInventories) {
                completions.add(x.toString())
            }
            completions
        }
        commandManager.registerCommand(ToggleInventoryCommand(this))

        versionInfo = VersionInfo(javaClass.`package`.implementationVersion)

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
        server.pluginManager.registerEvents(PlayerQuitListener(this), this)

        updateChecker = UpdateChecker(versionInfo)
    }

    override fun onDisable() {

    }

    fun colorize(text: String?): String {
        return ChatColor.translateAlternateColorCodes('&', text!!)
    }
}