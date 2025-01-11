plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.razomy"
version = "0.0.2-4"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.1.1")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
    }
}

intellijPlatform {
    pluginVerification {
        ides {
            recommended()
        }
    }
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(file("certificate/chain.crt").readText())
        privateKey.set(file("certificate/private.pem").readText())
        password.set(file("certificate/password.txt").readText())
    }

    publishPlugin {
        token.set(file("certificate/token.txt").readText())
    }
}
