/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.database.AssetsDatabase
import com.thread0.bookkeeping.entity.room.BillEntity
import com.thread0.bookkeeping.view.adapter.AccountRecyclerAdapter
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import top.xuqingquan.base.view.activity.SimpleActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * 账单列表界面
 * TODO：1、账单列表通过时间顺序展示
 *       2、不同日期的账单通过在列表中添加显示日期的item进行区分以及归类，
 *          该日期item还需要显示该日期收入-支出后的结果，正数则显示"收入：xxx元"，负数则显示"支出：xxx元"
 *       3、当有本月数据时，列表顶部还需显示当月结余的金额，正负数均可
 */
class BillsDetailsActivity : SimpleActivity() {

    private lateinit var swipeView: SwipeRecyclerView
    private lateinit var swipeMenu: SwipeMenuCreator
    private lateinit var monthTotalPrice: TextView
    private lateinit var todayView: TextView
    private lateinit var zhichuView: TextView
    private lateinit var shouruView: TextView

    private var accounts: ArrayList<BillEntity> = ArrayList()
    private lateinit var mAdapter: AccountRecyclerAdapter
    private var mTotal: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills_details)
        findViewById<ImageButton>(R.id.back_btn).setOnClickListener {
            finish()
        }
        monthTotalPrice = findViewById(R.id.textView3)
        todayView = findViewById(R.id.textView4)
        zhichuView = findViewById(R.id.textView5)
        shouruView = findViewById(R.id.textView6)

        // 获取数据，计算本月结余
        accounts.addAll(AssetsDatabase.getInstance(this).billDao().getBillList())
        competeMonthTotal()

        todayView.text = SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINA).format(Date())

        mAdapter = AccountRecyclerAdapter(this, accounts)
        swipeView = findViewById(R.id.details_bills_swipe)
        swipeView.setOnItemClickListener { view, adapterPosition ->
            val intent = Intent(this, AddBillsActivity::class.java)
            intent.putExtra("bill", accounts[adapterPosition])
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                this.accounts.clear()
                this.accounts.addAll(AssetsDatabase.getInstance(this).billDao().getBillList())
                competeMonthTotal()
                mAdapter.notifyDataSetChanged(accounts)
                swipeView.adapter = mAdapter
            }.launch(intent)
//            Toast.makeText(this, "Things $adapterPosition", Toast.LENGTH_SHORT).show()
        }

        swipeView.setSwipeMenuCreator { leftMenu, rightMenu, position ->
            val deleteItem = SwipeMenuItem(this).setBackground(R.color.white)
                .setImage(R.mipmap.icon_shanchu_jianpan)
                .setText("删除")
                .setTextColor(Color.WHITE)
                .setWidth(resources.getDimensionPixelSize(R.dimen.dp_70))
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            rightMenu.addMenuItem(deleteItem)
        }

        swipeView.setOnItemMenuClickListener { menuBridge, adapterPosition ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction
            val position = menuBridge.position


            if( direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                AssetsDatabase.getInstance(this).billDao().delete(accounts[adapterPosition])
                accounts.removeAt(adapterPosition)
                competeMonthTotal()
                mAdapter.notifyDataSetChanged(accounts)
                swipeView.adapter = mAdapter
            }
        }

        mAdapter.notifyDataSetChanged(accounts)
        swipeView.adapter = mAdapter
        swipeView.layoutManager = LinearLayoutManager(this)
        swipeView.isSwipeItemMenuEnabled = true
    }

    fun competeMonthTotal() {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
        val strDate = sdf.format(Date())

        var total : Float = 0f
        var income = 0f
        var outcome = 0f
        val oneDay: Float = 30f * 24 * 60 * 60 * 1000
        for( i in accounts ){
            val s = sdf.parse(i.date)
            if (s.time >= Date().time - oneDay) {
                total += if( i.isIncome )
                    i.price
                else
                    -i.price
            }

            if( i.isIncome ){
                income += i.price
            } else {
                outcome += i.price
            }
        }
        this.mTotal = total
        this.monthTotalPrice.text = this.mTotal.toString()
        this.zhichuView.text = outcome.toString()
        this.shouruView.text = income.toString()
    }

}