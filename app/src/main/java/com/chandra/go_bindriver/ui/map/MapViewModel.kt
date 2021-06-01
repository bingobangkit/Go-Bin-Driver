package com.chandra.go_bindriver.ui.map

import androidx.lifecycle.ViewModel
import com.chandra.go_bindriver.data.Repository
import com.chandra.go_bindriver.utils.lazyDeferred

class MapViewModel: ViewModel() {

    private val repository by lazy { Repository() }
    val orderByWaiting by lazyDeferred {
        repository.getOrder("waiting")
    }

    val orderByOngoing by lazyDeferred {
        repository.getOrder("ongoing")
    }

}