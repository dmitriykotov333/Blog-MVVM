package com.kotdev.blog.app.ui.viewmodels.auth

import android.annotation.SuppressLint
import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.repository.auth.AuthRepository
import com.kotdev.blog.app.ui.activities.auth.state.AuthStateEvent
import com.kotdev.blog.app.ui.activities.auth.state.AuthViewState
import com.kotdev.blog.app.ui.activities.auth.state.LoginFields
import com.kotdev.blog.app.ui.activities.auth.state.RegistrationFields
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.app.ui.viewmodels.BaseViewModel
import com.kotdev.blog.di.auth.AuthScope
import com.kotdev.blog.helpers.materialloadingbutton.LoadingButton
import com.kotdev.blog.app.repository.DataState
import com.kotdev.blog.app.repository.ErrorHandling.Companion.INVALID_STATE_EVENT
import com.kotdev.blog.app.repository.MessageType
import com.kotdev.blog.app.repository.Response
import com.kotdev.blog.app.repository.UIComponentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
@AuthScope
class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository,
) : BaseViewModel<AuthViewState>() {

    @SuppressLint("StaticFieldLeak")
    private var dialog: LoadingButton? = null


    fun getDialogLoadingButton(): LoadingButton? {
        return dialog
    }

    fun setDialogLoadingButton(view: LoadingButton) {
        dialog = view
    }

    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<AuthViewState>> = when (stateEvent) {

            is AuthStateEvent.LoginAttemptEvent -> {
                authRepository.attemptLogin(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is AuthStateEvent.RegisterAttemptEvent -> {
                authRepository.attemptRegistration(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                authRepository.checkPreviousAuthUser(stateEvent)
            }

            else -> {
                flow {
                    emit(
                        DataState.error<AuthViewState>(
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

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}


























