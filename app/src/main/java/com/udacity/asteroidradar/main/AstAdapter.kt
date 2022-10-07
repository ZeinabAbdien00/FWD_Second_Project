package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.CardViewBinding
import com.udacity.asteroidradar.model.Asteroid

class AstAdapter (private val clickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AstAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidViewHolder {
        return AsteroidViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class AsteroidViewHolder private constructor(private val binding: CardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid, clickItem: AsteroidClickListener) {
            binding.asteroid = item
            binding.clickListener = clickItem
            binding.executePendingBindings()
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardViewBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }

    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    class AsteroidClickListener(val clickListener: (Asteroid) -> Unit) {
        fun onClickAsteroid(data: Asteroid) = clickListener(data)
    }
}