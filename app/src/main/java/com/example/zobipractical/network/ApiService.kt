package com.example.zobipractical.network

import com.example.zobipractical.db.entity.PostEntity
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<PostEntity>>
}