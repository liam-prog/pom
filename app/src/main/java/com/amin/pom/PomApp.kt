package com.amin.pom

import android.app.Application
import timber.log.Timber

class PomApp : Application() {

  override fun onCreate() {
    super.onCreate()
    configureLogger()
  }

  private fun configureLogger() {
    Timber.plant(Timber.DebugTree())
  }
}