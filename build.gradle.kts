// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
    alias(libs.plugins.google.services) apply false

    // dependency for the Google services Gradle plugin
    // unnenecary since we already said alias(libs.plugins.google.services) apply false. We are able to do this because we have a version catalog
    // id("com.google.gms.google-services") version "4.6.0" apply false
}