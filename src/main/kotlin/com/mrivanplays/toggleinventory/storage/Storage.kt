package com.mrivanplays.toggleinventory.storage

import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

interface Storage {

    fun applyInventory(player: Player, number: Int)

    fun storeCurrentlyLoadedInventory(player: Player)

    fun getLastLoadedInventory(player: Player): Int

    fun onQuit(player: Player)

    fun serializeInventory(
        inventory: PlayerInventory
    ): MutableMap<String, Map<String, Any>> {
        val itemsStack = inventory.contents.filterNotNull()
        val serializableItems: MutableMap<String, Map<String, Any>> = mutableMapOf()

        for ((index, item) in itemsStack.withIndex()) {
            val serializedMap = item.serialize()
            serializedMap["slot"] = inventory.indexOf(item)
            serializableItems[index.toString()] = serializedMap
        }

        /*
 inventory-num:
     0:
       item-data
     1:
       item-data
     # and so on
        */
        return serializableItems
    }
}