package com.matthew.cakelistapp.modules.list.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.matthew.cakelistapp.ListAdapter
import com.matthew.cakelistapp.R
import com.matthew.cakelistapp.model.cake.Cake
import com.matthew.cakelistapp.modules.list.viewmodel.ListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*
import javax.inject.Inject

import android.content.res.Configuration
import android.os.Handler
import android.support.design.widget.Snackbar


/**
 * @author Matthew Howells
 * TODO Implement more filter options to take advantage of favourites
 */

class ListActivity : DaggerAppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val TAG: String = this.toString()
        private const val KEY_RECYCLER_STATE = "recycler_state"
        private const val KEY_FAVOURITE_RECYCLER_STATE = "favourite_recycler_state"
    }

    private var mBundleRecyclerViewState: Bundle? = null
    private var mListState : Parcelable?  = null
    private var mFavouriteListState : Parcelable?  = null

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mCakeList: ArrayList<Cake> = ArrayList()
    private var mFavouriteList: ArrayList<Cake> = ArrayList()

    var mViewModel: ListViewModel? = null

    private val favourite = { cake: Cake -> favouriteCake(cake) }
    private val onClick = { cake: Cake -> onClick(cake) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initialiseViews()
        initialiseViewModel()
        initialiseRoom()
        listenToLiveData()
        listenToFavouriteLiveData()
        listenToRemoveFavouriteLiveData()

        /**
         * Animation won't start on onCreate so post runnable
         * used to start refresh
         */
        swipe_container.post {
            swipe_container.isRefreshing = true
            // Fetching data from "server"
            getCakes()
        }

    }

    override fun onPause() {
        super.onPause()
        mListState = list.layoutManager?.onSaveInstanceState()
        mBundleRecyclerViewState = Bundle().apply {
            putParcelable(KEY_RECYCLER_STATE, mListState)
            putParcelable(KEY_FAVOURITE_RECYCLER_STATE, mFavouriteListState)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (mBundleRecyclerViewState != null) {
            Handler().postDelayed({
                mListState = mBundleRecyclerViewState?.getParcelable(KEY_RECYCLER_STATE)
                mFavouriteListState = mBundleRecyclerViewState?.getParcelable(KEY_FAVOURITE_RECYCLER_STATE)
                list.layoutManager?.onRestoreInstanceState(mListState)
            }, 50)
        }

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            (list.layoutManager as GridLayoutManager).spanCount = 3
        }else {
            (list.layoutManager as GridLayoutManager).spanCount = 2
        }
    }

    private var gridLayoutManager: GridLayoutManager? = null
    /**
     *  Initialises the ui
     */
    private fun initialiseViews() {
        swipe_container.setOnRefreshListener(this)
        swipe_container.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)


        //set recyclerview
        gridLayoutManager = GridLayoutManager(this, 2)
        list.layoutManager = gridLayoutManager
        list.adapter = ListAdapter(mCakeList, favourite, onClick )
        list.setHasFixedSize(true)
    }

    /**
     *  Initialises the view model [mViewModel] or reuses the existing one
     */
    private fun initialiseViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListViewModel::class.java)
    }

    /**
     *  Initialises Room DataBase
     */
    private fun initialiseRoom() {
        mViewModel?.initialiseRoom(this)
    }

    /**
     * starts listening to live data changes
     */
    private fun listenToLiveData() {
        mViewModel?.mCakeLiveData?.observe(this, Observer { cakeData ->
            cakeData?.let{cakes ->
                if(cakes.isEmpty()){
                    Log.d(TAG, "the cake is a lie")
                }else{
                    mFavouriteList.clear()
                    mCakeList.apply {
                        clear()
                        cakes.forEach { add(it) }
                    }
                    list.adapter?.notifyDataSetChanged()
                    swipe_container.isRefreshing = false
                }
            }
        })
    }

    /**
     * starts listening to favourite live data changes
     */
    private fun listenToFavouriteLiveData() {
        mViewModel?.mFavouriteLiveData?.observe(this, Observer {

            mCakeList.filter { cake -> it?.any { favourite -> favourite.title == cake.title }?:false }.forEach {cake ->
                cake.favourite = true
                mFavouriteList.add(cake)
            }

            list.adapter?.notifyDataSetChanged()
        })
    }

    /**
     * starts listening to remove favourite live data changes
     */
    private fun listenToRemoveFavouriteLiveData() {
        mViewModel?.mRemoveFavouriteLiveData?.observe(this, Observer { favourite ->
            //required to reset favourite
            favourite?.let {
                mFavouriteList.find { cake -> cake.title == it.title }?.apply{
                    this.favourite = false
                    mFavouriteList.remove(this)
                }
            }
            list.adapter?.notifyDataSetChanged()
        })
    }

    /**
     * asks view model to favourite the clicked cake
     */
    private fun favouriteCake(cake: Cake) {
        mViewModel?.favouriteCake(cake)
    }

    /**
     * display the cake description
     * TODO implement a popup or dialog to display this in more detail
     */
    private fun onClick(cake: Cake) {
        Log.d(TAG, "Clicked ${cake.title}")
        Snackbar.make(contraint, cake.description, Snackbar.LENGTH_LONG).show()
    }


    /**
     * asks view model to get list of cakes
     */
    private fun getCakes(){
        mViewModel?.fetchCakes()
    }

    /**
     * called whenever the swipe refresh is needing to refresh
     */
    override fun onRefresh() {
        getCakes()
    }



}
