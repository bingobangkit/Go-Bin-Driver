package com.chandra.go_bindriver.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chandra.go_bindriver.databinding.ItemOrderBinding

import com.chandra.go_bindriver.model.Order

class ListOrderAdapter(private val listOrder:List<Order>) :RecyclerView.Adapter<ListOrderAdapter.ListViewHolder>(){

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    inner class ListViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val order = listOrder[position]
        holder.binding.apply {
            tvName.text = "Priambudi Sutanto"
            tvAmount.text = order.amount +"kg"
            tvDistance.text = order.status
            tvDate.text = order.date
            tvPrice.text = "Rp. "+ order.total_price
            root.setOnClickListener { onItemClickCallback?.onItemClicked(order) }
        }



    }

    override fun getItemCount(): Int = listOrder.size



    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(order: Order)
    }


}