package com.chandra.go_bindriver.ui.order.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentOngoingBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.home.ListOrderAdapter
import com.chandra.go_bindriver.ui.map.MapFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class OngoingFragment : Fragment() {
    private lateinit var binding: FragmentOngoingBinding
    private val viewModel:OngoingViewModel by viewModels()

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
        val fabMaps = requireActivity().findViewById<FloatingActionButton>(R.id.fab_maps)
        fabMaps.visibility = View.GONE

        binding.fabMapsOngoing.setOnClickListener {
            val fragment = MapFragment()
            val bundle = Bundle()
            bundle.putString(MapFragment.FROM, "ongoing")
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.container_order, fragment)
                .addToBackStack(null).commit()
        }

        realtimeUpdates()

    }

    private fun realtimeUpdates() {
        lifecycleScope.launch {
            viewModel.orderByOngoing.await().observe(viewLifecycleOwner,{value->
                val listOrderGarbage = ArrayList<Order>()

                if (value?.count() == 0) {
                    binding.noOrderOngoing.visibility = View.VISIBLE
                } else {
                    binding.noOrderOngoing.visibility = View.INVISIBLE
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
            })
        }

    }



}