package com.amin.buildsrc

object Dependencies {

  const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.0-beta03"

  const val timber = "com.jakewharton.timber:timber:4.7.1"

  const val junit = "junit:junit:4.13.1"
  const val mockK = "io.mockk:mockk:1.10.2"

  const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.5"

  const val material = "com.google.android.material:material:1.1.0"

  object Kotlin {
    private const val version = "1.4.20"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
  }

  object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"

    object Test {
      private const val version = "1.3.0"
      const val core = "androidx.test:core:$version"
      const val rules = "androidx.test:rules:$version"

      object Ext {
        private const val version = "1.1.2"
        const val junit = "androidx.test.ext:junit-ktx:$version"
      }

      const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
    }

    const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"

    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.2"

    const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha04"

    object Lifecycle {
      private const val version = "2.3.0-beta01"
      const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
      const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    }
  }
}