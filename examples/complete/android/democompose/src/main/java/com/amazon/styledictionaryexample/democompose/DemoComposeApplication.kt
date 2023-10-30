package com.amazon.styledictionaryexample.democompose

import android.app.Application
import com.amazon.styledictionaryexample.democompose.network.DemoComposeService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class DemoComposeApplication : Application() {
  lateinit var demoComposeService: DemoComposeService
  override fun onCreate() {
    super.onCreate()
    instance = this
    demoComposeService = Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/alereyes2/style-dictionary-ps/main/examples/complete/android/styledictionary/src/main/").addConverterFactory(
      ScalarsConverterFactory.create(),
    ).build().create(DemoComposeService::class.java)
  }

  companion object {
    lateinit var instance: DemoComposeApplication
      private set
  }
}
