// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    }

}

plugins {
    id ("com.android.application") version ("8.6.0") apply false
    id ("org.jetbrains.kotlin.android") version ("2.0.21") apply false
    id ("com.android.library") version ("8.0.2") apply false
    id ("org.jetbrains.kotlin.plugin.compose") version ("2.0.0") apply false
    id ("com.google.devtools.ksp") version ("2.0.21-1.0.25") apply false
    id("com.google.dagger.hilt.android") version ("2.51") apply false
}

