plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.mrivanplays"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    shadowJar {
        archiveBaseName.set("ToggleInventory-Revamped")
        relocate("co.aikar.commands", "com.mrivanplays.toggleinventory.libs.acf")
        relocate("co.aikar.locales", "com.mrivanplays.toggleinventory.libs.locales")
        relocate("kotlin", "com.mrivanplays.toggleinventory.libs.kotlin")
        relocate("org.intellij.lang.annotations", "com.mrivanplays.toggleinventory.libs.lang.annotations")
        relocate("org.jetbrains.annotations", "com.mrivanplays.toggleinventory.libs.annotations")
    }

    processResources {
        expand(project.properties)
    }
}