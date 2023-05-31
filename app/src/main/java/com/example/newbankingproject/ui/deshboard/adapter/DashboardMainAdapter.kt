package com.example.newbankingproject.ui.deshboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.dashboard.DashboardData
import com.example.domain.model.dashboard.DashboardProfileData
import com.example.newbankingproject.databinding.ItemParentHomeBinding
import com.example.newbankingproject.util.Constant.DAILY_NEEDS
import com.example.newbankingproject.util.Constant.MONTH_SUMMERY

class DashboardMainAdapter constructor() : RecyclerView.Adapter<DashboardViewHolder>() {
    lateinit var context: Context
    val data: ObservableArrayList<DashboardData> = ObservableArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        context = parent.context
        return DashboardViewHolder(
            ItemParentHomeBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    fun updateData( data: ObservableArrayList<DashboardData>){
        this.data.clear()
        this.data.addAll(data)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val positionData = data[position]
        holder.bind(positionData, context)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class DashboardViewHolder(var item: ItemParentHomeBinding) : RecyclerView.ViewHolder(item.root) {
    var dataList = ObservableArrayList<DashboardProfileData>()

    fun bind(data: DashboardData?, context: Context) {
        item.tvNeeds.text = data?.title.toString()
        data?.data?.let { dataList.addAll(it) }
        initializeAdapter(data, context)
    }

    private fun initializeAdapter(data: DashboardData?, context: Context) {
        when (data?.type) {
            MONTH_SUMMERY -> {
                val monthSummeryAdapter = MonthSummeryAdapter(dataList)
                item.apply {
                    rvDailyNeeds.adapter = monthSummeryAdapter
                    rvDailyNeeds.setHasFixedSize(true)
                    rvDailyNeeds.layoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }

            }

            DAILY_NEEDS -> {
                val dailyNeedsAdapter = DailyNeedsAdapter(dataList, data.type)
                item.apply {
                    rvDailyNeeds.adapter = dailyNeedsAdapter
                    rvDailyNeeds.setHasFixedSize(true)
                    rvDailyNeeds.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            }

            else -> {
                val dailyNeedsAdapter = DailyNeedsAdapter(dataList, data?.type)
                item.apply {
                    rvDailyNeeds.adapter = dailyNeedsAdapter
                    rvDailyNeeds.setHasFixedSize(true)
                    rvDailyNeeds.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            }
        }
    }

}
