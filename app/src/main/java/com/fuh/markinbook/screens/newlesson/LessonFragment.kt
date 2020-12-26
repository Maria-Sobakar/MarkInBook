package com.fuh.markinbook.screens.newlesson

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.fuh.markinbook.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.lesson_fragment.*
import java.util.*


class LessonFragment : Fragment(R.layout.lesson_fragment) {

    private val viewModel: LessonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = activity?.actionBar
        actionBar?.title = context?.getText(R.string.add_new_lesson)
        lessonNameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.disciplineIsSet(text.toString())
        }
        datePickerButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                viewModel.dateIsSet(it)
                datePickerButton.text = DateFormat.format("dd.MM.yyyy", Date(it))
            }
            picker.show(parentFragmentManager, DIALOG_DATE)
        }
        timePickerButton.setOnClickListener {
            val builder = MaterialTimePicker.Builder()
            builder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            builder.setTimeFormat(TimeFormat.CLOCK_24H)
            builder.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            builder.setMinute(Calendar.getInstance().get(Calendar.MINUTE))
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour
                val minute = picker.minute
                val targetTime = Calendar.getInstance()
                targetTime.set(0, 0, 0, hour, minute, 0)
                viewModel.timeIsSet(targetTime)
                timePickerButton.text = DateFormat.format("kk:mm", targetTime)
            }
            picker.show(parentFragmentManager, DIALOG_TIME)
        }
        lessonDurationTextInputEditText.doOnTextChanged { text, _, _, _ ->
            val duration = text.toString().toIntOrNull()
            if (duration == null) {
                lessonDurationTextInputLayout.error =
                    requireContext().getText(R.string.invalid_duration)
            } else {
                lessonDurationTextInputLayout.error = null
                viewModel.durationIsSet(duration)
            }
        }

        lessonHomeworkTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.homeworkIsSet(text.toString())
        }

        createNewLessonFab.setOnClickListener {
            viewModel.createLesson()
        }
        viewModel.closeLiveData.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                if (it) {
                    val navController =
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.navigation_host_fragment
                        )
                    navController.popBackStack()
                }
            }
        }
        viewModel.createLessonLiveData.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let{
                Snackbar.make(lessonCoordinatorLayout,requireContext().getString(R.string.not_all_info),Snackbar.LENGTH_LONG)
            }

        }
    }

    companion object {
        private const val DIALOG_DATE = "DialogDate"
        private const val DIALOG_TIME = "DialogTime"
    }
}