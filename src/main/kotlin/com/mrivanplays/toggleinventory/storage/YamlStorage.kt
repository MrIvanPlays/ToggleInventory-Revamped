package com.mrivanplays.toggleinventory.storage

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class YamlStorage(pluginFolder: Path) : Storage {

    private val currentlyLoaded: MutableMap<UUID, Int> = mutableMapOf()
    private val dataFolder: Path = pluginFolder.resolve("inventories")

    override fun applyInventory(
        player: Player,
        number: Int
    ) {
        val numberPath = number.toString()
        val config = getPlayerConfig(player.uniqueId)
        currentlyLoaded[player.uniqueId] = number

        if (!config.isSet(numberPath)) {
            player.inventory.clear()
        } else {
            val contents: MutableMap<Int, ItemStack> = mutableMapOf()
            for (itemNum in config.getConfigurationSection(numberPath)?.getKeys(false)!!) {
                val itemSection =
                    config.getConfigurationSection("$numberPath.$itemNum") ?: continue
                val map = itemSection.getValues(true)
                val slot = map["slot"] as Int
                val item = ItemStack.deserialize(map)
                contents[slot] = item
            }

            player.inventory.clear()

            for (contentEntry in contents.entries) {
                player.inventory.setItem(contentEntry.key, contentEntry.value)
            }
        }
    }

    override fun storeCurrentlyLoadedInventory(
        player: Player
    ) {
        val currentlyLoadedInventoryNumber: Int = currentlyLoaded[player.uniqueId] ?: 1
        val uniqueId = player.uniqueId
        val config = getPlayerConfig(uniqueId)

        config.set(currentlyLoadedInventoryNumber.toString(), serializeInventory(player.inventory))
        config.save(File(dataFolder.toFile(), "$uniqueId.yml"))
    }

    override fun getLastLoadedInventory(player: Player): Int {
        val fromMemory = currentlyLoaded[player.uniqueId]
        return if (fromMemory == null) {
            val config = getPlayerConfig(player.uniqueId)
            val fromConfig = config.getInt("last-loaded-inventory")
            if (fromConfig == 0) {
                currentlyLoaded[player.uniqueId] = 1
                1
            } else {
                currentlyLoaded[player.uniqueId] = fromConfig
                fromConfig
            }
        } else {
            fromMemory
        }
    }

    override fun onQuit(player: Player) {
        val uniqueId = player.uniqueId
        val config = getPlayerConfig(uniqueId)
        val currentlyLoadedInventory = currentlyLoaded[uniqueId] ?: 1
        config.set("last-loaded-inventory", currentlyLoadedInventory)
        config.set(currentlyLoadedInventory.toString(), serializeInventory(player.inventory))
        config.save(File(dataFolder.toFile(), "$uniqueId.yml"))
    }

    private fun getPlayerConfig(
        uuid: UUID
    ): FileConfiguration {
        if (!Files.exists(dataFolder)) {
            Files.createDirectories(dataFolder)
        }

        val filePath = dataFolder.resolve("$uuid.yml")
        if (!Files.exists(filePath)) {
            Files.createFile(filePath)
        }

        return YamlConfiguration.loadConfiguration(Files.newBufferedReader(filePath))
    }
}