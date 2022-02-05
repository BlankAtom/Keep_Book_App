package com.thread0.bookkeeping.view.layout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.thread0.bookkeeping.R

class AssetsTitleBar(context: Context, attributeSet: AttributeSet) : RelativeLayout(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_bar, this)
    }

}