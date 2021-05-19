package com.chandra.go_bindriver.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->

            val markerOptions = MarkerOptions()

            val latitude: Float = "-7.060126959311316".toFloat()
            val longitude: Float = "107.74941552634316".toFloat()

            val addressUser = LatLng(
                latitude.toDouble(), longitude.toDouble()
            )

            markerOptions.position(addressUser)
            markerOptions.title("user")
            googleMap.clear()

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressUser, 15f))
            googleMap.addMarker(markerOptions)


        }
        return binding.root
    }


}