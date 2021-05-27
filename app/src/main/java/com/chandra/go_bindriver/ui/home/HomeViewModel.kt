package com.chandra.go_bindriver.ui.home

import androidx.lifecycle.ViewModel
import com.chandra.go_bindriver.data.Repository
import com.chandra.go_bindriver.utils.lazyDeferred

class HomeViewModel() : ViewModel() {
    private val repository by lazy { Repository() }

    val orderByComplete by lazyDeferred {
        repository.getOrder("complete")
    }

    val orderByWaiting by lazyDeferred {
        repository.getOrder("waiting")
    }

    val orderByOngoing by lazyDeferred {
        repository.getOrder("ongoing")
    }

}