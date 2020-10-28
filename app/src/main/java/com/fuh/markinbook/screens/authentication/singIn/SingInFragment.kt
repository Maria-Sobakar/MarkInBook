package com.fuh.markinbook.screens.authentication.singIn

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fuh.markinbook.R
import kotlinx.android.synthetic.main.sing_in_fragment.*

class SingInFragment:Fragment(R.layout.sing_in_fragment) {
    private val viewModel:SingInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singInEmailEt.doOnTextChanged { text, _, _, _ ->
            viewModel.emailInputted(text.toString())
        }
        singInPassEt.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordInputted(text.toString())
        }
        singInButton.setOnClickListener {
            viewModel.singInButtonClicked()
        }
    }

    companion object{
        fun getInstance(): SingInFragment {
            return SingInFragment()
        }
    }
}