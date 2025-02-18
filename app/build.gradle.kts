import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.spotless)
  alias(libs.plugins.detekt)
}

android {
  namespace = "ru.kode.plexus"
  compileSdk = 34

  defaultConfig {
    applicationId = "ru.kode.plexus"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  detektPlugins(files(rootProject.file("libs/detekt-rules-1.4.0.jar")))
  detektPlugins(libs.detekt.rules.compose)

  implementation(libs.androidx.core.ktx)
  implementation(platform(libs.androidx.compose.bom))

  implementation(libs.plexus.core)

  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.lifecycle.runtime.ktx)

  debugImplementation(libs.androidx.ui.tooling)
  androidTestImplementation(platform(libs.androidx.compose.bom))
}

detekt {
  config.setFrom(rootProject.file("detekt-config.yml"))
  ignoredBuildTypes = listOf("release")
}

tasks.withType<Detekt>().configureEach {
  jvmTarget = "11"
  // "exclude(**/generated/**)" doesn't work for some reason, see
  // https://github.com/detekt/detekt/issues/4127#issuecomment-1260733842
  exclude { element -> // Use separator to make sure it also works on Windows
    val sep = File.separator
    element.file.absolutePath.contains("${sep}generated${sep}")
  }
  reports {
    xml.required.set(false)
    html.required.set(false)
    txt.required.set(false)
    sarif.required.set(false)
  }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
  jvmTarget = "11"
}
