package com.matthew.cakelistapp.di.modules

import com.google.gson.Gson
import com.matthew.cakelistapp.BuildConfig
import com.matthew.cakelistapp.network.CakeListService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * API Specific Dependancies
 *
 * @author Matthew Howells
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_CAKE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideHubServiceApi(retrofit: Retrofit): CakeListService {
        return retrofit.create(CakeListService::class.java)
    }
}