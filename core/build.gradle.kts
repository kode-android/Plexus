plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
  `maven-publish`
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(11)
  }
}

dependencies {
  implementation(libs.kotlin.coroutines.core)
}
