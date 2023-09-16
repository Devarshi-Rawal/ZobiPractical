package com.example.zobipractical.ui.dashboard.data

import com.example.zobipractical.db.entity.PostEntity
import com.example.zobipractical.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class PostsRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPosts(): Response<List<PostEntity>>{
        return apiService.getPosts();
    }
}