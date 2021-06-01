package com.chandra.go_bindriver.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentMapBinding
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()

    companion object {
        const val FROM = "from"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val LOCATION = "location"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        val from = arguments?.get(FROM)
        binding.txtTitleMaps.bringToFront()


        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.clear()
            val markerOptions = MarkerOptions()

            if (from == "home") {
                binding.txtTitleMaps.text="Upcoming Order"
                lifecycleScope.launch {
                    viewModel.orderByWaiting.await().observe(viewLifecycleOwner, {
                        Log.d("map", it.toString())
                        for (i in it.indices) {
                            val latitude: Float = it[i].latitude.toFloat()
                            val longitude: Float = it[i].longitude.toFloat()

                            val addressUser = LatLng(
                                latitude.toDouble(), longitude.toDouble()
                            )
                            markerOptions.position(addressUser)
                            markerOptions.title(it[i].address)


                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    addressUser,
                                    15f
                                )
                            )
                            googleMap.addMarker(markerOptions).tag = it[i].id

                            googleMap.setOnMarkerClickListener { marker ->
                                val bundle = Bundle()
                                bundle.putString(DetailFragment.ID, marker.tag.toString())
                                val detailFragment = DetailFragment()
                                detailFragment.arguments = bundle
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.container_main, detailFragment)
                                    .addToBackStack(null)
                                    .commit()
                                true
                            }
                        }


                    })
                }
            } else if (from == "detail") {
                binding.txtTitleMaps.text=""
                val orderLatitude: String = arguments?.getBundle(LOCATION)?.getString("latitude").toString()
                val orderLongitude: String = arguments?.getBundle(LOCATION)?.getString("longitude").toString()
                val latitude: Float = orderLatitude.toFloat()
                val longitude: Float = orderLongitude.toFloat()

                val addressUser = LatLng(
                    latitude.toDouble(), longitude.toDouble()
                )

                markerOptions.position(addressUser)
                markerOptions.title("user")
                googleMap.clear()

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressUser, 15f))
                googleMap.addMarker(markerOptions)
            } else if(from == "ongoing"){
                binding.txtTitleMaps.text="The Order You Take"
                lifecycleScope.launch {
                    viewModel.orderByOngoing.await().observe(viewLifecycleOwner, {
                        Log.d("map", it.toString())
                        for (i in it.indices) {
                            val latitude: Float = it[i].latitude.toFloat()
                            val longitude: Float = it[i].longitude.toFloat()

                            val addressUser = LatLng(
                                latitude.toDouble(), longitude.toDouble()
                            )
                            markerOptions.position(addressUser)
                            markerOptions.title(it[i].address)


                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    addressUser,
                                    15f
                                )
                            )
                            googleMap.addMarker(markerOptions).tag = it[i].id

                            googleMap.setOnMarkerClickListener { marker ->
                                val bundle = Bundle()
                                bundle.putString(DetailFragment.ID, marker.tag.toString())
                                val detailFragment = DetailFragment()
                                detailFragment.arguments = bundle
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.container_order, detailFragment)
                                    .addToBackStack(null)
                                    .commit()
                                true
                            }
                        }

                    })
                }
            }

        }
        return binding.root
    }


}