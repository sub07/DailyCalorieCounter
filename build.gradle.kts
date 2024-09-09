buildscript {
    val agp_version by extra("8.5.2")
}
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
}