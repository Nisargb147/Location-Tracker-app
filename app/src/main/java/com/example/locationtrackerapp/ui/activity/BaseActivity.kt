package com.example.locationtrackerapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.ui.fragment.PathDraw
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity :AppCompatActivity(){
    @Inject
    lateinit var pathDraw: PathDraw

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract fun findFragmentPlaceHolder(): Int

    fun goBack(){
        onBackPressed()
    }

    fun loadFragment(
        fragment: Fragment,
        isAdd: Boolean,
        isBackStack: Boolean,
        bundle: Bundle? = null
    ) {
        val beginTransaction = supportFragmentManager.beginTransaction()

        bundle?.let {
            fragment.arguments = it
        }

        if (isAdd) {
            beginTransaction.add(
                R.id.fragment_container,
                fragment
            )
        } else {
            beginTransaction.replace(
                R.id.fragment_container,
                fragment
            )
        }

        if (isBackStack) {
            beginTransaction.addToBackStack(
                fragment::class.simpleName,
            )
        }

        beginTransaction.commit()
    }
}