package com.kotdev.blog.app.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kotdev.blog.app.ui.activities.auth.state.AuthStateEvent
import com.kotdev.blog.app.ui.activities.auth.state.RegistrationFields
import com.kotdev.blog.databinding.FragmentRegisterBinding
import com.kotdev.blog.di.auth.AuthScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class RegisterFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
) : BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setButtonOnClickListener {
            register()
        }
        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let { binding.email.setText(it) }
                it.registration_username?.let { binding.username.setText(it) }
                it.registration_password?.let { binding.password.setText(it) }
                it.registration_confirm_password?.let { binding.confirmPassword.setText(it) }
            }
        })
    }

    fun register() {
        viewModel.setDialogLoadingButton(binding.signup)
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                binding.email.text.toString(),
                binding.username.text.toString(),
                binding.password.text.toString(),
                binding.confirmPassword.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                binding.email.text.toString(),
                binding.username.text.toString(),
                binding.password.text.toString(),
                binding.confirmPassword.text.toString()
            )
        )
        _binding = null
    }
}