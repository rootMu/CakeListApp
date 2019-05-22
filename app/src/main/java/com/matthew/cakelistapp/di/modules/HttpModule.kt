package com.matthew.cakelistapp.di.modules

import com.matthew.cakelistapp.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Http-specific dependencies
 *
 * @author Matthew Howells
 */
@Module
class HttpModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient().newBuilder()
        // timeout in making the initial connection; i.e. completing the TCP connection handshake
        okHttpClient.connectTimeout(10, TimeUnit.SECONDS)
        // timeout setPowerOn waiting to read data
        okHttpClient.readTimeout(10, TimeUnit.SECONDS)
        okHttpClient.retryOnConnectionFailure(true)
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(okHttpClient: OkHttpClient.Builder, logging: HttpLoggingInterceptor): OkHttpClient {
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(logging)
        }
        return okHttpClient.build()
    }
}