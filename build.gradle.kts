plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
  `maven-publish`
}

android {
  namespace = "dev.mango.labs.auth.biometric"
  compileSdk = 36

  defaultConfig {
    minSdk = 23
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlin {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
  }
  testOptions {
    unitTests.isIncludeAndroidResources = true
  }
  publishing {
    singleVariant("release") {
      withSourcesJar()
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.biometric)

  testImplementation(libs.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.core)
}

publishing {
  publications {
    register<MavenPublication>("release") {
      groupId = "dev.mango.labs"
      artifactId = "biometric-auth"
      version = "1.0.0"

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
