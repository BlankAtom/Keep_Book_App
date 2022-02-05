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

class AssetsGroupAdapter (
    con: Context,
    val data: List<String>,
    val tags: List<String>,
    private val itemClickListener: (String?) -> Unit
) : ArrayAdapter<String>(con, 0, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        if( tags.contains(getItem(position))) {
            view = LayoutInflater.from(context).inflate(R.layout.group_list_item_tag, parent, false)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false)
            val imgView = view.findViewById<ImageView>(R.id.group_list_item_img)
            var icon = assetsIcons[getItem(position)]
            if (icon == null){
                icon = R.mipmap.icon_zidingyi
            }
            imgView.setImageResource(icon)
        }

        val txtView: TextView = view.findViewById(R.id.group_list_item_text)
        txtView.text = getItem(position)
        view.setOnClickListener {
            itemClickListener(getItem(position))
        }

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        if( tags.contains(getItem(position))){
            return false
        }
        return super.isEnabled(position)
    }

}