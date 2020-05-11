package com.mrivanplays.toggleinventory.util

data class VersionInfo(private val combinedVersion: String) {

    val build: String
    val commit:String
    val version: String

    init {
        val info: List<String> = combinedVersion.split(":")
        build = info[4]
        commit = info[3]
        version = info[2]
    }
}