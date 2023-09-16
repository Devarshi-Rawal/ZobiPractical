package com.example.zobipractical.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zobipractical.db.dao.PostsDao
import com.example.zobipractical.db.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class PostsDatabase : RoomDatabase(){
    abstract fun postsDao(): PostsDao
}