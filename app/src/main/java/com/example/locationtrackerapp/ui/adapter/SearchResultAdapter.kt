package com.example.locationtrackerapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtrackerapp.databinding.ItemSearchResultBinding
import com.example.locationtrackerapp.model.CustomPlace

class SearchResultAdapter(private val onLocationClick: (CustomPlace) -> Unit) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
     var places: List<CustomPlace> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
        holder.itemView.setOnClickListener {
            onLocationClick(place)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(places: List<CustomPlace>) {
        this.places = places
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: CustomPlace) {
            binding.textViewName.text = place.name
        }
    }
}
