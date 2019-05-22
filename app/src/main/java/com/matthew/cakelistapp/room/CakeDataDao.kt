package com.matthew.cakelistapp.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

/**
 * Room Data Access Object used to access [CakeData] from [CakeDataBase]
 *
 * @author Matthew Howells
 */
@Dao
interface CakeDataDao {

    @Query("SELECT * from cakeData")
    fun getAll(): List<CakeData>

    @Insert(onConflict = REPLACE)
    fun insert(cakeData: CakeData)

    @Query("SELECT * FROM cakeData WHERE title LIKE :title LIMIT 1")
    fun findCakeWithTitle(title: String): CakeData

    @Delete
    fun delete(cake: CakeData)

    @Delete
    fun deleteCakes(vararg cakes: CakeData)

    @Query("DELETE from cakeData")
    fun deleteAll()

}