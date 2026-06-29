plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

javafx {
    version = "24.0.2"
    modules = listOf(
        "javafx.controls",
        "javafx.fxml"
    )
}

application {
    mainClass.set("QuadraticCalculatorDriver")
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}