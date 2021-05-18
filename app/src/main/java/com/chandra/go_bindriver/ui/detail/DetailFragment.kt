package com.chandra.go_bindriver.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentDetailBinding
import com.chandra.go_bindriver.ui.MapFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


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
        fragmentManager?.beginTransaction()?.replace(R.id.layout_gmaps, fragment)?.commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
//        val data = args?.getParcelable<Order>(ORDERDETAIL)
        val id = args?.getString(ID)
        val fStore = FirebaseFirestore.getInstance()

        getOrder(fStore, id)
    }

    private fun getOrder(fStore: FirebaseFirestore, id: String?) {
        CoroutineScope(IO).launch {
            fStore.collection("order").document(id.toString()).get().addOnCompleteListener { task ->
                val data = task.result
                status = task.result?.get("status").toString()
                binding.apply {
                    setTextDetail(data)

                    if (status == "waiting") {
                        btnPickup.setOnClickListener {
                            status = "ongoing"
                            updateStatus(fStore, data!!)
                        }
                    }else if(status == "ongoing"){
                        Log.d("DETAIL",status)
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
    }

    private fun FragmentDetailBinding.updateStatus(
        fStore: FirebaseFirestore,
        data: DocumentSnapshot
    ) {
        fStore.collection("order").document(data.id).update("status", "ongoing")
            .addOnCompleteListener {
                getOrder(fStore,data.id)
                btnPickup.setBackgroundColor(0x69C45A)
                btnPickup.text = "Complete"

                Toast.makeText(
                    requireContext(),
                    "Success Pickup, Now Let's Go Drive",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failure Pickup",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }


}