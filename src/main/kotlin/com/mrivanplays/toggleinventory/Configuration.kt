package com.mrivanplays.toggleinventory

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Files

class Configuration(dataFolder: File) {

    private val file: File = File(dataFolder, "config.yml")
    var conf: FileConfiguration

    init {
        createNewFileIfNotExists()
        conf = YamlConfiguration.loadConfiguration(file)
    }

    private fun createNewFileIfNotExists() {
        if (!file.exists()) {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            javaClass.classLoader.getResourceAsStream("config.yml").use {
                if (it == null) {
                    return
                }
                Files.copy(it, file.toPath())
            }
        }
    }

    fun reload() {
        createNewFileIfNotExists()
        conf = YamlConfiguration.loadConfiguration(file)
    }

}