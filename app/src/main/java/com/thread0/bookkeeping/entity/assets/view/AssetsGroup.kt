package com.thread0.bookkeeping.entity.assets.view

class AssetsGroup(
    val groupName: String ,
    private val items: ArrayList<Pair<String, Int>> = ArrayList()
) {

    fun addItem(an: String, layout: Int) {
        items.add(Pair(an, layout))
    }

    fun getItem(position: Int): Pair<String, Int> {
        if( position == 0)
            return Pair(groupName, -1)
        return items[position]
    }

    fun getItemCount() : Int {
        return items.size + 1
    }
}