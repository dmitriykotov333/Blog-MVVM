package com.kotdev.blog.app.ui.fragments.main.account.factory

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.kotdev.blog.app.ui.fragments.main.account.AccountFragment
import com.kotdev.blog.app.ui.fragments.main.account.ChangePasswordFragment
import com.kotdev.blog.app.ui.fragments.main.account.UpdateAccountFragment
import com.kotdev.blog.di.main.MainScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@MainScope
class AccountFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            AccountFragment::class.java.name -> {
                AccountFragment(viewModelFactory)
            }

            ChangePasswordFragment::class.java.name -> {
                ChangePasswordFragment(viewModelFactory)
            }

            UpdateAccountFragment::class.java.name -> {
                UpdateAccountFragment(viewModelFactory)
            }

            else -> {
                AccountFragment(viewModelFactory)
            }
        }


}