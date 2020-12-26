package com.fuh.markinbook.screens.authentication.signup

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fuh.markinbook.R
import com.fuh.markinbook.utils.isEmailValid
import com.fuh.markinbook.screens.authentication.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.sign_up_fragment.*


class SignUpFragment : Fragment(R.layout.sign_up_fragment) {
    private val viewModel: AuthViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.schoolsLiveData.observe(viewLifecycleOwner) { schools ->
            signUpSchoolsSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    schools.map { it.title })
            signUpSchoolsSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.schoolSelected(schools[position])
                        signUpProgressFrame.isVisible = true
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }
        viewModel.groupsLiveData.observe(viewLifecycleOwner) { groups ->
            signUpGroupSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    groups.map { it.title })
            signUpGroupSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.groupSelected(groups[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            signUpProgressFrame.isVisible = false
        }
        viewModel.networkErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    val snackBar = Snackbar.make(
                        signUpCoordinatorLayout,
                        requireContext().getString(R.string.no_network_connection),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.show()
                }
            }
        }
        viewModel.serverErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    val snackBar = Snackbar.make(
                        signUpCoordinatorLayout,
                        requireContext().getString(R.string.server_error),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.show()
                }
            }
        }
        signUpEmailTextInputEditText.doOnTextChanged { text, _, _, _ ->
            if (isEmailValid(text.toString())) {
                viewModel.emailInputted(text.toString())
                signUpEmailTextInputEditText.error =null
            } else {
                signUpEmailTextInputEditText.error =
                    requireContext().getText(R.string.invalid_email)
            }

        }
        signUpPassTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordInputted(text.toString())
        }
        signUpFirstNameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.firstNameInputted(text.toString())
        }
        signUpLastNameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.lastNameInputted(text.toString())
        }
        signUpButton.setOnClickListener {
            viewModel.singUp()
        }
    }

    companion object {
        fun getInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
}

