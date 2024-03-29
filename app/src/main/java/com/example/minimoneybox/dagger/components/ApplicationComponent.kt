package com.example.minimoneybox.dagger.components

import android.app.Application
import com.example.minimoneybox.MainApplication
import com.example.minimoneybox.dagger.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ActivityBindingModule::class,
        ApiModule::class,
        ApplicationModule::class,
        ViewModelModule::class,
        RoomModule::class,
        AndroidSupportInjectionModule::class]
)
interface ApplicationComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    override fun inject(application: MainApplication)
}