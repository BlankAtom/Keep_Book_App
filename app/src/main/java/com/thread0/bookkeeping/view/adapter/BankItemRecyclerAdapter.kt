package com.thread0.bookkeeping.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.bankNameList
import com.thread0.bookkeeping.entity.BankData
import kotlinx.android.synthetic.main.recycle_add_assets_rows.view.*

class BankItemRecyclerAdapter(
    private var bankList: List<BankData>,
    private val itemClickListener: (BankData) -> Unit
) : RecyclerView.Adapter<BankItemRecyclerAdapter.BankHolder>() {

    class BankHolder(itemView: View, val itemClickListener: (BankData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BankData) {
            itemView.img_add_assets_item.setImageResource(item.bankIcon)
            itemView.txt_add_name_assets_item.text = bankNameList[item.bankName]
            itemView.setOnClickListener{itemClickListener(item)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        return BankHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycle_add_assets_rows, parent, false),itemClickListener)
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        holder.bind(bankList[position])
    }

    override fun getItemCount(): Int = bankList.size
}