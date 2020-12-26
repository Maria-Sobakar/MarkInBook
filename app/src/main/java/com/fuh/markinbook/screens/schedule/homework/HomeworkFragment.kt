package com.fuh.markinbook.screens.schedule.homework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fuh.markinbook.MainActivity.Companion.KEY_LESSON_ID
import com.fuh.markinbook.R
import com.fuh.markinbook.screens.schedule.ScheduleAdapter
import kotlinx.android.synthetic.main.homework_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class HomeworkFragment : Fragment(R.layout.homework_fragment) {
    private val viewModel: HomeworkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickedLessonId = arguments?.getInt(ScheduleAdapter.KEY_LESSON_ID_PICKED)
        if (pickedLessonId == null || pickedLessonId == 0) {
            val lessonId = arguments?.getString(KEY_LESSON_ID, "")?.toIntOrNull()
            if (lessonId == null) {
                val task = arguments?.getString(ScheduleAdapter.HOMEWORK_TASKS_KEY)
                val lessonName = arguments?.getString(ScheduleAdapter.LESSON_NAME_KEY)
                val lessonDate = arguments?.getLong(ScheduleAdapter.LESSON_DATE_KEY) ?: 0
                updateUI(task, lessonName, lessonDate)
            } else {
                viewModel.getLesson(lessonId)
            }
        } else {
            if (pickedLessonId != 0) {
                viewModel.getLesson(pickedLessonId)
            }
        }
        viewModel.lessonLiveData.observe(viewLifecycleOwner) { lesson ->
            val task = StringBuilder()
            lesson.homework?.tasks?.forEach {
                task.append(it.description)
                if (it != lesson.homework.tasks.last()) {
                    task.append(", ")
                }
            }
            val mark = lesson.homework?.mark?.value

            updateUI(task.toString(), lesson.discipline.title, lesson.start, mark)
        }
    }

    private fun updateUI(task: String?, lessonName: String?, lessonDate: Long, mark: Int? = null) {
        if (lessonName != null) {
            homeworkLessonNameTextView.text = lessonName
        }
        homeworkLessonDateTextView.text =
            SimpleDateFormat("dd-MM-yyyy").format(Date(lessonDate))
        if (task != null) {
            homeworkTaskTextView.text = task
        }
        if (mark != null) {
            homeworkLinearLayout.visibility = View.VISIBLE
            homeworkMarkTextView.text = mark.toString()
        }
    }
}