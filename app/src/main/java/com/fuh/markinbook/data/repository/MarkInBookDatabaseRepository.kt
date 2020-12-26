package com.fuh.markinbook.data.repository

import com.fuh.markinbook.data.lessons.ServerLesson
import com.fuh.markinbook.data.database.DatabaseBuilder
import com.fuh.markinbook.data.lessons.LocalLesson

class MarkInBookDatabaseRepository {
    private val database = DatabaseBuilder.instance
    private val dao = database.markInBookDAO()

    suspend fun addLesson(lesson: LocalLesson) = dao.addLesson(lesson)

    suspend fun getLessons() = dao.getLessons()

    suspend fun getDisciplines() = dao.getDisciplines()
}