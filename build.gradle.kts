group = "org.emurray.234PokerServer"
version = "1.0-SNAPSHOT"

plugins {
    java
    checkstyle
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val checkstyleConfig: Configuration by configurations.creating
dependencies {
    checkstyleConfig("com.puppycrawl.tools:checkstyle:9.3") {
        isTransitive = false
    }
}

configure<CheckstyleExtension> {
    configFile = File("config/checkstyle/checkstyle.xml")
    configDirectory.set(File(rootDir, "config/checkstyle"))
    configProperties = mapOf("suppressionFile" to configDirectory.get().file("checkstyle-suppressions.xml").asFile)
    toolVersion = "9.3"
    sourceSets = listOf(project.sourceSets["main"])
}


sourceSets.getByName("main") {
    java.srcDir("src/main/java")
}
sourceSets.getByName("test") {
    java.srcDir("src/test/java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
