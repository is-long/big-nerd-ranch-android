package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.criminalintent.Crime

// first param= list of entity classes
// version= keep track of change
@Database(entities = [ Crime::class], version=1, exportSchema = true)
@TypeConverters(CrimeTypeConverters::class)  // explicit add converter
abstract class CrimeDatabase: RoomDatabase() {

    // register DAO, so that Room implements the DAO functions
    abstract fun crimeDao(): CrimeDao
}