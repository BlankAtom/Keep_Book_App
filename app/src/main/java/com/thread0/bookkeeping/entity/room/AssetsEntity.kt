package com.thread0.bookkeeping.entity.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.assetsIcons
import com.thread0.bookkeeping.data.bankList
import com.thread0.bookkeeping.data.bankNameList
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
class AssetsEntity(
    @ColumnInfo var name: String = "",
    @ColumnInfo var amount: Float = 0.00f,
    @ColumnInfo var notes: String = "",
    @ColumnInfo var bankName: Int = -1,
    @ColumnInfo var date: Int = 1,
    @ColumnInfo var assetsType: Int = -1,
    @ColumnInfo var isCommon: Boolean = false,
    @Ignore var visible: Boolean = true,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable  {

//    constructor() : this("", 0.00f, "", -1, -1, )
    fun getIcon(): Int {
        if( bankName >= 0 ){
            return bankList[bankName].bankIcon
        }
        return assetsIcons[name] ?: R.mipmap.icon_zidingyi
    }

    fun getTitleName(): String {
        if( bankName >= 0 ) {
            return "${bankNameList[bankName]}-$name"
        }
        return name
    }
}