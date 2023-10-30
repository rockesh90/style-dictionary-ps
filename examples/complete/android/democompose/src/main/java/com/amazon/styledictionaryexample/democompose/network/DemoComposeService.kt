package com.amazon.styledictionaryexample.democompose.network

import retrofit2.Response
import retrofit2.http.GET

interface DemoComposeService {

  @GET("assets/data/properties.json")
  suspend fun retrieveStyleJson(): Response<String>
}
