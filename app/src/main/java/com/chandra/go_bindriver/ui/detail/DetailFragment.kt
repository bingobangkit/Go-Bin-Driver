package com.chandra.go_bindriver.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentDetailBinding
import com.chandra.go_bindriver.model.Order


class DetailFragment : Fragment() {

    private lateinit var binding:FragmentDetailBinding

    companion object{
        const val ORDERDETAIL : String = "orderdetail"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val data = args?.getParcelable<Order>(ORDERDETAIL)
        Log.d("detail", data.toString())
    }



}