package com.chandra.go_bindriver.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.User
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repository {


    fun getOrder(status: String): LiveData<out List<Order>> {
        val list = MutableLiveData<List<Order>>()
        val order = ArrayList<Order>()
        Firebase.firestore.collection("order").whereEqualTo("status", status)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    querySnapshot: QuerySnapshot?,
                    e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    order.clear()
                    for (document in querySnapshot!!) {
                        order.add(
                            Order(
                                id = document.id,
                                id_invoice = document.data["id_invoice"].toString(),
                                id_user = document.data["id_user"].toString(),
                                id_driver = document.data["id_driver"].toString(),
                                address = document.data["address"].toString(),
                                id_type = document.data["id_type"].toString(),
                                amount = document.data["amount"].toString(),
                                latitude = document.data["latitude"].toString(),
                                longitude = document.data["longitude"].toString(),
                                total_price = document.data["total_price"].toString(),
                                date = document.data["date"].toString(),
                                status = document.data["status"].toString()
                            )
                        )
                    }
                    list.postValue(order)
                    for (change in querySnapshot.documentChanges) {
                        if (change.type == DocumentChange.Type.ADDED) {
                            Log.d(ContentValues.TAG, "data:" + change.document.data)
                        }
                        val source = if (querySnapshot.metadata.isFromCache)
                            "local cache"
                        else
                            "server"
                        Log.d(ContentValues.TAG, "Data fetched from $source")
                    }
                }

            })
        return list
    }


    fun getOrderById(id: String): LiveData<out Order> {
        val order = MutableLiveData<Order>()

        Firebase.firestore.collection("order").document(id)
            .addSnapshotListener(object : EventListener<DocumentSnapshot?> {
                override fun onEvent(value: DocumentSnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    order.postValue(
                        Order(
                            id = value?.id.toString(),
                            id_invoice = value?.data?.get("id_invoice").toString(),
                            id_user = value?.data?.get("id_user").toString(),
                            id_driver = value?.data?.get("id_driver").toString(),
                            address = value?.data?.get("address").toString(),
                            id_type = value?.data?.get("id_type").toString(),
                            amount = value?.data?.get("amount").toString(),
                            latitude = value?.data?.get("latitude").toString(),
                            longitude = value?.data?.get("longitude").toString(),
                            total_price = value?.data?.get("total_price").toString(),
                            date = value?.data?.get("date").toString(),
                            status = value?.data?.get("status").toString()
                        )
                    )

                }
            })
        return order
    }

    fun updateStatus(id: String, status: String) {

        Firebase.firestore.collection("order").document(id).update("status", status)

    }

    fun getUserById(id: String): LiveData<out User> {
        val user = MutableLiveData<User>()

        Firebase.firestore.collection("users").document(id)
            .addSnapshotListener(object : EventListener<DocumentSnapshot?> {
                override fun onEvent(value: DocumentSnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }
                    user.postValue(
                        User(
                            id = value?.id.toString(),
                            address = value?.data?.get("address").toString(),
                            latitude = value?.data?.get("latitude").toString(),
                            longitude = value?.data?.get("longitude").toString(),
                            phone = value?.data?.get("phone").toString(),
                            saldo = value?.data?.get("saldo").toString(),
                            poin = value?.data?.get("poin").toString(),
                            name = value?.data?.get("name").toString()
                        )
                    )

                }
            })
        return user
    }

    fun updateBalance(id: String, balance: String) {

        Firebase.firestore.collection("users").document(id).update("saldo", balance)

    }
}