package com.thread0.bookkeeping.entity

import com.thread0.bookkeeping.data.AccountType

class AssetsItemEntity(
    var name: String,
    var recId: Int,
    var type: AccountType,
    var isNormal: Boolean
) {
}