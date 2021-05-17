package com.chandra.go_bindriver.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Type(
    val id: String = "",
    val name: String="",
    val price: Int=0
) : Parcelable