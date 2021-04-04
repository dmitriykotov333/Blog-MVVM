package com.kotdev.blog.app.repository.main

import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.ui.fragments.main.create_blog.state.CreateBlogViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.di.main.MainScope
import com.kotdev.blog.app.repository.DataState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@FlowPreview
@MainScope
interface CreateBlogRepository {

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
        stateEvent: StateEvent
    ): Flow<DataState<CreateBlogViewState>>
}