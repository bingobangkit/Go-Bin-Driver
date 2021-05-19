package com.chandra.go_bindriver.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chandra.go_bindriver.databinding.ItemOrderBinding
import com.chandra.go_bindriver.model.Order

class OrderFireStoreAdapter(private val listOrder:List<Order>) :RecyclerView.Adapter<OrderFireStoreAdapter.ListViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val order = listOrder[position]
        holder.binding.apply {
            tvName.text = order.idUser
            tvAmount.text = order.amount.toString()
            tvDistance.text = "5km"
            tvDate.text = order.date
            tvPrice.text = order.price.toString()
        }

    }

    override fun getItemCount(): Int = listOrder.size


    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(order: Order)
    }


}