package com.example.zobipractical.db.dao

import androidx.room.*
import com.example.zobipractical.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(listOfPosts: List<PostEntity>): List<Long>

    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<PostEntity>>
}