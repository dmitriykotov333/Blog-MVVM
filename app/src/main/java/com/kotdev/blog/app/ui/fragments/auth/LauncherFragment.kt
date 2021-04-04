package com.kotdev.blog.app.ui.fragments.auth

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kotdev.blog.R
import com.kotdev.blog.app.ui.viewmodels.auth.AuthViewModel
import com.kotdev.blog.databinding.FragmentForgotPasswordBinding
import com.kotdev.blog.databinding.FragmentLauncherBinding
import com.kotdev.blog.di.auth.AuthScope
import com.kotdev.blog.helpers.customeffect.SnowElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LauncherFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
) : BaseAuthFragment(viewModelFactory) {

    private var _binding: FragmentLauncherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val element1 = SnowElement(context, R.drawable.bitcoin)
        val element2 = SnowElement(context, R.drawable.euro)
        val element3 = SnowElement(context, R.drawable.ingot)

        binding.snowEffect.setElements(element1, element2, element3)
        binding.snowEffect.startShowing()
        binding.signup.setOnClickListener {
            navRegistration()
        }

        binding.login.setOnClickListener {
            navLogin()
        }

        binding.forgotPassword.setOnClickListener {
            navForgotPassword()
        }
    }

    fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

    fun navForgotPassword() {
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }

}



