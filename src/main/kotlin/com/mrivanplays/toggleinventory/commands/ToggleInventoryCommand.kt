package com.mrivanplays.toggleinventory.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.MessageKeys
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import com.mrivanplays.toggleinventory.ToggleInventory
import com.mrivanplays.toggleinventory.util.TIUtils
import com.mrivanplays.toggleinventory.util.VersionInfo
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("toggleinventory|th")
class ToggleInventoryCommand(private val plugin: ToggleInventory) : BaseCommand() {

    @Default
    @Syntax("<inventory>")
    @CommandCompletion("@inventoryrange")
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

        if (inventory == 0) {
            player.sendMessage(
                plugin.colorize(
                    plugin.config.conf.getString("messages.invalid-inventory")
                )
            )
            return
        }

        if (plugin.storage.isCurrentlyApplied(player, inventory)) {
            player.sendMessage(
                plugin.colorize(
                    plugin.config.conf.getString("messages.inventory-already-applied")
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
            sender.sendMessage("Warning: you are running a development version. You should report every bug to the issue tracker")
        }
        sender.sendMessage("Checking for updates...")
        val result = plugin.updateChecker.getResultSafe()
        if (result.buildsBehind == 0 && !result.devBuild) {
            sender.sendMessage("You are running latest build")
        } else if (result.devBuild) {
            sender.sendMessage("You are running a custom build. Beware")
        } else {
            sender.sendMessage("You are " + result.buildsBehind + " build(s) behind")
            if (sender is Player) {
                sender.spigot()
                    .sendMessage(
                        *ComponentBuilder("Click ").append(
                            ComponentBuilder("here").event(
                                ClickEvent(
                                    ClickEvent.Action.OPEN_URL,
                                    result.lastBuildDownloadUrl
                                )
                            ).event(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    ComponentBuilder("Opens download url").create()
                                )
                            ).create()
                        ).append(" to open download url for newest build").create()
                    )
            } else {
                sender.sendMessage("Download url: " + result.lastBuildDownloadUrl)
            }
        }
    }
}