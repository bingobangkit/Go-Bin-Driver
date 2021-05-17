package com.chandra.go_bindriver.ui.order

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.databinding.ActivityMainBinding
import com.chandra.go_bindriver.utils.DataDummy
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var listOrder:ListOrderAdapter

    companion object{
        const val TAG = "USERS"
    }

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderDummy = DataDummy.generateOrder()
        binding.rvOrder.layoutManager = LinearLayoutManager(this)
        listOrder = ListOrderAdapter(orderDummy)
        binding.rvOrder.adapter = listOrder

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }

    }
}