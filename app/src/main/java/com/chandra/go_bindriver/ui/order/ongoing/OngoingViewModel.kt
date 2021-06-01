package com.chandra.go_bindriver.ui.order.ongoing

import androidx.lifecycle.ViewModel
import com.chandra.go_bindriver.data.Repository
import com.chandra.go_bindriver.utils.lazyDeferred

class OngoingViewModel:ViewModel() {

    private val repository by lazy { Repository() }

    val orderByOngoing by lazyDeferred {
        repository.getOrder("ongoing")
    }
}