package com.fuh.markinbook.screens.schedule.homework

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuh.markinbook.data.lessons.ServerLesson
import com.fuh.markinbook.data.repository.MarkInBookServerRepository
import kotlinx.coroutines.launch

class HomeworkViewModel : ViewModel() {
    private val repository = MarkInBookServerRepository()
    val lessonLiveData = MutableLiveData<ServerLesson>()

    fun getLesson(lessonId: Int) {
        viewModelScope.launch {
           val lesson = repository.getLesson(lessonId)
            lessonLiveData.value = lesson
        }
    }
}