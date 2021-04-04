package com.kotdev.blog.app.ui.fragments.main.account.state

import android.os.Parcelable
import com.kotdev.blog.app.models.AccountProperties
import kotlinx.android.parcel.Parcelize

const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "com.kotdev.blog.app.ui.fragments.main.account.state.AccountViewState"

@Parcelize
class AccountViewState(
    var accountProperties: AccountProperties? = null
) : Parcelable