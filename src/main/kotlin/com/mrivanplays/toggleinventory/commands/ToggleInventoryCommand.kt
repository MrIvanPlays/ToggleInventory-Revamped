package com.mrivanplays.toggleinventory.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.MessageKeys
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.mrivanplays.toggleinventory.ToggleInventory
import com.mrivanplays.toggleinventory.util.TIUtils
import com.mrivanplays.toggleinventory.util.VersionInfo
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("toggleinventory|th")
class ToggleInventoryCommand(private val plugin: ToggleInventory) : BaseCommand() {

    @Default
    fun loadInventory(
        player: Player,
        inventory: Int
    ) {
        if (!TIUtils.checkPermission(player, inventory, plugin.config.conf.getInt("max-inventories"))) {
            player.sendMessage(
                ChatColor.RED.toString() +
                        plugin.commandManager.locales.getMessage(
                            plugin.commandManager.getCommandIssuer(player),
                            MessageKeys.PERMISSION_DENIED
                        )
            )
            return
        }

        plugin.storage.storeCurrentlyLoadedInventory(player)
        plugin.storage.applyInventory(player, inventory)
        player.sendMessage(
            plugin.colorize(
                plugin.config.conf.getString("messages.applied-inventory")
            ).replace("%inventory%", inventory.toString())
        )
    }

    @Subcommand("reload")
    @CommandPermission("toggleinventory.reload")
    fun reload(
        sender: CommandSender
    ) {
        plugin.config.reload()
        sender.sendMessage("§aConfiguration reloaded successfully")
    }

    @Subcommand("info")
    @CommandPermission("toggleinventory.info")
    fun info(
        sender: CommandSender
    ) {
        val info: VersionInfo = plugin.versionInfo
        sender.sendMessage("ToggleInventory-Revamped v" + info.version)
        sender.sendMessage("Build: " + info.build)
        sender.sendMessage("Git commit: " + info.commit)
        if (info.version.contains("SNAPSHOT")) {
            sender.sendMessage("Warning: you are running a dev build. You should report every bug to the issue tracker")
        }

    }
}