package com.amazon.styledictionaryexample.democompose

import android.app.Application
import com.amazon.styledictionaryexample.democompose.network.DemoComposeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class DemoComposeApplication : Application() {
  lateinit var demoComposeService: DemoComposeService
  override fun onCreate() {
    super.onCreate()
    instance = this
    demoComposeService = Retrofit.Builder().baseUrl("https://276b8b43-e61f-428a-9ee1-a835437ce37d.mock.pstmn.io/").addConverterFactory(
      ScalarsConverterFactory.create()).build().create(DemoComposeService::class.java)
  }

  companion object {
    lateinit var instance: DemoComposeApplication
      private set
  }
}
