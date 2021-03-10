package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.criminalintent.Crime
import java.util.*

// DAO = interface of fun for each db ops
@Dao
interface CrimeDao {

    // Query -> doesn't do any update to DB
    // return type = result of operations
    // if return LiveData, Room will exec query in BACKGROUND thread
    //  then LD sends data over to main thread + notify observers
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    // no need for params
    @Update
    fun updateCrime(crime: Crime)

    // no need for params
    @Insert
    fun addCrime(crime: Crime)
}