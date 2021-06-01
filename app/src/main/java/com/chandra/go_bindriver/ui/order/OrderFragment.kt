package com.chandra.go_bindriver.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.FragmentOrderBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


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
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        binding.toolbarOrder.appName.text = "YOUR PICKUP ORDER"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarOrder.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

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