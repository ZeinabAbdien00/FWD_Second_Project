package com.udacity.asteroidradar.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.CardViewBinding
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.model.Asteroid
import kotlinx.android.synthetic.main.card_view.view.*

class Adapter (private var arrayList: List<Asteroid>) :
    RecyclerView.Adapter<Adapter.RecyclerViewHolder>() {

    private lateinit var myInterface : OnItemClick
    inner class RecyclerViewHolder(item: View, onClickInterface : OnItemClick) : RecyclerView.ViewHolder(item) {

        init {
            item.setOnClickListener{
                onClickInterface.OnItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return RecyclerViewHolder(view,myInterface)

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int ) {
        holder.itemView.apply {
            asteroid_name.text=arrayList[position].codename
            asteroid_date.text=arrayList[position].closeApproachDate
            if (arrayList[position].isPotentiallyHazardous)
            {
                asteroid_image.setImageResource(R.drawable.ic_status_potentially_hazardous)
            }else
            {
                asteroid_image.setImageResource(R.drawable.ic_status_normal)
            }
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setOnItemClick(listener : OnItemClick){
        myInterface=listener
    }

    interface OnItemClick{
        fun OnItemClick(position : Int)
    }
}