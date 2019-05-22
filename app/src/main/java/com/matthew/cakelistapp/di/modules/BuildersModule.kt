package com.matthew.cakelistapp.di.modules

import com.matthew.cakelistapp.modules.list.ui.ListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * All Activities being injected into
 * @author Matthew Howells
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindListActivity(): ListActivity

}