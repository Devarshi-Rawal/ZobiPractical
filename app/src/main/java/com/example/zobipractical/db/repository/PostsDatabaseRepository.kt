package com.example.zobipractical.db.repository

import com.example.zobipractical.db.dao.PostsDao
import com.example.zobipractical.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class PostsDatabaseRepository @Inject constructor(private val postsDao: PostsDao){
    suspend fun insertAllPosts(listOfPosts: List<PostEntity>){
        return postsDao.insertAllPosts(listOfPosts)
    }
    fun getAllPosts(): Flow<List<PostEntity>>{
        return postsDao.getAllPosts()
    }
}