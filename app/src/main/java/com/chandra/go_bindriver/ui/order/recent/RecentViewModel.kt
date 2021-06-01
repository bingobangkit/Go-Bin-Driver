package com.chandra.go_bindriver.ui.order.recent

import androidx.lifecycle.ViewModel
import com.chandra.go_bindriver.data.Repository
import com.chandra.go_bindriver.utils.lazyDeferred

class RecentViewModel: ViewModel() {
    val repository = Repository()
    val orderByComplete by lazyDeferred {
        repository.getOrder("complete")
    }
}