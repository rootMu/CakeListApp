package com.matthew.cakelistapp.network

import com.matthew.cakelistapp.model.cake.Cake
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * This contains all the api end points needed
 *
 * @author Matthew Howells
 */
interface CakeListService {

    @GET("waracle_cake-android-client")
    fun getCakes(): Flowable<ArrayList<Cake>?>
}

