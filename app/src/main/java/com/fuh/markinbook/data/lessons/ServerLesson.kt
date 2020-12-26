package com.fuh.markinbook.data.lessons

import com.fuh.markinbook.data.Discipline
import com.fuh.markinbook.data.Group


data class ServerLesson(
    val id: Int,
    val group: Group,
    val discipline: Discipline,
    override val start: Long,
    val durationInMinutes: Int,
    val homework: Homework?,
    val mark: StudentMark?
) : Lesson {
    data class StudentMark(val value: Int)

    data class Homework(val id: Int, val mark: StudentMark?, val tasks: List<Task>) {

        data class Task(val done: Boolean = false, val description: String)
    }
}

