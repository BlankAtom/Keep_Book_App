package com.thread0.bookkeeping.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.assetsIcons
import com.thread0.bookkeeping.entity.room.BillEntity

class AccountAdapter(
    val con: Context,
    var data: List<BillEntity>,
    private val itemClickListener: (BillEntity?) -> Unit
) : ArrayAdapter<BillEntity>(con,0,data){

    fun setOnDataChanged(list: List<BillEntity>) {
        this.data = list
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View = LayoutInflater.from(context).inflate(R.layout.fragment_account_item, parent, false)
        val bill = getItem(position)!!
        var viewIcon = view.findViewById<ImageView>(R.id.account_item_icon)
        var viewText1 = view.findViewById<TextView>(R.id.account_item_text_1)
        var viewText2 = view.findViewById<TextView>(R.id.account_item_text_2)
        if( bill.isIncome ) {
            viewIcon.setImageResource(R.mipmap.icon_zhangdan_shouru)
        }
        else {
            viewIcon.setImageResource(R.mipmap.icon_zhangdan_zhichu)
        }
        viewText1.text = bill.note
        viewText2.text = bill.price.toString()

        view.setOnClickListener {
            itemClickListener(getItem(position))
        }
        return view
    }

    override fun getItem(position: Int): BillEntity? {
        return super.getItem(position)
    }
}