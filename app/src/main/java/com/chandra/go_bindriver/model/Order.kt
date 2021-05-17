package com.chandra.go_bindriver.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Order(
    val id: String,
    val idInvoice : String,
    val idDriver:String,
    val idUser:String,
    val idType:Type,
    val address:String,
    val amount:Int,
    val price:Int,
    val latitude:String,
    val longitude:String,
    val status:String,
    val date:String
) : Parcelable