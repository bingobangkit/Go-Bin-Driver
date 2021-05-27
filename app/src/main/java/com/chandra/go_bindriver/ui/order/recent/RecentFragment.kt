package com.chandra.go_bindriver.ui.order.recent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentRecentBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.Type
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.home.ListOrderAdapter
import com.chandra.go_bindriver.ui.home.MainActivity
import com.google.firebase.firestore.FirebaseFirestore


class RecentFragment : Fragment() {

    private lateinit var binding:FragmentRecentBinding
    private var listOrderGarbage = ArrayList<Order>()
    private lateinit var listOrder: ListOrderAdapter
    var fStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater,container,false)
        binding.rvRecent.layoutManager = LinearLayoutManager(context)

        realtimeUpdates()
        return binding.root
    }



    private fun realtimeUpdates() {
        fStore.collection("order").whereEqualTo("status", "complete")
            .addSnapshotListener { value, error ->
                val listOrderGarbage = ArrayList<Order>()

                if (value?.count() == 0) {
                    binding.noOrderRecent.visibility = View.VISIBLE
                } else {
                    binding.noOrderRecent.visibility = View.INVISIBLE
                }

                value.let {
                    if (it != null) {
                        for (document in it) {
                            val type = Type()

                            val order = Order(
                                id = document.id,
                                id_invoice = document["id_invoice"].toString(),
                                id_driver = document["id_driver"].toString(),
                                id_user = "Chandra Muhamad Apriana",
                                id_type = "1",
                                address = document["address"].toString(),
                                amount = document["amount"].toString(),
                                total_price = document["total_price"].toString(),
                                latitude = document["latitude"].toString(),
                                longitude = document["longitude"].toString(),
                                status = document["status"].toString(),
                                date = document["date"].toString()
                            )

                            listOrderGarbage.add(order)
                        }
                    }
                    listOrder = ListOrderAdapter(listOrderGarbage)
                    listOrder.setOnItemClickCallback(object : ListOrderAdapter.OnItemClickCallback {
                        override fun onItemClicked(order: Order) {
                            val bundle = Bundle()
//                            bundle.putParcelable(DetailFragment.ORDERDETAIL, order)
                            bundle.putString(DetailFragment.ID, order.id)
                            val detailFragment = DetailFragment()
                            detailFragment.arguments = bundle
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container_order, detailFragment).addToBackStack(null)
                                .commit()

                        }
                    })
                    binding.rvRecent.adapter = listOrder

                }
            }
    }


}