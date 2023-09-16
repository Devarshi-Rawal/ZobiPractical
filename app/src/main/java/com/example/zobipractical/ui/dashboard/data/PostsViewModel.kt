package com.example.zobipractical.ui.dashboard.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zobipractical.db.entity.PostEntity
import com.example.zobipractical.db.repository.PostsDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsRepository: PostsRepository, private val postsDatabaseRepository: PostsDatabaseRepository) : ViewModel() {

    fun getPosts(){
        viewModelScope.launch {
            val response = postsRepository.getPosts()
            if (response.isSuccessful){
                val listOfPosts = response.body()
                if (listOfPosts != null) {
                    val ids = insertAllPosts(listOfPosts)
                }
            }
        }
    }

    private suspend fun insertAllPosts(listOfPosts: List<PostEntity>): List<Long>{
        return postsDatabaseRepository.insertAllPosts(listOfPosts)
    }

    fun getAllPosts(): Flow<List<PostEntity>>{
        return postsDatabaseRepository.getAllPosts()
    }
}