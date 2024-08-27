package com.dicoding.truenoblestoryapp.loginwithanimation.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.ListStoryItem
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ItemStoryBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.view.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)

        if (story != null) {
            holder.binding.tvUsername.text = story.name

            Glide
                .with(holder.itemView.context)
                .load(story.photoUrl)
                .into(holder.binding.tvItemPhoto)

            holder.binding.tvItemDesc.text = story.description

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)

                intent.putExtra(DetailActivity.STORY_ID, story.id)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}