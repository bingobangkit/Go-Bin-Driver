package com.chandra.go_bindriver.model

data class User(
    var id: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val latitude: String? = "",
    val longitude: String? = "",
    val address: String? = "",
    val poin: String? = "0",
    val saldo: String? = "0",
    val jml_pickup: String? = "0"
)