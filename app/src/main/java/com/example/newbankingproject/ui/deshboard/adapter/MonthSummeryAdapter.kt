package com.example.newbankingproject.ui.deshboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.newbankingproject.databinding.ItemDateBinding

/**MonthSummeryAdapter is used to set monthly summery adapter list*/
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

/**MonthSummeryViewHolder is view holder class which elaborate the views for MonthSummeryAdapter adapter class */
class MonthSummeryViewHolder(var item: ItemDateBinding) : RecyclerView.ViewHolder(item.root) {
    fun bind(data: DashboardProfileData?) {
        item.apply {
            tvName.text = data?.productName ?: "Milk"
            tvQuantity.text = String.format("%d %s", data?.totalQty ?: 1, data?.unit ?: "Lt")
        }
    }


}