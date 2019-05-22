package com.matthew.cakelistapp.room

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.matthew.cakelistapp.R
import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess

/**
 * worker handler to takes care or putting
 * the queries and updates in worker thread.
 *
 * @author Matthew Howells
 */
class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: () -> Runnable) {
        mWorkerHandler.post(task.invoke())
    }

}