package com.kotdev.blog.app.ui.fragments.auth.factory

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.kotdev.blog.app.ui.fragments.auth.ForgotPasswordFragment
import com.kotdev.blog.app.ui.fragments.auth.LauncherFragment
import com.kotdev.blog.app.ui.fragments.auth.LoginFragment
import com.kotdev.blog.app.ui.fragments.auth.RegisterFragment
import com.kotdev.blog.di.auth.AuthScope
import javax.inject.Inject

@AuthScope
class AuthFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            LauncherFragment::class.java.name -> {
                LauncherFragment(viewModelFactory)
            }

            LoginFragment::class.java.name -> {
                LoginFragment(viewModelFactory)
            }

            RegisterFragment::class.java.name -> {
                RegisterFragment(viewModelFactory)
            }

            ForgotPasswordFragment::class.java.name -> {
                ForgotPasswordFragment(viewModelFactory)
            }

            else -> {
                LauncherFragment(viewModelFactory)
            }
        }


}