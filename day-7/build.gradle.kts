plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "uk.andrewnorman"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223")
    // coroutines dependency is required for this particular definition
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    testImplementation(kotlin("test"))
}

application {
    mainClass = "uk.andrewnorman.MainKt"
}

tasks.run {

}

tasks.test {
    useJUnitPlatform()
}