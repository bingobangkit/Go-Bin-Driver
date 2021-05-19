package com.chandra.go_bindriver.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.ActivityMainBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.Type
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.order.OrderFragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listOrder: ListOrderAdapter

    private lateinit var query: Query
    private var listOrderGarbage = ArrayList<Order>()


    companion object {
        const val TAG = "USERS"
    }

    var fStore = FirebaseFirestore.getInstance()
    lateinit var doc: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderFragment=OrderFragment()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            Log.d("main", it.itemId.toString())
            when(it.itemId){

                R.id.order -> supportFragmentManager.beginTransaction().apply { replace(R.id.container_main,orderFragment).addToBackStack(null).commit() }
                R.id.home-> startActivity(Intent(applicationContext,MainActivity::class.java))
            }
            true
        }


        binding.rvOrder.layoutManager = LinearLayoutManager(this)

        fStore.collection("users").get().addOnCompleteListener {
            if (it.isSuccessful){
                for (document in it.result!!){
                    Log.d(TAG, document.id + " data=> " + document.data)
                }
            }

        }
        fStore.collection("order").whereEqualTo("status", "waiting").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    if (task.result?.count() == 0){
                        binding.noOrder.visibility = View.VISIBLE
                    }else{
                        binding.noOrder.visibility = View.INVISIBLE
                    }

                    for (document in task.result!!) {

//                        doc = document.data["id_type"] as DocumentReference
//                        var name: String;
//                        var id: Int;
                        var type = Type()
//                        doc.get().addOnCompleteListener {
//                            val data = it.result
//                            Log.d(TAG, it.result?.data?.get("name")?.toString().toString())
//                            Log.d(TAG, it.result?.id.toString())
//                            it.result?.data
//                            name = it.result?.data?.get("name")?.toString().toString()
//                            setType(type, data)
//
//                        }
//                        Log.d(TAG, type.toString())
//

                        val order = Order(
                            id = document.id,
                            idInvoice = document["id_invoice"].toString(),
                            idDriver = document["id_driver"].toString(),
                            idUser = "Chandra Muhamad Apriana",
                            idType = type,
                            address = document["address"].toString(),
                            amount = document["amount"].toString().toInt(),
                            price = document["total_price"].toString().toInt(),
                            latitude = document["latitude"].toString(),
                            longitude = document["longitude"].toString(),
                            status = document["status"].toString(),
                            date = document["date"].toString()
                        )

                        listOrderGarbage.add(order)
                    }
                    listOrder = ListOrderAdapter(listOrderGarbage)
                    listOrder.setOnItemClickCallback(object : ListOrderAdapter.OnItemClickCallback {
                        override fun onItemClicked(order: Order) {
                            val bundle = Bundle()
                            bundle.putParcelable(DetailFragment.ORDERDETAIL, order)
                            bundle.putString(DetailFragment.ID,order.id)
                            val detailFragment = DetailFragment()
                            detailFragment.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.container_main, detailFragment).addToBackStack(null)
                                .commit()
                            Log.d(TAG, "clicked")
                        }
                    })
                    binding.rvOrder.adapter = listOrder


                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private fun setType(
        type: Type,
        data: DocumentSnapshot?
    ): Type {
        var type1 = type
        type1 =
            Type(data?.id.toString(), data!!["name"].toString(), data["price"].toString().toInt())
        return type1
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }


}