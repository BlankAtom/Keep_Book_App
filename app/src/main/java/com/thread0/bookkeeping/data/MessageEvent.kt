package com.thread0.bookkeeping.data

import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.entity.room.BillEntity

class MessageEvent() {
    private lateinit var asset: AssetsEntity
    fun put(asset: AssetsEntity) : MessageEvent {
        this.asset = asset
        return this
    }

    fun getAssets(): AssetsEntity {
        return asset
    }

}