package com.thread0.bookkeeping.dao

import androidx.room.*
import com.thread0.bookkeeping.entity.room.BillEntity

@Dao
interface BillDao {
    @Query("select * from BillEntity")
    fun getBillList() : MutableList<BillEntity>

    @Query("select * from BillEntity order by id desc limit 0,3")
    fun getBill(): MutableList<BillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: BillEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addList(list: MutableList<BillEntity>)

    @Delete
    fun delete(entity: BillEntity)

    @Update
    fun update (entity: BillEntity)

//    @Query("select * from ")
//    fun getById(id: Int)
}