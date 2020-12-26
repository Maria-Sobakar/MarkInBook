package com.fuh.markinbook.screens.schedule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fuh.markinbook.R
import com.fuh.markinbook.utils.addDuration
import com.fuh.markinbook.data.Day
import com.fuh.markinbook.data.lessons.Item
import com.fuh.markinbook.data.lessons.LocalLessonItem
import com.fuh.markinbook.data.lessons.ServerLessonItem
import com.fuh.markinbook.utils.dayName
import com.fuh.markinbook.utils.getMinutes
import java.util.*


class ScheduleAdapter(
    var itemList: MutableList<Item>,
    var activity: Activity?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_SERVER_LESSON -> {
                val view = inflater.inflate(R.layout.item_schedule_server_lesson, parent, false)
                ScheduleServerLessonHolder(view)
            }
            TYPE_LOCAL_LESSON -> {
                val view = inflater.inflate(R.layout.item_schedule_local_lesson, parent, false)
                ScheduleLocalLessonHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_schedule_day, parent, false)
                ScheduleDayNameHolder(parent.context, view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScheduleServerLessonHolder -> holder.bind(itemList[position] as ServerLessonItem)
            is ScheduleDayNameHolder -> holder.bind(itemList[position] as Day)
            is ScheduleLocalLessonHolder -> holder.bind(itemList[position] as LocalLessonItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is ServerLessonItem -> TYPE_SERVER_LESSON
            is LocalLessonItem -> TYPE_LOCAL_LESSON

            else -> TYPE_DAY_OF_WEEK
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ScheduleServerLessonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ServerLessonItem) {
            val lesson = item.serverLesson
            val time = itemView.findViewById<TextView>(R.id.serverLessonTimeTextView)
            val name = itemView.findViewById<TextView>(R.id.serverLessonNameTextView)
            val mark = itemView.findViewById<TextView>(R.id.markTextView)
            val homework = itemView.findViewById<ImageButton>(R.id.serverLessonHomeworkImageButton)
            val lessonTimeStart = Calendar.getInstance()
            lessonTimeStart.timeInMillis = lesson.start
            val lessonTimeEnd = Calendar.getInstance()
            lessonTimeEnd.timeInMillis = lessonTimeStart.addDuration(lesson.durationInMinutes)
            time.text =
                "${lessonTimeStart.get(Calendar.HOUR_OF_DAY)}:${lessonTimeStart.getMinutes()} - ${
                    lessonTimeEnd.get(Calendar.HOUR_OF_DAY)
                }:${lessonTimeEnd.getMinutes()}"

            if (lesson.homework == null) {
                homework.isEnabled = false
            }
            val homeworkImage = if (lesson.homework == null) {
                ContextCompat.getDrawable(context, R.drawable.ic_no_homework)

            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_homework)
            }
            homework.setImageDrawable(homeworkImage)
            homework.setOnClickListener {
                val arg = Bundle().apply {
                    putInt(KEY_LESSON_ID_PICKED, lesson.id)
                }
                val navController =
                    Navigation.findNavController(activity!!, R.id.navigation_host_fragment)
                navController.navigate(R.id.homeworkFragment, arg)
            }
            name.text = lesson.discipline.title
            mark.text = if (lesson.mark != null) lesson.mark.value.toString() else ""
        }
    }

    inner class ScheduleLocalLessonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: LocalLessonItem) {
            val lesson = item.localLesson
            val time = itemView.findViewById<TextView>(R.id.localLessonTimeTextView)
            val name = itemView.findViewById<TextView>(R.id.localLessonNameTextView)
            val homework = itemView.findViewById<ImageButton>(R.id.localLessonHomeworkImageButton)
            val lessonTimeStart = Calendar.getInstance()
            lessonTimeStart.timeInMillis = lesson.start
            val lessonTimeEnd = Calendar.getInstance()
            lessonTimeEnd.timeInMillis = lessonTimeStart.addDuration(lesson.durationInMinutes)
            time.text =
                "${lessonTimeStart.get(Calendar.HOUR_OF_DAY)}:${lessonTimeStart.getMinutes()} - ${
                    lessonTimeEnd.get(Calendar.HOUR_OF_DAY)
                }:${lessonTimeEnd.getMinutes()}"

            if (lesson.homework.isEmpty()) {
                homework.isClickable = false
            }
            val homeworkImage = if (lesson.homework.isEmpty()) {
                ContextCompat.getDrawable(context, R.drawable.ic_no_homework)

            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_homework)
            }
            homework.setImageDrawable(homeworkImage)
            homework.setOnClickListener {
                val arg = Bundle().apply {
                    putString(LESSON_NAME_KEY, lesson.discipline)
                    putLong(LESSON_DATE_KEY, lesson.start)
                    putString(HOMEWORK_TASKS_KEY, lesson.homework)

                }
                val navController =
                    Navigation.findNavController(activity!!, R.id.navigation_host_fragment)
                navController.navigate(R.id.homeworkFragment, arg)
            }
            name.text = lesson.discipline
        }
    }

    inner class ScheduleDayNameHolder(val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(day: Day) {
            val dayName = itemView.findViewById<TextView>(R.id.dayNameTextView)
            dayName.text = dayName(context, day)
        }
    }

    companion object {
        const val KEY_LESSON_ID_PICKED = "lesson_id_picked"
        const val LESSON_NAME_KEY = "lesson_key"
        const val HOMEWORK_TASKS_KEY = "homework_tasks_key"
        const val LESSON_DATE_KEY = "lesson_date_key"
        private const val TYPE_SERVER_LESSON = 0
        private const val TYPE_LOCAL_LESSON = 1
        private const val TYPE_DAY_OF_WEEK = 2
    }
}
