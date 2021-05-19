package com.chandra.go_bindriver.ui.detail

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentDetailBinding
import com.chandra.go_bindriver.ui.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    lateinit var status: String

    companion object {
        const val ID: String = "id"
        const val ORDERDETAIL: String = "orderdetail"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)

        val fragment = MapFragment()
        parentFragmentManager.beginTransaction().replace(R.id.layout_gmaps, fragment).commit()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        binding.toolbarDetail.root.setPadding(0,0,0,0)
        binding.toolbarDetail.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack();
        }

        val id = args?.getString(ID)
        val fStore = FirebaseFirestore.getInstance()

        getOrder(fStore, id)
        val botnav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botnav.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getOrder(fStore: FirebaseFirestore, id: String?) {

        fStore.collection("order").document(id.toString()).get().addOnCompleteListener { task ->
            val data = task.result
            status = task.result?.get("status").toString()
            if (status == "ongoing") {
                binding.btnPickup.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
                binding.btnPickup.text = "Complete"
            } else if (status == "complete") {
                binding.btnPickup.visibility = View.GONE
            }

            binding.apply {
                setTextDetail(data)
                btnPickup.setOnClickListener {
                    if (status == "waiting") {
                        status = "ongoing"
                        updateStatus(fStore, data!!, status)
                        binding.btnPickup.setBackgroundResource(R.drawable.background_button_complete)
                        binding.btnPickup.text = "Complete"
                    } else if (status == "ongoing") {
                        status = "complete"
                        updateStatus(fStore, data!!, status)
                        btnPickup.visibility = View.GONE
                    }
                }


            }

        }

    }

    private fun FragmentDetailBinding.setTextDetail(data: DocumentSnapshot?) {
        tvDetailAddress.text = data!!["address"].toString()
        tvDetailDate.text = data["date"].toString()
        tvDetailAmount.text = data["amount"].toString() + "Kg"
        tvDetailPrice.text =
            data["amount"].toString() + "Kg" + " x Rp." + "2000"
        tvDetailTotal.text = "Rp. " + data["total_price"].toString()
        tvDetailDistance.text = data["status"].toString()
    }

    private fun FragmentDetailBinding.updateStatus(
        fStore: FirebaseFirestore,
        data: DocumentSnapshot, status: String
    ) {
        fStore.collection("order").document(data.id).update("status", status)
            .addOnCompleteListener {
                Toast.makeText(
                    requireContext(),
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failure",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }



    override fun onDestroy() {
        super.onDestroy()
        val botnav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botnav.visibility = View.VISIBLE
    }


}