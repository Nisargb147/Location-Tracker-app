package com.example.locationtrackerapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtrackerapp.databinding.FragmentEditLocationBinding
import com.example.locationtrackerapp.model.CustomPlace
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.ui.adapter.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationEditFragment : BaseFragment() {
    private lateinit var binding: FragmentEditLocationBinding
    private var locationId: Int = 0
    private var currentLocation: LocationEntity? = null
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationId = arguments?.getInt("locationId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setEditTextSearch()
        observeData()
        setOnClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerView() {
        searchResultAdapter = SearchResultAdapter { place ->
            viewModel.getPlaceDetails(place.placeId).observe(viewLifecycleOwner) { customPlace ->
                customPlace?.let {
                    currentLocation = LocationEntity(
                        id = locationId,
                        name = it.name,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        distance = 0f
                    )
                    binding.textViewLocationName.text = "Current Location : ${it.name}"
                }
            }
        }
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearch.adapter = searchResultAdapter
    }

    private fun setEditTextSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.searchLocations(s.toString()).observe(viewLifecycleOwner) { results ->
                        val customPlaces = results.map { place ->
                            CustomPlace(
                                placeId = place.placeId,
                                name = place.name ?: ""
                            )
                        }
                        searchResultAdapter.submitList(customPlaces)
                    }
                } else {
                    searchResultAdapter.submitList(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setOnClickListener() {
        binding.btnSave.setOnClickListener {
            currentLocation?.let { location ->
                viewModel.updateLocation(location)
                parentFragmentManager.popBackStack()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.getLocationById(locationId).observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                currentLocation = it
                binding.textViewLocationName.text = "Current Location : ${it.name}"
            }
        })
    }
}
