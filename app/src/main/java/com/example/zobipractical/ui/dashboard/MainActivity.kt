package com.example.zobipractical.ui.dashboard

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.zobipractical.databinding.ActivityMainBinding
import com.example.zobipractical.db.entity.PostEntity
import com.example.zobipractical.ui.dashboard.data.PostsViewModel
import com.example.zobipractical.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var adapter: PostsListAdapter
    var listOfPosts = mutableListOf<PostEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        if (Utils.getConnectivityStatus(this)){
            setRecyclerView()
        } else{
            Toast.makeText(this, "No internet connected!", Toast.LENGTH_SHORT).show()
        }
        networkObserverDataPopulation()
    }

    private fun setRecyclerView(){
        adapter = PostsListAdapter(listOfPosts)
        binding.recyclerViewPosts?.adapter = adapter
    }

    private fun networkObserverDataPopulation(){
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Toast.makeText(this@MainActivity, "Internet found!", Toast.LENGTH_SHORT).show()
                populateList()
                binding.swipeRefreshPosts?.setOnRefreshListener{
                    populateList()
                    binding.swipeRefreshPosts?.isRefreshing = false
                }
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                Toast.makeText(this@MainActivity, "Connection lost!", Toast.LENGTH_LONG).show()
                populateListFromDatabase()
                binding.swipeRefreshPosts?.setOnRefreshListener {
                    populateListFromDatabase()
                    binding.swipeRefreshPosts?.isRefreshing = false
                }
            }
        }

        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }


    fun populateList(){
        viewModel.getPosts()
        lifecycleScope.launch {
            viewModel.getAllPosts().collectLatest {
                listOfPosts.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun populateListFromDatabase(){
        lifecycleScope.launch {
            viewModel.getAllPosts().collectLatest {
                if (it.isNotEmpty()){
                    listOfPosts.addAll(it)
                    adapter.notifyDataSetChanged()
                } else{
                    Toast.makeText(this@MainActivity, "Database is empty!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}