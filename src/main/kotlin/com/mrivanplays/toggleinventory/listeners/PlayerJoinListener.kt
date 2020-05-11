package com.mrivanplays.toggleinventory.listeners

import com.mrivanplays.toggleinventory.ToggleInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val plugin: ToggleInventory) : Listener {

    @EventHandler
    fun onEvent(event: PlayerJoinEvent) {
        plugin.storage.applyInventory(event.player, plugin.storage.getLastLoadedInventory(event.player))
    }
}