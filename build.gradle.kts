plugins {
    kotlin("jvm") version "1.9.20"
    application

    //id("io.gitlab.arturborsch.detekt") version "1.23.0"
}

group = "at.pichler"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    // lib
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.9")
    implementation("org.jsoup:jsoup:1.15.3")

    testImplementation(kotlin("test"))
    implementation("io.kotest:kotest-runner-junit5:5.6.2")

    // solutions
    implementation("org.jetbrains.kotlinx:multik-core:0.2.2")
    implementation("org.jetbrains.kotlinx:multik-default:0.2.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}