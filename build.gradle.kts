plugins {
  id("java-library")
}

group = "dev.hireben.demo"
version = "1.0.0-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot:3.5.3")
  implementation("org.springframework:spring-webmvc:6.2.8")
  compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
  implementation("jakarta.validation:jakarta.validation-api:3.1.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.19.1")
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  implementation("org.slf4j:slf4j-api:2.0.17")
  compileOnly("org.projectlombok:lombok:1.18.38")
  annotationProcessor("org.projectlombok:lombok:1.18.38")
  testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}