package com.example.minimoneybox.viewmodel

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Unable to @Inject my ViewModel, needed to create a custom ViewModelFactory
 * (This class contains the annotation of the ViewModelKey
 * https://proandroiddev.com/viewmodel-with-dagger2-architecture-components-2e06f06c9455
 */

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)