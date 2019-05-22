package com.matthew.cakelistapp.di.qualifier

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * @author Matthew Howells
 */

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)