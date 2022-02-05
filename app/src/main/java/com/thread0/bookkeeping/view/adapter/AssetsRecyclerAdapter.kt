package com.thread0.bookkeeping.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.entity.room.AssetsEntity
import kotlinx.android.synthetic.main.recycle_assets_rows.view.*

class AssetsRecyclerAdapter(
    private val lists: List<AssetsEntity>,
    private val itemClickListener: (AssetsEntity) -> Unit
) : RecyclerView.Adapter<AssetsRecyclerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.recycle_assets_rows,  parent, false)
        return MyViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    class MyViewHolder(itemView: View, val itemClickListener: (AssetsEntity) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(assets: AssetsEntity) {
            itemView.img_assets_item.setImageResource(assets.getIcon())
            itemView.txt_name_assets_item.text = assets.getTitleName()
            itemView.assets_item_invest.text = assets.amount.toString()
            if( assets.visible ) {
                itemView.detail_btn_assets_item.setImageResource(R.drawable.ic_return_black)
                itemView.detail_btn_assets_item.scaleX = -1f
                itemView.detail_btn_assets_item.alpha = 0.5f
                if( assets.amount < 0 ) {
                    itemView.assets_item_invest.setTextColor(Color.RED)
                }
            } else {
                itemView.assets_item_invest.text = "****"
                itemView.detail_btn_assets_item.setImageResource(R.mipmap.icon_assets_closed)
                itemView.detail_btn_assets_item.setOnClickListener {
                    assets.visible = true
                    itemView.detail_btn_assets_item.setImageResource(R.drawable.ic_return_black)
                    itemView.detail_btn_assets_item.scaleX = -1f
                    itemView.detail_btn_assets_item.alpha = 0.5f
                    itemView.assets_item_invest.text = assets.amount.toString()
                }
            }
            itemView.setOnClickListener {
                itemClickListener(assets)
            }
        }
    }
}