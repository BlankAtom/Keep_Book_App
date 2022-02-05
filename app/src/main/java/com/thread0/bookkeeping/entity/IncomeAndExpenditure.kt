package com.thread0.bookkeeping.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
class IncomeAndExpenditure(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo var price: Float = 0.00f,
    @ColumnInfo var date:Date = Date(),
    @ColumnInfo var isIncome:Boolean = true,
    @ColumnInfo var tag:String = ""
) {
    override fun toString(): String {
        val s = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date)
        return "$tag $price $s"
    }
}