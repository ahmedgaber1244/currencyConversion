package com.task.currencyconversion.data.remote

import com.task.currencyconversion.data.model.LatestRes
import com.task.currencyconversion.data.model.SymbolsRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET("latest")
    suspend fun latest(@Query("access_key") key: String): Response<LatestRes>

    @GET("symbols")
    suspend fun symbols(@Query("access_key") key: String): Response<SymbolsRes>
}