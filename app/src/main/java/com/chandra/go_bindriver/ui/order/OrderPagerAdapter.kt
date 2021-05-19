package com.chandra.go_bindriver.ui.order

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chandra.go_bindriver.ui.order.ongoing.OngoingFragment
import com.chandra.go_bindriver.ui.order.recent.RecentFragment

class OrderPagerAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OngoingFragment()
            1 -> fragment = RecentFragment()
        }
        return fragment as Fragment
    }
}