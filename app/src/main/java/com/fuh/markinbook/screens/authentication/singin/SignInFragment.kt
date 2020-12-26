package com.fuh.markinbook.screens.authentication.singin

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.R
import com.fuh.markinbook.screens.authentication.AuthViewModel
import com.fuh.markinbook.utils.isEmailValid
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : Fragment(R.layout.sign_in_fragment) {
    private val viewModel: AuthViewModel by viewModels({ requireParentFragment() })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withoutSignInButton.isVisible = PreferencesManager.userToken.isNotEmpty()
        signInEmailTextInputEditText.doOnTextChanged { text, _, _, _ ->
            if (isEmailValid(text.toString())) {
                viewModel.emailInputted(text.toString())
                signInEmailTextInputEditText.error = null
            } else {
                signInEmailTextInputEditText.error =
                    requireContext().getText(R.string.invalid_email)
            }
        }
        signInPassTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordInputted(text.toString())
        }
        signInButton.setOnClickListener {
            viewModel.singIn()
        }
        withoutSignInButton.setOnClickListener {
            PreferencesManager.userToken = ""
            val navController =
                Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
            navController.navigate(R.id.scheduleFragment)
        }
        viewModel.networkErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    val snackBar = Snackbar.make(
                        signInCoordinatorLayout,
                        requireContext().getString(R.string.no_network_connection),
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }
            }
        }
        viewModel.serverErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    val snackBar = Snackbar.make(
                        signInCoordinatorLayout,
                        requireContext().getString(R.string.server_error),
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }
            }
        }

    }

    companion object {
        fun getInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}