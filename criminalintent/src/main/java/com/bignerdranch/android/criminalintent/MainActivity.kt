package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fragment Manager: adds fragment's view to hosting activity hierarchy
        //                   + handle fragment lifecycle
        //
        // when activity is destroyed, FM saves the list of fragments
        // when activity is recreated, the new FM retrieves the list and recreates the fragments
        val currentFragment =
            // note the FM identifies the fragment not directly, but by its container
            //  -> to handle case where one fragment is used in multiple places
            supportFragmentManager
                .findFragmentById(R.id.fragment_container)


        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                // fragment txn allows grouping multiple operation (add, rmv, detach, replace) together
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            // to go back to MainActivity
            .addToBackStack(null)  // null = name of the back stack state
            .commit()
    }
}