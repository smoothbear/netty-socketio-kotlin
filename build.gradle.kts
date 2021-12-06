plugins {
    kotlin("jvm") version "1.5.21"
}

group = "io.smoothbear"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    val nettyVersion = "4.1.50.Final"
    implementation("io.netty:netty-buffer:$nettyVersion")
    implementation("io.netty:netty-common:$nettyVersion")
    implementation("io.netty:netty-transport:$nettyVersion")
    implementation("io.netty:netty-handler:$nettyVersion")
    implementation("io.netty:netty-codec-http:$nettyVersion")
    implementation("io.netty:netty-codec:$nettyVersion")
    implementation("io.netty:netty-transport-native-epoll:$nettyVersion")

    // For reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")

    implementation("org.slf4j:slf4j-api:1.7.32")
}
