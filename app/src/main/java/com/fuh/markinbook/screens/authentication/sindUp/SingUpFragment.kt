package com.fuh.markinbook.screens.authentication.sindUp

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fuh.markinbook.R
import kotlinx.android.synthetic.main.sing_up_fragment.*

class SingUpFragment:Fragment(R.layout.sing_up_fragment) {
    private val viewModel: SingUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singUpEmailEt.doOnTextChanged { text, _, _, _ ->
            viewModel.emailInputted(text.toString())
        }
        singUpPassEt.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordInputted(text.toString())
        }
        singUpButton.setOnClickListener {
            viewModel.singUpButtonClicked()
        }
    }
    companion object{
        fun getInstance(): SingUpFragment {
            return SingUpFragment()
        }
    }
}