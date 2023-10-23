package com.amazon.styledictionaryexample.democompose.network

import retrofit2.Response
import retrofit2.http.GET

interface DemoComposeService {

  @GET("get")
  suspend fun retrieveStyleJson(): Response<String>
}
