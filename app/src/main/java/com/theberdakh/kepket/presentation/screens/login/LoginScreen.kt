package com.theberdakh.kepket.presentation.screens.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.extensions.ViewExtensions.gone
import com.theberdakh.extensions.ViewExtensions.string
import com.theberdakh.extensions.ViewExtensions.visible
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenLoginBinding
import com.theberdakh.kepket.presentation.screens.allorders.AllOrdersScreen
import com.theberdakh.kepket.presentation.statemanagers.login.LoginViewModel
import com.theberdakh.navigation.NavigationExtensions.addFragment
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginScreen : Fragment(R.layout.screen_login) {

    private val binding by viewBinding<ScreenLoginBinding>()
    private val loginViewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInLoginBtn.setOnClickListener {
            signInAction()
        }
    }


    override fun onStart() {
        super.onStart()
        listenToObservers()
    }

    private fun listenToObservers() {
        loginViewModel.loginState.onEach {
            if (it.isLoading){
                binding.loginText.gone()
                binding.loginProgress.visible()
            }
            if (it.result != null){
                when(it.result.status){
                    Status.SUCCESS -> {
                        binding.loginProgress.gone()
                        binding.loginText.visible()
                        requireActivity().supportFragmentManager.addFragment(R.id.main, AllOrdersScreen.newInstance())
                    }
                    Status.ERROR -> {
                        binding.loginProgress.gone()
                        binding.loginText.visible()
                        Toast.makeText(requireContext(), it.result.errorThrowable?.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun signInAction() {
        val username = binding.etUsername.string
        val password = binding.etPassword.string
        loginViewModel.login(username, password)
    }

    companion object {
         fun newInstance(): LoginScreen {
            return LoginScreen()
        }
    }

}
