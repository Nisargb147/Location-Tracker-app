package com.example.locationtrackerapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtrackerapp.databinding.ItemLocationBinding
import com.example.locationtrackerapp.model.LocationEntity

class LocationAdapter(
    var locations: List<LocationEntity>,
    private val onClick: (LocationEntity) -> Unit,
    private val onDelete: (LocationEntity) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
        holder.itemView.setOnClickListener { onClick(location) }
        holder.binding.imageViewDelete.setOnClickListener {
            onDelete(location)
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLocations(newLocations: List<LocationEntity>) {
        locations = newLocations
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationEntity) {
            binding.textViewName.text = location.name
        }
    }
}