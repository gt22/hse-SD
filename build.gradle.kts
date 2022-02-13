plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(platform("org.junit:junit-bom:5.8.1"))
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("junit:junit:4.12")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

application {
    mainClass.set("com.hse.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}