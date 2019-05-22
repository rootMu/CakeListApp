package com.matthew.cakelistapp.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Room Data Object used in [CakeDataBase]
 *
 * @author Matthew Howells
 */
@Entity(tableName = "cakeData")
data class CakeData(@PrimaryKey(autoGenerate = true) var id: Int?,
                    @ColumnInfo(name = "title") var title: String,
                    @ColumnInfo(name = "description") var description: String
){
    constructor():this(null,"","")
}