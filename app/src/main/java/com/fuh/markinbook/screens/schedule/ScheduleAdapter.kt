package com.fuh.markinbook.screens.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fuh.markinbook.R
import com.fuh.markinbook.data.dto.Day
import com.fuh.markinbook.data.dto.Lesson
import com.fuh.markinbook.data.dto.Item
import java.lang.StringBuilder

private const val TYPE_DAY = 0
private const val TYPE_DAY_OF_WEEK = 1

class ScheduleAdapter(val context: Context, var itemList: MutableList<Item> ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == TYPE_DAY) {
            val view = inflater.inflate(R.layout.item_schedule_lesson, parent, false)
            ScheduleLessonHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_schedule_day, parent, false)
            ScheduleDayNameHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ScheduleLessonHolder-> holder.bind(itemList[position] as Lesson)
            is ScheduleDayNameHolder-> holder.bind(itemList[position] as Day)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] is Lesson) {
            TYPE_DAY
        } else {
            TYPE_DAY_OF_WEEK
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ScheduleLessonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(lesson: Lesson) {
            val time = itemView.findViewById<TextView>(R.id.lessonTimeTv)
            val name = itemView.findViewById<TextView>(R.id.lessonNameTv)
            val mark = itemView.findViewById<TextView>(R.id.markTf)
            time.text = lesson.lessonTime()
            name.text = lesson.lessonName
            mark.text = if (lesson.mark != null) mark.toString() else ""
        }
    }

    inner class ScheduleDayNameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(day:Day){
            val dayName = itemView.findViewById<TextView>(R.id.dayNameTv)
            dayName.text = day.name
        }
    }
}