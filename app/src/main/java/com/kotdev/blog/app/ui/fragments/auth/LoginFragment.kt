package com.kotdev.blog.app.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kotdev.blog.app.ui.activities.auth.state.AuthStateEvent
import com.kotdev.blog.app.ui.activities.auth.state.LoginFields
import com.kotdev.blog.databinding.FragmentLauncherBinding
import com.kotdev.blog.databinding.FragmentLoginBinding
import com.kotdev.blog.di.auth.AuthScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LoginFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
) : BaseAuthFragment(viewModelFactory) {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        binding.login.setButtonOnClickListener {
            login()
        }

    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, {
            it.loginFields?.let {
                it.login_email?.let { binding.email.setText(it) }
                it.login_password?.let { binding.password.setText(it) }
            }
        })
        viewModel.setDialogLoadingButton(binding.login)
    }

    fun login() {
        saveLoginFields()
        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        )
    }

    private fun saveLoginFields() {
        viewModel.setLoginFields(
            LoginFields(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveLoginFields()
        _binding = null
    }

}


