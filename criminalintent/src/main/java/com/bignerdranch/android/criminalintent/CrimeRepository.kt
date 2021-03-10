package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import com.bignerdranch.android.criminalintent.database.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

// UI request data from repo, which contains the implementations
// Singleton stays as long as the app stays in memory
class CrimeRepository private constructor(context: Context) {

    // create concrete impl of the abstract CrimeDB
    // context = for accessing FS
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME   //name of DB file to be created
    )
        // apply change
        .addMigrations(migration_1_2)
        .build()

    private val crimeDao = database.crimeDao()

    // ref a thread, executor.execute() takes a code to be executed in that thread
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime:Crime) {
        executor.execute{
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime:Crime) {
        executor.execute{
            crimeDao.addCrime(crime)
        }
    }

    // Singleton
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized.")
        }
    }
}