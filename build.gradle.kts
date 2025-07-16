import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    id("io.izzel.taboolib") version "2.0.23"
    kotlin("jvm") version "1.8.22"
}

taboolib {
    subproject = true
    env {
        // 安装模块
        install(Basic, Bukkit, BukkitHook, BukkitNMSUtil, Kether)
    }
    version {
        taboolib = "6.2.3"
        //coroutines = "1.10.1"
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
    maven("https://jitpack.io") // ItemsAdder
    maven("https://r.irepo.space/maven/") // NeigeItems
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/") // MMOItems
    maven("https://repo.oraxen.com/releases") // Oraxen
    maven("https://repo.momirealms.net/releases/") // CraftEngine
    maven {
        url = uri("https://nexus.maplex.top/repository/maven-public/")
         isAllowInsecureProtocol = true
    }
    mavenLocal()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    /** 发光工具 **/
    compileOnly("net.kyori:adventure-api:4.19.0")
    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")
    /** adyeshach  **/
    compileOnly("ink.ptms:adyeshach:2.0.4")
    /** MythicMobs **/
    compileOnly("ink.ptms:um:1.1.5")
    /** NMS 工具 **/
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    /** 物品库 Hook **/
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")
    compileOnly("pers.neige.neigeitems:NeigeItems:1.15.96")
    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")
    compileOnly("io.th0rgal:oraxen:1.171.0")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")
    compileOnly("net.momirealms:craft-engine-core:0.0.22")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.22")
    /** 数学表达式 **/
    compileOnly("com.notkamui.libs:keval:1.1.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven("https://nexus.maplex.top/repository/maven-releases/") {
            isAllowInsecureProtocol = true
            credentials {
                username = project.findProperty("NEXUS_MAPLEX_USERNAME")?.toString() ?: System.getenv("NEXUS_MAPLEX_USERNAME")
                password = project.findProperty("NEXUS_MAPLEX_PASSWORD")?.toString() ?: System.getenv("NEXUS_MAPLEX_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            from(components.findByName("java"))
//            artifactId = "Arim"
//            groupId = "top.maplex.arim"
            version = project.version.toString()
            println("Publishing version $version")
            println("Publishing artifactId $artifactId")
            println("Publishing groupId $groupId")

        }
    }
}
