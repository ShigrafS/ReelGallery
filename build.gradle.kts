// Top-level build file where you can add configuration options common to all sub-projects/modules.
val media3Version = "1.2.0" // <-- latest stable Media3 version

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
