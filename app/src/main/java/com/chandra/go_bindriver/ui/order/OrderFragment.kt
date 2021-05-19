package com.chandra.go_bindriver.ui.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentOrderBinding

import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.Type
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.home.ListOrderAdapter
import com.chandra.go_bindriver.ui.home.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding


    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(R.string.ongoing, R.string.recent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderPagerAdapter = OrderPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = orderPagerAdapter
        viewPager.isSaveEnabled = false
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }
}