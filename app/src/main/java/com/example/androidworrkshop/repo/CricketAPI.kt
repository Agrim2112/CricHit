package com.example.androidworrkshop.repo

import com.example.androidworrkshop.model.MatchInfo
import com.example.androidworrkshop.model.leanback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface CricketAPI {

    @Headers(
        "X-RapidAPI-Key:148e628a88msh7d9ed34390b2f26p109943jsn9bcd9beae9ba",
        "X-RapidAPI-Host:cricbuzz-cricket.p.rapidapi.com"
    )
    @GET("series/v1/6732")
    suspend fun getMatchList(): Response<MatchInfo>



    @Headers(
        "X-RapidAPI-Key:148e628a88msh7d9ed34390b2f26p109943jsn9bcd9beae9ba",
        "X-RapidAPI-Host:cricbuzz-cricket.p.rapidapi.com"
    )
    @GET("mcenter/v1/{id}/leanback")
    suspend fun getMatchScore(@Path("id") id: String): Response<leanback>


}