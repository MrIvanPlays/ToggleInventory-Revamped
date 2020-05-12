package com.mrivanplays.toggleinventory.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UpdateChecker(private val versionInfo: VersionInfo) {

    // info specification
    private val jobName = "ToggleInventory-Revamped"
    private val artifactName = "ToggleInventory-%buildNumber%.jar"
    private val artifactPath = "build/libs"
    private val ciUrl = "https://ci.mrivanplays.com/"

    private val jobApiUrl = "${ciUrl}job/${jobName}/api/json"
    var result: Result? = null
    private val gson: Gson = Gson()

    fun checkForUpdates() {
        val url = URL(jobApiUrl)
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.addRequestProperty("User-Agent", "ToggleInventory Plugin v" + versionInfo.version)
        InputStreamReader(urlConnection.inputStream).use {
            val obj = gson.fromJson(it, JsonObject::class.java)
            val lastBuildInfo = obj.get("builds").asJsonArray[0].asJsonObject
            val buildNumber = lastBuildInfo.get("number").asNumber.toInt()
            val buildNumberString = buildNumber.toString()
            val buildUrl = lastBuildInfo.get("url").asString
            val downloadUrl =
                "${buildUrl}/artifact/${artifactPath}/${artifactName.replace("%buildNumber%", buildNumberString)}"

            val currentBuildString = versionInfo.build
            if (currentBuildString == "CUSTOM BUILD") {
                result = Result(
                    devBuild = true,
                    lastBuildDownloadUrl = downloadUrl,
                    buildsBehind = 0
                )
                return
            }

            val currentBuild = currentBuildString.toInt()
            result = if (buildNumber > currentBuild) {
                Result(
                    devBuild = false,
                    lastBuildDownloadUrl = downloadUrl,
                    buildsBehind = buildNumber - currentBuild
                )
            } else {
                Result(
                    devBuild = false,
                    lastBuildDownloadUrl = downloadUrl,
                    buildsBehind = buildNumber - currentBuild
                )
            }
        }
    }

    fun getResultSafe(): Result {
        return if (result != null) {
            result!!
        } else {
            checkForUpdates()
            result!!
        }
    }

    class Result(
        val devBuild: Boolean,
        val lastBuildDownloadUrl: String,
        val buildsBehind: Int
    )
}