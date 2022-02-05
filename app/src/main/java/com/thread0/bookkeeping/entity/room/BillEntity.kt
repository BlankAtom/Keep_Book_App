package com.thread0.bookkeeping.entity.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.time.LocalDateTime

@Parcelize
@Entity
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,  //主键id
    @ColumnInfo var date: String,   //账单日期
    @ColumnInfo var isIncome:Boolean,  //标志：收入还是支出
    @ColumnInfo var price:Float,    //价格
    @ColumnInfo var note:String     //备注
    ) : Parcelable
