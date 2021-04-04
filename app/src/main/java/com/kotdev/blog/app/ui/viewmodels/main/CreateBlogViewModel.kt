package com.kotdev.blog.app.ui.viewmodels.main

import android.net.Uri
import com.kotdev.blog.app.repository.DataState
import com.kotdev.blog.app.repository.main.CreateBlogRepositoryImpl
import com.kotdev.blog.app.session.SessionManager
import com.kotdev.blog.app.ui.fragments.main.create_blog.state.CreateBlogStateEvent
import com.kotdev.blog.app.ui.fragments.main.create_blog.state.CreateBlogViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.app.ui.viewmodels.BaseViewModel
import com.kotdev.blog.di.main.MainScope
import com.kotdev.blog.app.repository.ErrorHandling.Companion.INVALID_STATE_EVENT
import com.kotdev.blog.app.repository.MessageType
import com.kotdev.blog.app.repository.Response
import com.kotdev.blog.app.repository.UIComponentType
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
@MainScope
class CreateBlogViewModel
@Inject
constructor(
    val createBlogRepository: CreateBlogRepositoryImpl,
    val sessionManager: SessionManager,
) : BaseViewModel<CreateBlogViewState>() {


    override fun handleNewData(data: CreateBlogViewState) {

        setNewBlogFields(
            data.blogFields.newBlogTitle,
            data.blogFields.newBlogBody,
            data.blogFields.newImageUri
        )
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<CreateBlogViewState>> = when (stateEvent) {

                is CreateBlogStateEvent.CreateNewBlogEvent -> {
                    val title = RequestBody.create(
                        MediaType.parse("text/plain"),
                        stateEvent.title
                    )
                    val body = RequestBody.create(
                        MediaType.parse("text/plain"),
                        stateEvent.body
                    )

                    createBlogRepository.createNewBlogPost(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        title = title,
                        body = body,
                        image = stateEvent.image
                    )
                }

                else -> {
                    flow {
                        emit(
                            DataState.error<CreateBlogViewState>(
                                response = Response(
                                    message = INVALID_STATE_EVENT,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
            launchJob(stateEvent, job)
        }
    }

    override fun initNewViewState(): CreateBlogViewState {
        return CreateBlogViewState()
    }

    fun setNewBlogFields(title: String?, body: String?, uri: Uri?) {
        val update = getCurrentViewStateOrNew()
        val newBlogFields = update.blogFields
        title?.let { newBlogFields.newBlogTitle = it }
        body?.let { newBlogFields.newBlogBody = it }
        uri?.let { newBlogFields.newImageUri = it }
        update.blogFields = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrentViewStateOrNew()
        update.blogFields = CreateBlogViewState.NewBlogFields()
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}