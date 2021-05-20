package com.chandra.go_bindriver.ui.order.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentOngoingBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.Type
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.home.ListOrderAdapter
import com.google.firebase.firestore.FirebaseFirestore


class OngoingFragment : Fragment() {
    private lateinit var binding: FragmentOngoingBinding

    private var listOrderGarbage = ArrayList<Order>()
    private lateinit var listOrder: ListOrderAdapter
    var fStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOngoingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvOngoing.layoutManager = LinearLayoutManager(context)

        realtimeUpdates()

    }

    private fun realtimeUpdates() {
        fStore.collection("order").whereEqualTo("status", "ongoing")
            .addSnapshotListener { value, error ->
                val listOrderGarbage = ArrayList<Order>()

                if (value?.count() == 0) {
                    binding.noOrderOngoing.visibility = View.VISIBLE
                } else {
                    binding.noOrderOngoing.visibility = View.INVISIBLE
                }

                value.let {
                    if (it != null) {
                        for (document in it) {
                            val type = Type()

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
                    }
                    listOrder = ListOrderAdapter(listOrderGarbage)
                    listOrder.setOnItemClickCallback(object : ListOrderAdapter.OnItemClickCallback {
                        override fun onItemClicked(order: Order) {
                            val bundle = Bundle()
                            bundle.putParcelable(DetailFragment.ORDERDETAIL, order)
                            bundle.putString(DetailFragment.ID, order.id)
                            val detailFragment = DetailFragment()
                            detailFragment.arguments = bundle
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container_order, detailFragment).addToBackStack(null)
                                .commit()

                        }
                    })
                    binding.rvOngoing.adapter = listOrder

                }
            }
    }


}