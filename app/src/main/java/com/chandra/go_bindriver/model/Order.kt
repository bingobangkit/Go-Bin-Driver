package com.chandra.go_bindriver.model

import java.io.Serializable

data class Order(
    var id: String = "",
    var id_invoice: String = "",
    var id_driver: String = "",
    var id_user: String = "",
    var address: String = "",
    var id_type: String = "",
    var amount: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var status: String = "",
    var date: String = "",
    var total_price: String = "",
    var amount_cardboard: String = "",
    var amount_plastic: String = "",
    var amount_steel: String = "",
    var total_plastic:String="",
    var total_cardboard:String="",
    var total_steel:String = ""
) : Serializable