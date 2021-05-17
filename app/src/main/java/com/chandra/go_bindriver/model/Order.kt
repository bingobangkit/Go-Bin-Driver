package com.chandra.go_bindriver.model

import java.util.*

data class Order(
    val id: String,
    val idInvoice : String,
    val idDriver:String,
    val idUser:String,
    val idType:Int,
    val address:String,
    val amount:Int,
    val price:Int,
    val latitude:String,
    val longitude:String,
    val status:String,
    val date:String
)