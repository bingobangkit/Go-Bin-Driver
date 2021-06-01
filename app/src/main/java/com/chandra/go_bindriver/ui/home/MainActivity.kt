package com.chandra.go_bindriver.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.go_bindriver.R
import com.chandra.go_bindriver.databinding.ActivityMainBinding
import com.chandra.go_bindriver.model.Order
import com.chandra.go_bindriver.model.Type
import com.chandra.go_bindriver.ui.detail.DetailFragment
import com.chandra.go_bindriver.ui.map.MapFragment
import com.chandra.go_bindriver.ui.order.OrderFragment
import com.google.firebase.firestore.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listOrder: ListOrderAdapter
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var query: Query


    companion object {
        const val TAG = "USERS"
    }

    var fStore = FirebaseFirestore.getInstance()
    lateinit var doc: DocumentReference

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvOrder.layoutManager = LinearLayoutManager(this)
        binding.fabMaps.setOnClickListener {
            val fragment = MapFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container_main, fragment)
                .commit()
        }
        binding.fabMaps.visibility = View.VISIBLE

        bottomNavigation()
        realtimeUpdates()


    }

    private fun bottomNavigation() {
        val orderFragment = OrderFragment()
        val iconColorStates = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                Color.parseColor("#A4A4A4"),
                Color.parseColor("#FFFFFF")
            )
        )

        binding.bottomNavigation.itemIconTintList = iconColorStates
        binding.bottomNavigation.itemTextColor = iconColorStates
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.order -> {
                    it.isCheckable = true
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container_main, orderFragment).addToBackStack(null).commit()
                    }
                }
                R.id.home ->{
                    it.isCheckable = true
                    startActivity(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )}
            }
            true
        }
    }

    private fun setType(
        type: Type,
        data: DocumentSnapshot?
    ): Type {
        var type1 = type
        type1 =
            Type(data?.id.toString(), data!!["name"].toString(), data["price"].toString().toInt())
        return type1
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    private fun realtimeUpdates() {

        lifecycleScope.launch {
            viewModel.orderByWaiting.await().observe(this@MainActivity, {
                if (it.count() == 0) {
                    binding.noOrder.visibility = View.VISIBLE
                } else {
                    binding.noOrder.visibility = View.INVISIBLE
                }

                listOrder = ListOrderAdapter(it)
                binding.rvOrder.adapter = listOrder

                binding.fabMaps.setOnClickListener {
                    val fragment = MapFragment()
                    val bundle = Bundle()
                    bundle.putString(MapFragment.FROM, "home")
                    fragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container_main, fragment)
                        .addToBackStack(null).commit()
                }

                listOrder.setOnItemClickCallback(object : ListOrderAdapter.OnItemClickCallback {
                    override fun onItemClicked(order: Order) {
                        val bundle = Bundle()
                        bundle.putString(DetailFragment.ID, order.id)
                        val detailFragment = DetailFragment()
                        detailFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container_main, detailFragment).addToBackStack(null)
                            .commit()

                    }
                })
            })
        }


    }


}

