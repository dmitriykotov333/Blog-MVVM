package com.kotdev.blog.di.auth

import android.content.SharedPreferences
import com.kotdev.blog.app.api.auth.AuthService
import com.kotdev.blog.app.api.main.ApiMainService
import com.kotdev.blog.app.persistance.AccountPropertiesDao
import com.kotdev.blog.app.persistance.AuthTokenDao
import com.kotdev.blog.app.repository.auth.AuthRepository
import com.kotdev.blog.app.repository.auth.AuthRepositoryImpl
import com.kotdev.blog.app.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@FlowPreview
@Module
object AuthModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): AuthService {
        return retrofitBuilder
            .build()
            .create(AuthService::class.java)
    }

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: AuthService,
        sharedPreferences: SharedPreferences,
        sharedPreferencesEditor: SharedPreferences.Editor,
    ): AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            sharedPreferences,
            sharedPreferencesEditor
        )
    }


}



