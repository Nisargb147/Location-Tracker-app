package com.example.locationtrackerapp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.databinding.ActivityMainBinding
import com.example.locationtrackerapp.ui.fragment.LocationListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            loadFragment(LocationListFragment(), isAdd = false, isBackStack = false)
        }
    }

    override fun findFragmentPlaceHolder(): Int {
        return R.id.fragment_container
    }
}