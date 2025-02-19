plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
  alias(libs.plugins.dokka) apply true
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
