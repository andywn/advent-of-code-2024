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
    implementation(project(":common"))
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