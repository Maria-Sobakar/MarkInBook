package com.fuh.markinbook.screens.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fuh.markinbook.R

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private var adapter = ScheduleAdapter(requireContext(), mutableListOf())
    private val viewModel: ScheduleViewModel by viewModels()


}