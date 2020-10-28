package com.fuh.markinbook.screens.authentication

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fuh.markinbook.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.auth_fragment.*


class AuthFragment : Fragment(R.layout.auth_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authVp.adapter = AuthAdapter(this )
        TabLayoutMediator(tabLayout,authVp){tab,pos->
            val text =  if (pos ==0){
                requireContext().getText(R.string.sing_in)
            } else {
                requireContext().getText(R.string.sing_up)
            }
            tab.text = text
        }.attach()
    }
}