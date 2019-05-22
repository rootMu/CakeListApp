package com.matthew.cakelistapp.model.cake

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Model class for individual cakes from the cake response data
 *
 * @author Matthew Howells
 */

@Parcelize
class Cake(
        @SerializedName("title") val title: String = "",
        @SerializedName("desc") val description: String = "",
        @SerializedName("image") val image: String = "",
        var favourite: Boolean = false

) : Parcelable