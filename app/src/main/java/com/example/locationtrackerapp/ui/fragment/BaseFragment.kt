package com.example.locationtrackerapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.locationtrackerapp.ui.activity.BaseActivity
import com.example.locationtrackerapp.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var pathDraw: PathDraw

    val viewModel: LocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadFragment(
        fragment: Fragment,
        isAdd: Boolean,
        isBackStack: Boolean,
        bundle: Bundle? = null
    ) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).loadFragment(fragment, isAdd, isBackStack, bundle)
        }
    }

    fun navigateGoBack() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).goBack()
        }
    }
}