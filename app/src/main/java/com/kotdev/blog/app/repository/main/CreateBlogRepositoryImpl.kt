package com.kotdev.blog.app.repository.main

import com.kotdev.blog.app.api.main.ApiMainService
import com.kotdev.blog.app.api.main.response.BlogCreateUpdateResponse
import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.persistance.BlogPostDao
import com.kotdev.blog.app.repository.*
import com.kotdev.blog.app.session.SessionManager
import com.kotdev.blog.app.ui.fragments.main.create_blog.state.CreateBlogViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.di.main.MainScope
import com.kotdev.blog.app.repository.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@FlowPreview
@MainScope
class CreateBlogRepositoryImpl
@Inject
constructor(
    val apiMainService: ApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager,
) : CreateBlogRepository {

    private val TAG: String = "AppDebug"

    override fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
        stateEvent: StateEvent,
    ) = flow {

        val apiResult = safeApiCall(IO) {
            apiMainService.createBlog(
                "Token ${authToken.token!!}",
                title,
                body,
                image
            )
        }

        emit(
            object : ApiResponseHandler<CreateBlogViewState, BlogCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: BlogCreateUpdateResponse): DataState<CreateBlogViewState> {

                    // If they don't have a paid membership account it will still return a 200
                    // Need to account for that
                    if (!resultObj.response.equals(RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER)) {
                        val updatedBlogPost = resultObj.toBlogPost()
                        blogPostDao.insert(updatedBlogPost)
                    }
                    return DataState.data(
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

}



