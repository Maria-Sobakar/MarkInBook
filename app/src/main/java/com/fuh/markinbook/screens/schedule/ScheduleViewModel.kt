package com.fuh.markinbook.screens.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.api.ResultWrapper
import com.fuh.markinbook.data.*
import com.fuh.markinbook.data.lessons.*
import com.fuh.markinbook.data.repository.MarkInBookDatabaseRepository
import com.fuh.markinbook.data.repository.MarkInBookServerRepository
import com.fuh.markinbook.utils.Event
import com.fuh.markinbook.utils.getDay
import com.fuh.markinbook.utils.isThisWeek
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class ScheduleViewModel : ViewModel() {
    private val serverRepository = MarkInBookServerRepository()
    private val databaseRepository = MarkInBookDatabaseRepository()
    val lessonsLiveData = MutableLiveData<MutableList<Item>>()
    val needAuthorizationLiveData = MutableLiveData<Event<Boolean>>()
    val networkErrorLiveData = MutableLiveData<Event<Boolean>>()
    val serverErrorLiveData = MutableLiveData<Event<Boolean>>()
    val lessonLiveData = MutableLiveData<ServerLesson>()

    private fun updateLessonsList(
        dayLessonsList: Map<Day, MutableList<ServerLesson>>,
        localLessonList: List<LocalLesson>
    ) {
        val itemList = mutableListOf<Item>()

        val days = listOf<Day>(
            Day.MONDAY,
            Day.TUESDAY,
            Day.WEDNESDAY,
            Day.THURSDAY,
            Day.FRIDAY,
            Day.SATURDAY,
            Day.SUNDAY
        )
        days.forEach { day ->
            val remoteLessons = dayLessonsList[day]?: mutableListOf()
            val localLessons = localLessonList.filter { it.getDay() == day }
           val lessons = (remoteLessons + localLessons).asSequence()
                .sortedBy { it.start }
                .map {
                    if (it is LocalLesson){
                        LocalLessonItem(it)
                    } else{
                        ServerLessonItem(it as ServerLesson)
                    }
                }
               .toMutableList()
            if (lessons.isNotEmpty()) {
                val listItems = lessons.map {

                }
                itemList.add(day)
                itemList.addAll(lessons)
            }
        }
        lessonsLiveData.value = itemList
    }

    private fun pushToken(token: String) {
        viewModelScope.launch {
            serverRepository.pushToken(token)
        }
    }

    fun getLessonsForWeek(weekNumber: Int = 0) {
        viewModelScope.launch {
            val today = Calendar.getInstance()
            today.add(Calendar.WEEK_OF_YEAR, weekNumber)
            val currentWeek = today.get(Calendar.WEEK_OF_YEAR)
            val currentYear = today.get(Calendar.YEAR)
            when (val response = serverRepository.getLessonsForWeek(currentWeek, currentYear)) {
                is ResultWrapper.Success -> {
                    val localLessons = databaseRepository.getLessons()
                    val currentWeekLessons = localLessons.filter { it.isThisWeek(currentWeek) }
                    if (PreferencesManager.userToken.isNotEmpty()) {
                        updateLessonsList(response.value, currentWeekLessons)
                        val firebase = FirebaseMessaging.getInstance()

                        firebase.token.addOnCompleteListener {
                            if (it.isSuccessful) {
                                PreferencesManager.deviceToken = it.result
                            }
                        }
                        Timber.i(PreferencesManager.deviceToken)
                        if (PreferencesManager.deviceToken!!.isNotEmpty()) {
                            pushToken(PreferencesManager.deviceToken!!)
                        }

                    } else {
                        getLocalLessons(currentWeek)
                    }
                }
                is ResultWrapper.GenericError -> {
                    if (response.code == 401) {
                        if (PreferencesManager.userToken.isEmpty()) {
                            getLocalLessons(currentWeek)
                        } else {
                            needAuthorizationLiveData.value = Event(true)
                        }

                    } else {
                        getLocalLessons(currentWeek)
                        serverErrorLiveData.value = Event(true)
                    }
                }
                is ResultWrapper.NetworkError -> {
                    PreferencesManager.userToken = ""
                    getLocalLessons(currentWeek)
                    networkErrorLiveData.value = Event(true)

                }
            }
        }
    }

    private suspend fun getLocalLessons(currentWeek: Int) {

        val localLessons = databaseRepository.getLessons()
        val currentWeekLessons = localLessons.filter { it.isThisWeek(currentWeek) }
        updateLessonsList(emptyMap(), currentWeekLessons)
    }

    fun getLesson(lessonId: Int) {
        viewModelScope.launch {
            val lesson = serverRepository.getLesson(lessonId)
            lessonLiveData.value = lesson
        }
    }

    companion object {
        const val PREV_WEEK = -1
        const val NEXT_WEEK = 1
    }

}