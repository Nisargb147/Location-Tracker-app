package com.example.locationtrackerapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtrackerapp.databinding.FragmentAddLocationBinding
import com.example.locationtrackerapp.model.CustomPlace
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.ui.adapter.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationAddFragment: BaseFragment() {
    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setEditTextSearch()
        binding.textViewSearchHint.visibility = View.VISIBLE
    }

    private fun setRecyclerView() = with(binding) {
        searchResultAdapter = SearchResultAdapter { place ->
            viewModel.getPlaceDetails(place.placeId).observe(viewLifecycleOwner) { customPlace ->
                customPlace?.let {
                    val locationEntity = LocationEntity(
                        name = it.name,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        distance = 0f
                    )
                    viewModel.insertLocation(locationEntity)
                    navigateGoBack()
                }
            }
        }
        recyclerViewSearch.layoutManager = LinearLayoutManager(context)
        recyclerViewSearch.adapter = searchResultAdapter
    }

    private fun setEditTextSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.textViewSearchHint.visibility = View.GONE
                    viewModel.searchLocations(s.toString()).observe(viewLifecycleOwner) { results ->
                        val customPlaces = results.map { place ->
                            CustomPlace(
                                placeId = place.placeId,
                                name = place.name ?: ""
                            )
                        }
                        searchResultAdapter.submitList(customPlaces)
                        Log.d("HHH", "onTextChanged() called with: results = $customPlaces")
                    }
                } else {
                    binding.textViewSearchHint.visibility = View.VISIBLE
                    searchResultAdapter.submitList(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
