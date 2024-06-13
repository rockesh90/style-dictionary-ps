package com.amazon.styledictionaryexample.democompose

import android.app.Application

class DemoComposeApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    lateinit var instance: DemoComposeApplication
      private set
  }
}
