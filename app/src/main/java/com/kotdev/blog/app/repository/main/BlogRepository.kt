package com.kotdev.blog.app.repository.main

import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.models.BlogPost
import com.kotdev.blog.app.ui.fragments.main.blog.state.BlogViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.di.main.MainScope
import com.kotdev.blog.app.repository.DataState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@FlowPreview
@MainScope
interface BlogRepository {

    fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent,
    ): Flow<DataState<BlogViewState>>

    fun isAuthorOfBlogPost(
        authToken: AuthToken,
        slug: String,
        stateEvent: StateEvent,
    ): Flow<DataState<BlogViewState>>

    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost,
        stateEvent: StateEvent,
    ): Flow<DataState<BlogViewState>>

    fun updateBlogPost(
        authToken: AuthToken,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
        stateEvent: StateEvent,
    ): Flow<DataState<BlogViewState>>

}