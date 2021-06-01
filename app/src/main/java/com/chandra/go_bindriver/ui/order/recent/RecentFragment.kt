package com.chandra.go_bindriver.ui.order.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentRecentBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.home.ListOrderAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class RecentFragment : Fragment() {

    private lateinit var binding:FragmentRecentBinding
    private val viewModel :RecentViewModel by viewModels()
    private lateinit var listOrder: ListOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater,container,false)
        binding.rvRecent.layoutManager = LinearLayoutManager(context)
        val fabMaps = requireActivity().findViewById<FloatingActionButton>(R.id.fab_maps)
        fabMaps.visibility = View.GONE
        realtimeUpdates()
        return binding.root
    }



    private fun realtimeUpdates() {
        lifecycleScope.launch {
            viewModel.orderByComplete.await().observe(viewLifecycleOwner,{value->
                val listOrderGarbage = ArrayList<Order>()

                if (value?.count() == 0) {
                    binding.noOrderRecent.visibility = View.VISIBLE
                } else {
                    binding.noOrderRecent.visibility = View.INVISIBLE
                }

                value.let {
                    if (it != null) {
                        for (document in it) {

                            val order = Order(
                                id = document.id,
                                id_invoice = document.id_invoice,
                                id_driver = document.id_driver,
                                id_user = "Chandra Muhamad Apriana",
                                id_type = "1",
                                address = document.address,
                                amount = document.amount,
                                total_price =  document.total_price,
                                latitude = document.latitude,
                                longitude = document.longitude,
                                status = document.status,
                                date = document.date
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
            })
        }

    }


}