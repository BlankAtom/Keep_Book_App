package com.thread0.bookkeeping.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thread0.bookkeeping.dao.AssetsDao
import com.thread0.bookkeeping.dao.BillDao
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.entity.room.BillEntity

@Database(entities = [AssetsEntity::class, BillEntity::class], version = 2, exportSchema = false)
abstract class AssetsDatabase : RoomDatabase(){
    abstract fun assetsDao(): AssetsDao
    abstract fun billDao(): BillDao
    companion object {
        private var instance: AssetsDatabase? = null
        fun getInstance(ctx: Context) : AssetsDatabase {
            if( instance == null ) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AssetsDatabase::class.java,
                    "Room.db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}