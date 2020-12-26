package com.fuh.markinbook.screens.authentication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fuh.markinbook.screens.authentication.signup.SignUpFragment
import com.fuh.markinbook.screens.authentication.singin.SignInFragment

class AuthAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            SignInFragment.getInstance()
        } else {
            SignUpFragment.getInstance()
        }
    }
}