package com.example.zobipractical.ui.dashboard.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zobipractical.db.entity.PostEntity
import com.example.zobipractical.db.repository.PostsDatabaseRepository
import com.example.zobipractical.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsRepository: PostsRepository, private val postsDatabaseRepository: PostsDatabaseRepository) : ViewModel() {

    val isLoadingObservableBoolean = ObservableBoolean(false)

    private var isNetworkAvailable = false

    private val _listOfPostsStateFlow = MutableStateFlow(emptyList<PostEntity>())
    val listOfPostsStateFlow: StateFlow<List<PostEntity>> = _listOfPostsStateFlow

    fun checkNetworkOnAppOpen(mContext: Context): Boolean{
        return if (Utils.getConnectivityStatus(mContext)){
            true
        } else{
            isLoadingObservableBoolean.set(false)
            false
        }
    }

    fun networkChangedBasedDataPopulation(mContext: Context){
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Toast.makeText(mContext, "Internet found!", Toast.LENGTH_SHORT).show()
                populateList()
                isNetworkAvailable = true
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                Toast.makeText(mContext, "Connection lost!", Toast.LENGTH_LONG).show()
                populateListFromDatabase()
                isNetworkAvailable = false
            }
        }

        val connectivityManager = mContext.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun onRefresh(){
        isLoadingObservableBoolean.set(true)
        if (isNetworkAvailable){
            populateList()
        } else{
            populateListFromDatabase()
        }
    }

    fun populateList(){
        getPosts()
        viewModelScope.launch {
            getAllPosts().collectLatest {
                _listOfPostsStateFlow.emit(it)
                isLoadingObservableBoolean.set(false)
            }
        }
    }

    fun populateListFromDatabase(){
        viewModelScope.launch {
            getAllPosts().collectLatest {
                if (it.isNotEmpty()){
                    _listOfPostsStateFlow.emit(it)
                    isLoadingObservableBoolean.set(false)
                } else{
                    isLoadingObservableBoolean.set(false)
                }
            }
        }
    }

    private fun getPosts(){
        viewModelScope.launch {
            val response = postsRepository.getPosts()
            if (response.isSuccessful){
                val listOfPosts = response.body()
                if (listOfPosts != null) {
                    insertAllPosts(listOfPosts)
                    isLoadingObservableBoolean.set(false)
                }
            }
        }
    }

    private suspend fun insertAllPosts(listOfPosts: List<PostEntity>){
        return postsDatabaseRepository.insertAllPosts(listOfPosts)
    }

    private fun getAllPosts(): Flow<List<PostEntity>>{
        return postsDatabaseRepository.getAllPosts()
    }
}