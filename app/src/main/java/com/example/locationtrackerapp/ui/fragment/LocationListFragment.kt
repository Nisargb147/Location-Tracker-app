package com.example.locationtrackerapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtrackerapp.databinding.FragmentLocationListBinding
import com.example.locationtrackerapp.model.LocationEntity
import com.example.locationtrackerapp.ui.adapter.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment: BaseFragment() {
    private lateinit var binding: FragmentLocationListBinding
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setRecyclerView()
        observeData()
    }

    private fun setOnClickListener()= with(binding){
        btnAddLocation.setOnClickListener {
            loadFragment(LocationAddFragment(),isAdd = false, isBackStack = true)
        }

        btnShowPath.setOnClickListener {
            loadFragment(MapFragment(), isAdd = false, isBackStack = true)
        }

        btnSortAscending.setOnClickListener {
            viewModel.toggleSortOrder(isAscending = true)
        }

        btnSortDescending.setOnClickListener {
            viewModel.toggleSortOrder(isAscending = false)
        }
    }

    private fun setRecyclerView()= with(binding){
        locationAdapter = LocationAdapter(emptyList(), { location ->
            //Edit navigation
            val bundle = Bundle().apply {
                putInt("locationId", location.id)
            }
            loadFragment(LocationEditFragment().apply { arguments = bundle }, isAdd = false, isBackStack = true)
        }, { location ->
            //Delete logic
            showDeleteConfirmationDialog(location)
        })

        recyclerViewLocation.layoutManager = LinearLayoutManager(context)
        recyclerViewLocation.adapter = locationAdapter
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showDeleteConfirmationDialog(location: LocationEntity) {
        val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Location")
            builder.setMessage("Are you sure you want to delete this location?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                // Perform delete action
                viewModel.deleteLocation(location)
                val updatedList = locationAdapter.locations.toMutableList()
                updatedList.remove(location)
                locationAdapter.updateLocations(updatedList)
            updateNoDataTextVisibility(updatedList.isEmpty())
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun observeData(){
        viewModel.locations.observe(viewLifecycleOwner) { locationList ->
            locationAdapter.updateLocations(locationList)
            updateNoDataTextVisibility(locationList.isEmpty())
        }
       //initial locations
        viewModel.getAllLocations()
    }

    private fun updateNoDataTextVisibility(isEmpty: Boolean) {
        binding.textViewNoData.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewLocation.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}