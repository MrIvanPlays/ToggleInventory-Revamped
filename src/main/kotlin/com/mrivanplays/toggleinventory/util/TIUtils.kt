package com.mrivanplays.toggleinventory.util

import org.bukkit.command.CommandSender

object TIUtils {

    fun checkPermission(sender: CommandSender, number: Int, max: Int): Boolean {
        if (sender.hasPermission("toggleinventory.inventories.$number")) {
            return true
        }
        for (x in number .. max) {
            if (sender.hasPermission("toggleinventory.inventories.$x")) {
                return true
            }
        }
        return false
    }

    fun getMaxFromPermission(sender: CommandSender, max: Int): Int {
        val number = 0
        for (x in number .. max) {
            if (sender.hasPermission("toggleinventory.inventories.$x")) {
                return x
            }
        }
        return 0
    }
}