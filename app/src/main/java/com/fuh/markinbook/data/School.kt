package com.fuh.markinbook.data

data class School(
    val id: Int = 0,
    val title: String = "",
    val disciplines: List<Discipline> = emptyList()
)