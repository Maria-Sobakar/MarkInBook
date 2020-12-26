package com.fuh.markinbook.data

import com.fuh.markinbook.data.lessons.Item
import com.google.gson.annotations.SerializedName

enum class Day : Item {
    @SerializedName("monday")
    MONDAY,
    @SerializedName("tuesday")
    TUESDAY,
    @SerializedName("wednesday")
    WEDNESDAY,
    @SerializedName("thursday")
    THURSDAY,
    @SerializedName("friday")
    FRIDAY,
    @SerializedName("saturday")
    SATURDAY,
    @SerializedName("sunday")
    SUNDAY
}

