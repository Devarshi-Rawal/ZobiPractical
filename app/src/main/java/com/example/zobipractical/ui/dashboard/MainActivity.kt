package com.example.zobipractical.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.zobipractical.databinding.ActivityMainBinding
import com.example.zobipractical.ui.dashboard.data.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        viewModel.networkChangedBasedDataPopulation(this)
        if (!viewModel.checkNetworkOnAppOpen(this)){
            Toast.makeText(this, "No internet connected!", Toast.LENGTH_SHORT).show()
        }
        setRecyclerView()
    }
    private fun setRecyclerView(){
        lifecycleScope.launch {
            viewModel.listOfPostsStateFlow.collectLatest {
                binding.recyclerViewPosts.adapter = PostsListAdapter(it)
            }
        }
    }
}