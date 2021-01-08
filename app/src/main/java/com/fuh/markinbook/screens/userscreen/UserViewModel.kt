package com.fuh.markinbook.screens.userscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuh.markinbook.data.Group
import com.fuh.markinbook.data.School
import com.fuh.markinbook.data.repository.MarkInBookServerRepository
import com.fuh.markinbook.data.Student
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserViewModel : ViewModel() {
    private val repository = MarkInBookServerRepository()
    val studentLiveData = MutableLiveData<Student>()
    val schoolLiveData = MutableLiveData<School>()
    val groupLiveData = MutableLiveData<Group>()
    val profileImageLiveData = MutableLiveData<String?>()
    val loadingLiveData = MutableLiveData<Boolean>()

    init {
        getStudentInfo()
    }

    fun addProfileImage(path: String) {
        viewModelScope.launch {
            val imageFile = File(path)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val image = MultipartBody.Part.createFormData("upload", imageFile.name, requestFile)
            repository.addProfileImage(image)
        }
    }

    private fun getStudentInfo() {
        viewModelScope.launch {
            loadingLiveData.value = true
            val student = repository.getCurrentStudent()
            val imageUrl = student.profileImage
            if (imageUrl != null) {
                profileImageLiveData.value = imageUrl
            }
            studentLiveData.value = student
            schoolLiveData.value = repository.getSchoolById(student.schoolId)
            groupLiveData.value = repository.getGroupById(student.schoolId, student.groupId)
            loadingLiveData.value = false
        }
    }
}