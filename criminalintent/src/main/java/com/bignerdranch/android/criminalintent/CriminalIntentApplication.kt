package com.bignerdranch.android.criminalintent

import android.app.Application

// must register class as android:name in manifest
class CriminalIntentApplication: Application() {

    // called by OS when app first loaded into memory
    // good place for one-time inits
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}