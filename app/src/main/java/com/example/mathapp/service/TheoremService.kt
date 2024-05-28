package com.example.mathapp.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheoremService {
    @GET("theorem")
    suspend fun getTheorem(@Query("name") name: String): Response<TheoremResponse>
}

data class TheoremResponse(
    val theorem: String?,
    val error: String?
)
