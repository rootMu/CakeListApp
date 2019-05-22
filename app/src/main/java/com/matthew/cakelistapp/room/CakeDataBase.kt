package com.matthew.cakelistapp.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Room Database to store [CakeData]
 *
 * @author Matthew Howells
 */
@Database(entities = [CakeData::class], version = 1)
abstract class CakeDataBase : RoomDatabase() {

    abstract fun cakeDataDao(): CakeDataDao

    companion object {
        private var INSTANCE: CakeDataBase? = null

        fun getInstance(context: Context): CakeDataBase? {
            if (INSTANCE == null) {
                synchronized(CakeDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CakeDataBase::class.java, "cake.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}