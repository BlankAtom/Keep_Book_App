/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.database.AssetsDatabase
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.entity.room.BillEntity
import com.thread0.bookkeeping.view.activity.AddBillsActivity
import com.thread0.bookkeeping.view.activity.BillsDetailsActivity
import com.thread0.bookkeeping.view.adapter.AccountAdapter
import kotlinx.android.synthetic.main.fragment_account_book.*
import java.text.SimpleDateFormat

/**
 * 月支出总额、月收入总额以及账单数据体现界面
 * TODO:1、显示本月支出总额，本月收入总额
 *      2、提供两个按钮，点击统一进入AddBillsActivity界面新增支出、收入记录
 *      3、需要一个账单列表展示最近三条支出或收入记录，单条点击后进入AddBillsActivity编辑该记录
 *      4、列表处需有入口可以进入BillsDetailsActivity界面
 *      5、当支出总额、收入总额以及账单列表发生变化时，需要更新界面的显示
 *      6、数据库表需结合当前界面、ChartFragment界面、BillsDetailsActivity界面和AddBillsActivity界面，的需求进行设计
 */
class AccountBookFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_account_book
    lateinit var bookview :ListView
    private var dataTop3List: ArrayList<BillEntity> = ArrayList()
    private lateinit var mAdapter: AccountAdapter
    private val startActivityForEdit: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            getBills()
            mAdapter.setOnDataChanged(dataTop3List)
            bookview.adapter = mAdapter
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBills()


        bookview = view.findViewById(R.id.account_book_recycleview)
        mAdapter = AccountAdapter(mContext!!, dataTop3List) {
            val intent = Intent(activity, AddBillsActivity::class.java)
            intent.putExtra("bill", it)
            startActivityForEdit.launch(intent)
        }
        bookview.adapter = mAdapter
        val layout = view.findViewById<LinearLayout>(R.id.billdetail)
        layout.setOnClickListener {
            val intent = Intent(activity, BillsDetailsActivity::class.java)
            startActivityForEdit.launch(intent)
        }

        this_month_btn1.setOnClickListener {
            val intent = Intent(activity, AddBillsActivity::class.java)
            intent.putExtra("key",0)
//            startActivity(intent)
            startActivityForEdit.launch(intent)
        }
        this_month_btn2.setOnClickListener {
            val intent = Intent(activity, AddBillsActivity::class.java)
            intent.putExtra("key",1)
//            startActivity(intent)
            startActivityForEdit.launch(intent)
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initData() {


    }
    private fun getBills(){
        val bill = AssetsDatabase.getInstance(mContext!!).billDao().getBill()
        this.dataTop3List.clear()
        this.dataTop3List.addAll(bill)
//        println(bill.toString())
    }

}