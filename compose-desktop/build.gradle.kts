import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    application
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
dependencies {
    implementation(compose.desktop.currentOs)

    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.components.resources)

    implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")

    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.13.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-jvm:0.13.0")
    implementation(project(":shared"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("MainKt")
}