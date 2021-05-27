package com.chandra.go_bindriver.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chandra.go_bindriver.data.Repository
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.utils.lazyDeferred
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Deferred

class DetailViewModel : ViewModel() {
    private val repository = Repository()
    suspend fun getDetailOrder(id: String): LiveData<out Order> {
        val detailOrder by lazyDeferred {
            repository.getOrderById(id)
        }
        return detailOrder.await()
    }

    fun updateStatus(id:String,status:String):Boolean {

        return try {
            repository.updateStatus(id,status)
            true
        }  catch (e: FirebaseFirestoreException){
            e.printStackTrace()
            false
        }
    }

}