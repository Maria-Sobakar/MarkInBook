package com.fuh.markinbook.screens.newlesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuh.markinbook.data.Discipline
import com.fuh.markinbook.data.lessons.LocalLesson
import com.fuh.markinbook.data.lessons.ServerLesson
import com.fuh.markinbook.data.repository.MarkInBookDatabaseRepository
import com.fuh.markinbook.utils.Event
import com.fuh.markinbook.utils.getMillisFromCalendars
import kotlinx.coroutines.launch
import java.util.*

class LessonViewModel : ViewModel() {
    private val repository = MarkInBookDatabaseRepository()

    private  var discipline = ""
    private  var homework: String? = null
    private  var startDate: Calendar? = null
    private  var startTime: Calendar?= null
    private var lessonDuration = 0
    val closeLiveData = MutableLiveData<Event<Boolean>>()
    val createLessonLiveData = MutableLiveData<Event<Boolean>>()

    fun createLesson() {
        viewModelScope.launch {
            if (discipline.isNotEmpty()&&startDate!=null&&startTime!=null&&lessonDuration!=0){
                repository.addLesson(
                    LocalLesson(
                        discipline,
                        getMillisFromCalendars(startDate!!, startTime!!),
                        lessonDuration,
                        homework.orEmpty()
                    )
                )
                closeLiveData.value = Event(true)
            } else{
                createLessonLiveData.value = Event(true)
            }

        }
    }

    fun disciplineIsSet(name: String) {
        discipline = name
    }

    fun dateIsSet(date: Long) {
        val targetDate = Calendar.getInstance()
        targetDate.time = Date(date)
        startDate = targetDate
    }

    fun timeIsSet(time: Calendar) {
        startTime = time
    }

    fun durationIsSet(duration: Int) {
        lessonDuration = duration
    }

    fun homeworkIsSet(text:String){
        homework = text
    }
}
