package com.kotdev.blog.app.repository.main

import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.ui.fragments.main.account.state.AccountViewState
import com.kotdev.blog.app.ui.interfaces.StateEvent
import com.kotdev.blog.di.main.MainScope
import com.kotdev.blog.app.repository.DataState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent,
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent,
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent,
    ): Flow<DataState<AccountViewState>>
}