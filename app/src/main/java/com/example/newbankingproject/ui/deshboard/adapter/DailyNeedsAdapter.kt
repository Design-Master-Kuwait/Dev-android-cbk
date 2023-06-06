package com.example.newbankingproject.ui.deshboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.newbankingproject.databinding.ItemDailyNeedsBinding
import com.example.newbankingproject.util.Constant

/** DailyNeedsAdapter is adapter class for daily needs list*/
class DailyNeedsAdapter constructor(
    val data: ObservableArrayList<DashboardProfileData>,
    val type: String?,
) : RecyclerView.Adapter<DailyNeedsViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyNeedsViewHolder {
        context = parent.context
        return DailyNeedsViewHolder(
            ItemDailyNeedsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyNeedsViewHolder, position: Int) {
        val positionData = data[position]
        holder.bind(positionData, type)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

/**DailyNeedsViewHolder is view holder class for DailyNeedsAdapter recycler view */
class DailyNeedsViewHolder(var item: ItemDailyNeedsBinding) : RecyclerView.ViewHolder(item.root) {
    fun bind(data: DashboardProfileData?, type: String?) {
        item.apply {
            tvName.text = data?.productName
            tvNameValue.text = if (type == Constant.MONTH_SUMMERY) String.format(
                "%d %s",
                data?.totalQty,
                data?.unit
            ) else String.format("%d %s", data?.totalPrice, data?.unit)
        }
    }


}