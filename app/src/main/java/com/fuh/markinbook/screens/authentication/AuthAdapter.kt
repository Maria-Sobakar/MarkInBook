package com.fuh.markinbook.screens.authentication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fuh.markinbook.screens.authentication.sindUp.SingUpFragment
import com.fuh.markinbook.screens.authentication.singIn.SingInFragment

class AuthAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            SingInFragment.getInstance()
        } else {
            SingUpFragment.getInstance()
        }
    }
}