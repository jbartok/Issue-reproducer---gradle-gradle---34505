plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.reproducer"
version = "1.0"

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation(gradleTestKit())
}

val repoDir = layout.buildDirectory.dir("repo")

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = repoDir.get().asFile.toURI()
        }
    }
}

tasks.test {
    dependsOn(tasks.publish)
    inputs.dir(repoDir)
}

gradlePlugin {
    plugins {
        create("my-plugin") {
            id = "my-plugin"
            implementationClass = "com.reproducer.MyPlugin"
        }
    }
}