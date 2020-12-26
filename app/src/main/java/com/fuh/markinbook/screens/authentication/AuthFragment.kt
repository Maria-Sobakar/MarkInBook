package com.fuh.markinbook.screens.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.fuh.markinbook.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.auth_fragment.*


class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewPager.adapter = AuthAdapter(this)
        TabLayoutMediator(authTabLayout, authViewPager) { tab, pos ->
            val text = if (pos == 0) {
                requireContext().getText(R.string.sign_in)
            } else {
                requireContext().getText(R.string.sig_up)
            }
            tab.text = text
        }.attach()
        viewModel.openScheduleFragmentLiveData.observe(viewLifecycleOwner) {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
            it.getContentIfNotHandled()?.let { open ->
                if (open) {
                    navController.navigate(R.id.scheduleFragment)
                }
            }
        }
    }
}
