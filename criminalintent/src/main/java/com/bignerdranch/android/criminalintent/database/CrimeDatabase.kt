package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.criminalintent.Crime

// first param= list of entity classes
// version= keep track of change
@Database(entities = [ Crime::class], version=2)
@TypeConverters(CrimeTypeConverters::class)  // explicit add converter
abstract class CrimeDatabase: RoomDatabase() {

    // register DAO, so that Room implements the DAO functions
    abstract fun crimeDao(): CrimeDao
}

val migration_1_2 = object: Migration(1, 2) {
    override fun migrate(database:SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}