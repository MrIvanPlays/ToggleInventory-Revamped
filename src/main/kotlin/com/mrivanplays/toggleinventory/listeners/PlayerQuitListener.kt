package com.mrivanplays.toggleinventory.listeners

import com.mrivanplays.toggleinventory.ToggleInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: ToggleInventory) : Listener {

    @EventHandler
    fun onEvent(event: PlayerQuitEvent) {
        plugin.storage.onQuit(event.player)
    }
}