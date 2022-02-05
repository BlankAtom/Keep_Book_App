package com.thread0.bookkeeping.dao

import androidx.room.*
import com.thread0.bookkeeping.entity.room.AssetsEntity

@Dao
interface AssetsDao {
    @Query("select * from AssetsEntity")
    fun get() : MutableList<AssetsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: AssetsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addList(list: MutableList<AssetsEntity>)

    @Delete
    fun delete(entity: AssetsEntity)

    @Update
    fun update (entity: AssetsEntity)

//    @Query("select * from ")
//    fun getById(id: Int)
}