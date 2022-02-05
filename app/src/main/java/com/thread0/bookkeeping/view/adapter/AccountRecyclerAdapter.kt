package com.thread0.bookkeeping.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.entity.room.BillEntity
import com.yanzhenjie.recyclerview.SwipeRecyclerView

class AccountRecyclerAdapter(
    ctx: Context,
    var data: List<BillEntity>
) : RecyclerView.Adapter<AccountRecyclerAdapter.SwipeViewHolder>(){
    private var inflater: LayoutInflater = LayoutInflater.from(ctx)
    fun getInflater() = inflater

    fun notifyDataSetChanged(list: List<BillEntity>) {
        this.data = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        return SwipeViewHolder(getInflater().inflate(R.layout.fragment_account_item, parent, false))
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size

    inner class SwipeViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bill: BillEntity) {
            view.findViewById<ImageView>(R.id.account_item_icon).setImageResource(
                if (bill.isIncome)
                    R.mipmap.icon_zhangdan_shouru
                else
                    R.mipmap.icon_zhangdan_zhichu
            )
            view.findViewById<TextView>(R.id.account_item_text_1).text = bill.note
            view.findViewById<TextView>(R.id.account_item_text_2).text = bill.price.toString()
        }
    }

}