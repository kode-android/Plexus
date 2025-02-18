// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

allprojects {
  buildscript {
    repositories {
      google()
      mavenCentral()
    }
  }
  repositories {
    google()
    mavenCentral()
    mavenLocal()
  }
}

subprojects {
  plugins.withType<MavenPublishPlugin> {
    apply(plugin = "org.gradle.signing")

    configure<PublishingExtension> {
      repositories {
        mavenLocal()
        maven {
          name = "MavenCentral"
          val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
          val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
          val versionName: String by project
          url = if (versionName.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
          credentials {
            username = project.findProperty("NEXUS_USERNAME")?.toString()
            password = project.findProperty("NEXUS_PASSWORD")?.toString()
          }
        }
      }

      publications.create<MavenPublication>("maven") {
        val versionName: String by project
        val pomGroupId: String by project
        groupId = pomGroupId
        version = versionName

        from(components["java"])

        pom {
          val pomUrl: String by project
          val pomDescription: String by project
          url.set(pomUrl)
          description.set(pomDescription)
        }
      }

      configure<SigningExtension> {
        sign(publications)
      }
    }
  }
}