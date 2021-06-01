package com.chandra.go_bindriver.ui.detail

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentDetailBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.ui.map.MapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    lateinit var status: String
    var income = 0
    companion object {
        const val ID: String = "id"
        const val ID_USER:String = "ZS91wKaGmkdMvCTZ4Ko6B8EkUw52"
        const val ORDERDETAIL: String = "orderdetail"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


        val args = arguments
        binding.toolbarDetail.root.setPadding(0, 0, 0, 0)
        binding.toolbarDetail.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val id: String = args?.getString(ID).toString()

        getDetailOrder(id)
        val botnav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fabMaps = requireActivity().findViewById<FloatingActionButton>(R.id.fab_maps)
        fabMaps.visibility = View.GONE
        botnav.visibility = View.GONE
        Log.d("detail", income.toString())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getDetailOrder(id: String) {
        lifecycleScope.launch {
            detailViewModel.getDetailOrder(id).observe(viewLifecycleOwner, { value ->
                status = value.status
                if (status == "ongoing") {
                    binding.btnDirections.visibility = View.VISIBLE
                    binding.btnPickup.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.green))
                    binding.btnPickup.text = "Complete"
                } else if (status == "complete") {
                    binding.btnDirections.visibility = View.GONE
                    binding.btnPickup.visibility = View.GONE
                }

                val mapFragment = MapFragment()
                val bundle = Bundle()
                bundle.putString(MapFragment.FROM, "detail")
                bundle.putBundle(
                    MapFragment.LOCATION,
                    bundleOf("latitude" to value.latitude, "longitude" to value.longitude)
                )
                mapFragment.arguments = bundle
                parentFragmentManager.beginTransaction().replace(R.id.layout_gmaps, mapFragment).commit()

                binding.apply {
                    setTextDetail(value)
                    btnPickup.setOnClickListener {
                        if (status == "waiting") {
                            status = "ongoing"
                            updateStatus(value.id, status)
                            binding.btnPickup.setBackgroundResource(R.drawable.background_button_complete)
                            binding.btnPickup.text = "Complete"
                        } else if (status == "ongoing") {
                            getUserById(ID_USER,value.total_price.toInt()-2000)
                            Log.d("detail","clicked")
                            status = "complete"
                            updateStatus(value.id, status)
                            btnPickup.visibility = View.GONE
                        }

                    }
                    btnDirections.setOnClickListener {
                        navigate(value.latitude, value.longitude)
                    }


                }
            })
        }
    }


    private fun FragmentDetailBinding.setTextDetail(data: Order) {
        tvDetailAddress.text = data.address
        tvDetailDate.text = data.date
        tvDetailAmount.text = StringBuilder(data.amount + " kg")
        tvDetailPrice.text = StringBuilder("Rp. " + data.total_price)
        txtDetailTotalAmount.text = StringBuilder("Rp. " + data.total_price)
        tvDetailTotal.text = StringBuilder("Rp. " +(data.total_price.toInt() - 2000).toString())
        tvDetailDistance.text = data.status
    }

    private fun updateStatus(
        id: String, status: String
    ) {
        val isSuccess = detailViewModel.updateStatus(id, status)
        if (isSuccess) {
            Toast.makeText(
                requireContext(),
                "Success",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Failure",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val botnav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botnav.visibility = View.VISIBLE
    }

    private fun navigate(latitude: String, longitude: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44
            )

        } else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) {
                    Log.d("detail", "${location.latitude} ${location.longitude}")
                    try {
                        val source = "${location.latitude},${location.longitude}"
                        Log.d("detail", "$latitude $longitude")
                        val destination = "${latitude},${longitude}"
//                        val uri: Uri =
//                            Uri.parse("https://www.google.com/maps/dir/$source/$destination")
                        val uri: Uri =
                            Uri.parse("http://maps.google.com/maps?daddr=$destination")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }

                } else {
                    Log.d("detail", "null")
                }
            }.addOnFailureListener {
                Log.d("detail", it.message.toString())

            }
        }

    }


    private fun getUserById(id: String, balance: Int) {
        FirebaseFirestore.getInstance().collection("users").document(id).get()
            .addOnCompleteListener {
                if (it.isComplete) {
                    updateBalance(id, (it.result["saldo"].toString().toInt() + balance).toString())
                }
            }
    }


    private fun updateBalance(id: String, balance: String) {

        detailViewModel.updateBalance(id, balance)

    }

}