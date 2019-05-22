package com.matthew.cakelistapp.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides

/**
 * General application dependencies
 *
 * @author Matthew Howells
 */
@Module
class AppModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

}