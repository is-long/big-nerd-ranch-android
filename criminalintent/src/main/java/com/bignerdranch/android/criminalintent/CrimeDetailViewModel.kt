package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class CrimeDetailViewModel: ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    // currently displayed Crime ID
    // why wrap in LiveData?
    private val crimeIdLiveData = MutableLiveData<UUID>()

    // using transformation, CrimeFragment has to observe crimeLiveData only once
    // if fragment changes the ID to display, the viewModel publishes new data to existing stream
    var crimeLiveData: LiveData<Crime?> =
        // sets up trigger-response relationship between 2 LiveData obj
        // trigger is crimeIdLiveData
        // response is the LiveData query result, update each time new crimeId gets set
        Transformations.switchMap(crimeIdLiveData) {
            crimeId ->
            crimeRepository.getCrime(crimeId)
        }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }

    fun getPhotoFile(crime: Crime): File {
        return crimeRepository.getPhotoFile(crime)
    }
}