package com.fuh.markinbook.screens.schedule

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuh.markinbook.MainActivity.Companion.KEY_LESSON_ID
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.R
import com.fuh.markinbook.data.lessons.ServerLessonItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.schedule_fragment.*
import timber.log.Timber

class ScheduleFragment : Fragment(R.layout.schedule_fragment) {
    private var adapter = ScheduleAdapter(mutableListOf(), null)
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var prevWeekItem: MenuItem
    private lateinit var currentWeekItem: MenuItem
    private lateinit var nextWeekItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.schedule_menu, menu)
        val profileItem = menu.findItem(R.id.profile)
        val logInItem = menu.findItem(R.id.log_in)
        prevWeekItem = menu.findItem(R.id.prev_week)
        currentWeekItem = menu.findItem(R.id.current_week)
        nextWeekItem = menu.findItem(R.id.next_week)
        profileItem.isVisible = PreferencesManager.userToken.isNotEmpty()
        logInItem.isVisible = PreferencesManager.userToken.isEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
                navController.navigate(R.id.userFragment)
                true
            }
            R.id.next_week -> {
                viewModel.getLessonsForWeek(ScheduleViewModel.NEXT_WEEK)
                prevWeekItem.isVisible = true
                currentWeekItem.isVisible = true
                nextWeekItem.isVisible = false

                true
            }
            R.id.prev_week -> {
                viewModel.getLessonsForWeek(ScheduleViewModel.PREV_WEEK)
                prevWeekItem.isVisible = false
                currentWeekItem.isVisible = true
                nextWeekItem.isVisible = true

                true
            }
            R.id.current_week -> {
                viewModel.getLessonsForWeek()
                prevWeekItem.isVisible = true
                currentWeekItem.isVisible = false
                nextWeekItem.isVisible = true

                true
            }
            R.id.log_in->{
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
                navController.navigate(R.id.authorizationFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLessonsForWeek()
        scheduleProgressFrame.isVisible = true

        val lessonId = arguments?.getString(KEY_LESSON_ID,"")?.toIntOrNull()
        if (lessonId != null ) {
            viewModel.getLesson(lessonId)
        }

        (activity as AppCompatActivity).setSupportActionBar(scheduleToolBar)
        scheduleToolBar.title = requireContext().getString(R.string.schedule)

        scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.lessonsLiveData.observe(viewLifecycleOwner) {
            scheduleProgressFrame.isVisible = false
            adapter.itemList = it
            adapter.activity = requireActivity()
            scheduleRecyclerView.adapter = adapter

        }
        viewModel.lessonLiveData.observe(viewLifecycleOwner) { lesson ->
            val lessonList = adapter.itemList

            lessonList.forEach {
                if (it is ServerLessonItem) {
                    if (it.serverLesson.id == lesson.id) {
                        scheduleRecyclerView.scrollToPosition(lessonList.indexOf(it))
                    }
                }
            }
        }
        viewModel.networkErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    if (PreferencesManager.userToken.isNotEmpty()){
                        val snackBar = Snackbar.make(
                            scheduleCoordinatorLayout,
                            requireContext().getString(R.string.no_network_connection),
                            Snackbar.LENGTH_SHORT
                        )
                        snackBar.show()
                    }
                }
            }
        }
        viewModel.serverErrorLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { error ->
                if (error) {
                    val snackBar = Snackbar.make(
                        scheduleCoordinatorLayout,
                        requireContext().getString(R.string.server_error),
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }
            }
        }
        viewModel.needAuthorizationLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { open ->
                if (open) {
                    val navController =
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.navigation_host_fragment
                        )
                    navController.navigate(R.id.authorizationFragment)
                }
            }
        }
        scheduleFab.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.navigation_host_fragment)
            navController.navigate(R.id.lessonFragment)
        }
    }
}

