package com.matthew.cakelistapp.modules.list.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.matthew.cakelistapp.model.cake.Cake
import com.matthew.cakelistapp.modules.list.ui.ListActivity
import com.matthew.cakelistapp.network.CakeListService
import com.matthew.cakelistapp.room.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.lang.Exception
import javax.inject.Inject

/**
 * view model for [ListActivity]
 * will handle api calls using LiveData
 *
 * @author Matthew Howells
 */
class ListViewModel @Inject constructor( val mCakeService: CakeListService) : ViewModel() {

    companion object {
        val TAG : String = ListViewModel::class.java.simpleName.toString()
    }

    private var mDb: CakeDataBase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread

    val mCakeLiveData = MutableLiveData<List<Cake>>()

    val mFavouriteLiveData = MutableLiveData<ArrayList<CakeData>>()
    val mRemoveFavouriteLiveData = MutableLiveData<CakeData>()

    private val mDisposable: CompositeDisposable = CompositeDisposable()

    private var mFavouriteList: ArrayList<CakeData> = ArrayList()

    /**
     * updates [mCakeLiveData] with [Cake] retrieved from "API" call
     */
    private fun updateCakeLiveData(cakes: List<Cake>?) {
        mCakeLiveData.postValue(cakes?.distinctBy { it.title }?.sortedBy { it.title })
    }

    /**
     * initialise room
     */
    fun initialiseRoom(context: Context){
        mDbWorkerThread = DbWorkerThread("dbWorkerThread").apply{
            start()
        }
        mDb = CakeDataBase.getInstance(context)
    }

    /**
     * add or remove [CakeData] from [CakeDataBase]
     */
    private fun insertOrDeleteCakeDataInDb(cakeData: CakeData) {

        mDbWorkerThread.postTask{
            Runnable {
                kotlin.runCatching {
                    //delete cake from database
                    //assert cake is there
                    //intended to fail when adding the cake
                    mDb?.cakeDataDao()?.delete(mDb?.cakeDataDao()?.findCakeWithTitle(cakeData.title)!!)
                }.onFailure {
                    //when cake is not in database
                    //insert it into database
                    Log.d(TAG,"favourite cake: ${cakeData.title}")
                    mDb?.cakeDataDao()?.insert(cakeData)
                    mFavouriteList.add(cakeData)
                    updateFavouriteLiveData()
                }.onSuccess {
                    //when cake was found and deleted from database
                    //find and remove it from mFavouriteList
                    mFavouriteList.find { cake -> cake.title == cakeData.title }?.apply{
                        Log.d(TAG,"remove favourite cake: ${cakeData.title}")
                        mFavouriteList.remove(this)
                        updateRemoveFavouriteLiveData(this)
                    }
                }
            }
        }
    }

    /**
     * retrieve [CakeData] from [CakeDataBase]
     */
    private fun fetchCakeDataFromDb() {
        mDbWorkerThread.postTask{Runnable {

            mDb?.cakeDataDao()?.getAll()?.apply {
                mFavouriteList.clear()
                forEach {
                    mFavouriteList.add(it)
                }
                updateFavouriteLiveData()
            }
        }}
    }

    /**
     * updates [mFavouriteLiveData] with [mFavouriteList]
     */
    private fun updateFavouriteLiveData() {
        mFavouriteLiveData.postValue(mFavouriteList)
    }

    /**
     * updates [mRemoveFavouriteLiveData] with [CakeData] to be removed
     */
    private fun updateRemoveFavouriteLiveData(cakeData: CakeData) {
        mRemoveFavouriteLiveData.postValue(cakeData)
    }


    /**
     * makes #getCakes "api" call
     * filters out duplicates
     * and orders by name
     * TODO display error dialog
     *  simple dialog fragment could work here
     */
    fun fetchCakes() {
        try{
            mCakeService.getCakes()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe{
                    updateCakeLiveData(it)
                    updateFavourites()
                }.addTo(mDisposable)
        }catch(e: Exception){
            Log.e(TAG, "There was an error loading the cakes", e)
        }
    }

    /**
     * @param cake [Cake] to be added to favourites
     */
    fun favouriteCake(cake: Cake) {

        val cakeData = CakeData()
        cakeData.title = cake.title
        insertOrDeleteCakeDataInDb(cakeData)
        //saves to favourites list
    }

    /**
     * call to retrieve favourites from Database
     */
    private fun updateFavourites() {
        fetchCakeDataFromDb()
    }

    /**
     * clears all disposables within CompositeDisposable [mDisposable]
     */
    override fun onCleared() {
        super.onCleared()
        mDbWorkerThread.quit()
        mDisposable.clear()
    }

}