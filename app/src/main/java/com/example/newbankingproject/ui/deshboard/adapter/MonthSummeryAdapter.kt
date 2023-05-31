package com.example.newbankingproject.ui.deshboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.newbankingproject.databinding.ItemDateBinding

class MonthSummeryAdapter constructor(
    val data: ObservableArrayList<DashboardProfileData>,
) : RecyclerView.Adapter<MonthSummeryViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthSummeryViewHolder {
        context = parent.context
        return MonthSummeryViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MonthSummeryViewHolder, position: Int) {
        val positionData = data[position]
        holder.bind(positionData)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class MonthSummeryViewHolder(var item: ItemDateBinding) : RecyclerView.ViewHolder(item.root) {
    fun bind(data: DashboardProfileData?) {
        item.apply {
            tvName.text = data?.productName
            tvQuantity.text = String.format("%d %s", data?.totalQty, data?.unit)
        }
    }


}