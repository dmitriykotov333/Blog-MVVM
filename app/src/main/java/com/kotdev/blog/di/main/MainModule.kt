package com.kotdev.blog.di.main

import com.kotdev.blog.app.api.main.ApiMainService
import com.kotdev.blog.app.persistance.AccountPropertiesDao
import com.kotdev.blog.app.persistance.AppDatabase
import com.kotdev.blog.app.persistance.BlogPostDao
import com.kotdev.blog.app.repository.main.AccountRepositoryImpl
import com.kotdev.blog.app.repository.main.BlogRepositoryImpl
import com.kotdev.blog.app.repository.main.CreateBlogRepositoryImpl
import com.kotdev.blog.app.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): ApiMainService {
        return retrofitBuilder
            .build()
            .create(ApiMainService::class.java)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        apiMain: ApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager,
    ): AccountRepositoryImpl {
        return AccountRepositoryImpl(apiMain, accountPropertiesDao, sessionManager)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDatabase): BlogPostDao {
        return db.getBlogPostDao()
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogRepository(
        apiMain: ApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager,
    ): BlogRepositoryImpl {
        return BlogRepositoryImpl(apiMain, blogPostDao, sessionManager)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideCreateBlogRepository(
        apiMain: ApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager,
    ): CreateBlogRepositoryImpl {
        return CreateBlogRepositoryImpl(apiMain, blogPostDao, sessionManager)
    }

}



