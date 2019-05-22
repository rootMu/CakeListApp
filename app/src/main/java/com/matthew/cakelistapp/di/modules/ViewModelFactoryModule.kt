package com.matthew.cakelistapp.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.matthew.cakelistapp.di.qualifier.ViewModelKey
import com.matthew.cakelistapp.modules.di.ViewModelFactory
import com.matthew.cakelistapp.modules.list.viewmodel.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Binds view model factory
 *
 * @author Matthew Howells
 */
@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel

}