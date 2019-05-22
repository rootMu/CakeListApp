package com.matthew.cakelistapp.di.components

import android.app.Application
import com.matthew.cakelistapp.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

/**
 * @author Matthew Howells.
 */

@Singleton
@Component(modules = [
AndroidSupportInjectionModule::class,
BuildersModule::class,
ViewModelFactoryModule::class,
ApiModule::class,
HttpModule::class,
AppModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}