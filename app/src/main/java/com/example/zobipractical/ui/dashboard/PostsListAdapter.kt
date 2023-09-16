package com.example.zobipractical.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zobipractical.databinding.ItemPostBinding
import com.example.zobipractical.db.entity.PostEntity

class PostsListAdapter(private var listOfPosts: List<PostEntity>) : RecyclerView.Adapter<PostsListAdapter.PostsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsListViewHolder {
        return PostsListViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = listOfPosts.size

    override fun onBindViewHolder(holder: PostsListViewHolder, position: Int) {
        holder.bind(listOfPosts[position])
    }

    class PostsListViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(postsItem: PostEntity){
            binding.postsItem = postsItem
        }
    }
}