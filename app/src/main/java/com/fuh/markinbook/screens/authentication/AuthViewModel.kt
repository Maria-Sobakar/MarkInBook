package com.fuh.markinbook.screens.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.api.ResultWrapper
import com.fuh.markinbook.data.Group
import com.fuh.markinbook.data.School
import com.fuh.markinbook.data.repository.MarkInBookServerRepository
import com.fuh.markinbook.utils.Event
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel : ViewModel() {
    private val repository = MarkInBookServerRepository()
    private var email: String? = null
    private var password: String? = null
    private var firstName: String? = null
    private var lastName: String? = null

    private var school: School? = null
    private var group: Group? = null
    val openScheduleFragmentLiveData = MutableLiveData<Event<Boolean>>()
    val schoolsLiveData = MutableLiveData<List<School>>()
    val groupsLiveData = MutableLiveData<List<Group>>()
    val networkErrorLiveData = MutableLiveData<Event<Boolean>>()
    val serverErrorLiveData = MutableLiveData<Event<Boolean>>()
    val notFilledFieldsLiveData = MutableLiveData<Event<Boolean>>()

    init {
        getSchools()
    }

    fun emailInputted(text: String) {
        email = text
    }

    fun passwordInputted(text: String) {
        password = text
    }

    fun firstNameInputted(text: String) {
        firstName = text
    }

    fun lastNameInputted(text: String) {
        lastName = text
    }

    fun schoolSelected(selected: School) {
        school = selected
        getGroupsForSchool()
    }

    fun groupSelected(selected: Group) {
        group = selected
    }

    private fun getSchools() {
        viewModelScope.launch {
            when (val schools = repository.getAllSchools()) {
                is ResultWrapper.NetworkError -> {
                    networkErrorLiveData.value = Event(true)
                }
                is ResultWrapper.Success -> {
                    schoolsLiveData.value = schools.value!!
                }
                is ResultWrapper.GenericError -> serverErrorLiveData.value = Event(true)
            }
        }
    }

    private fun getGroupsForSchool() {
        viewModelScope.launch {
            val groups = repository.getGroups(school?.id ?: 0)
            groupsLiveData.value = groups
        }
    }


    fun singUp() {

        if (firstName != null &&
            lastName != null &&
            school != null &&
            group != null &&
            email != null &&
            password != null
        ) {
            viewModelScope.launch {
                val response = repository.signUp(
                    firstName!!,
                    lastName!!,
                    school!!.id,
                    group!!.id,
                    email!!,
                    password!!
                )
                handleResponse(response)
            }
        } else {
            notFilledFieldsLiveData.value = Event(true)
        }
    }

    fun singIn() {
        if (email != null && email?.isNotEmpty()!! && password != null && password?.isNotEmpty()!!) {
            viewModelScope.launch {
                val response = repository.signIn(email!!, password!!)
                handleResponse(response)
            }
        } else {
            notFilledFieldsLiveData.value = Event(true)
        }
    }

    private fun pushToken(token: String) {
        viewModelScope.launch {
            repository.pushToken(token)
        }
    }

    private fun handleResponse(response: ResultWrapper<String>) {
        when (response) {
            is ResultWrapper.NetworkError -> {
                networkErrorLiveData.value = Event(true)
            }
            is ResultWrapper.Success -> {
                PreferencesManager.userToken = response.value
                PreferencesManager.email = email ?: ""
//                PreferencesManager.deviceToken =  FirebaseMessaging.getInstance().token.result
                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                    if (it.isSuccessful) {
                        PreferencesManager.deviceToken = it.result
                    }
                }
                Timber.i(PreferencesManager.deviceToken)
                if (PreferencesManager.deviceToken!!.isNotEmpty()) {
                    pushToken(PreferencesManager.deviceToken!!)
                }

            }
            is ResultWrapper.GenericError -> serverErrorLiveData.value = Event(true)
        }
        if (PreferencesManager.userToken.isNotEmpty()) {
            openScheduleFragmentLiveData.value = Event(true)
        }
    }
}
