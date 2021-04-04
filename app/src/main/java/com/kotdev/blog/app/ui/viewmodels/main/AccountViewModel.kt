package com.kotdev.blog.app.ui.viewmodels.main

import com.kotdev.blog.app.repository.DataState
import com.kotdev.blog.app.models.AccountProperties
import com.kotdev.blog.app.repository.main.AccountRepositoryImpl
import com.kotdev.blog.app.session.SessionManager
import com.kotdev.blog.app.ui.fragments.main.account.state.AccountStateEvent
import com.kotdev.blog.app.ui.fragments.main.account.state.AccountViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.app.ui.viewmodels.BaseViewModel
import com.kotdev.blog.di.main.MainScope
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
@MainScope
class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepositoryImpl
)
    : BaseViewModel<AccountViewState>()
{

    override fun handleNewData(data: AccountViewState) {
        data.accountProperties?.let { accountProperties ->
            setAccountPropertiesData(accountProperties)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<AccountViewState>> = when(stateEvent){

                is AccountStateEvent.GetAccountPropertiesEvent -> {
                    accountRepository.getAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }

                is AccountStateEvent.UpdateAccountPropertiesEvent -> {
                    accountRepository.saveAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is AccountStateEvent.ChangePasswordEvent -> {
                    accountRepository.updatePassword(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        currentPassword = stateEvent.currentPassword,
                        newPassword = stateEvent.newPassword,
                        confirmNewPassword = stateEvent.confirmNewPassword
                    )
                }

                else -> {
                    flow{
                        emit(
                            DataState.error<AccountViewState>(
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

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
            return
        }
        update.accountProperties = accountProperties
        setViewState(update)
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun logout(){
        sessionManager.logout()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}




