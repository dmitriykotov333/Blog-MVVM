package com.kotdev.blog.di


import android.app.Application
import com.kotdev.blog.app.session.SessionManager
import com.kotdev.blog.app.ui.activities.BaseActivity
import com.kotdev.blog.di.auth.AuthComponent
import com.kotdev.blog.di.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent  {

    val sessionManager: SessionManager

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory

}