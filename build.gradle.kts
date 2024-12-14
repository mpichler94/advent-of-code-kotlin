plugins {
    kotlin("jvm") version "2.1.0"
    application

    // id("io.gitlab.arturborsch.detekt") version "1.23.0"
}

group = "at.pichler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // lib
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.12")
    implementation("org.jsoup:jsoup:1.18.3")

    testImplementation(kotlin("test"))
    implementation("io.kotest:kotest-runner-junit5:5.9.1")

    // solutions
    implementation("org.jetbrains.kotlinx:multik-core:0.2.3")
    implementation("org.jetbrains.kotlinx:multik-default:0.2.3")
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
