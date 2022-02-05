/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.database.AssetsDatabase
import com.thread0.bookkeeping.entity.room.BillEntity
import com.thread0.bookkeeping.view.fragment.AccountBookFragment
import kotlinx.android.synthetic.main.activity_add_bills.*
import top.xuqingquan.base.view.activity.SimpleActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * 添加账单界面
 * TODO：1、进入界面时，根据传递进来的数据判断是新建账单还是修改账单
 *      2、需要一个可以选择账单日期的控件，新建账单时默认显示当前日期，修改账单时显示账单日期
 *      3、需要一个输入金额的editText，显示只能输入"-"（符号），"."（小数点）和数字，保留小数点后两位
 *      4、需要提供支出、收入选项让用户选择输入的金额是属于支出还是收入，根据点击进来前点击的是记支出还是记收入来判断默认选择支出还是收入模式。
 *      5、需要一个输入备注的editText，让用户设置当前账单的备注
 *      6、需要一个list展示一些较常用的支出备注以及收入备注，选择记录收入模式和选择记录支出模式下，列表展示的选项不一致，点击列表可以直接将列表展示的文字设置到editText内
 *      7、提供一个保存按钮，点击保存后才将修改/新增保存进数据库
 *      tips：支出相关备注还需要提前记录所属类型，如早餐属于餐饮类消费，打的属于出行类消费，当用户自行输入时，用户点击保存前需让用户选择所属消费类型
 */
class AddBillsActivity : SimpleActivity() {

    private lateinit var bill: BillEntity
    private lateinit var strDate: String
    private var flag:Boolean = true
    private var editor:Int = 0
    private lateinit var bills: ArrayList<BillEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_add_bills)

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        strDate = sdf.format(Date())


        var billEntity = intent.getParcelableExtra<BillEntity>("bill")
        editor = intent.getIntExtra("editor",0)
        flag = intent.getIntExtra("key", 0) == 0

        // 日期，默认为当前日期，若收到则替换
        datepicker.text = billEntity?.date?:strDate

        // 输入框，填充，默认为 “”
        editTextTextPersonName2.setText("")
        editTextTextPersonName.setText("")
        if( billEntity != null ) {
            editTextTextPersonName2.setText(billEntity.price.toString())
            editTextTextPersonName.setText(billEntity.note )
            println("price: ${billEntity.price.toString()}, note: ${billEntity.note }")
        }

        // 下拉框，已测
        val outin = resources.getStringArray(R.array.outin)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, outin
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    flag = position == 0
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        spinner.setSelection( if(flag) 0 else 1 )

        backbutton.setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.save_btn).setOnClickListener {
            var strPrice = editTextTextPersonName2.text.toString()
            if (strPrice.isEmpty()||strPrice=="-")
                strPrice = "0.00"
            val pri = strPrice.toFloat()
            val strNote = editTextTextPersonName.text.toString()
            bill = BillEntity(
                date = strDate,
                isIncome = !flag,
                price = pri,
                note = strNote
            )

            if( billEntity!=null)
                bill.id = billEntity.id
            save()
            finish()
        }



    }

    fun buttonFunc(view: View) {
        when(view.id){
            R.id.datepicker -> {
                val ca = Calendar.getInstance()
                var mYear = ca[Calendar.YEAR]
                var mMonth = ca[Calendar.MONTH]
                var mDay = ca[Calendar.DAY_OF_MONTH]

                val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        mYear = year
                        mMonth = month
                        mDay = dayOfMonth
                        val mDate = "${year}/${month + 1}/${dayOfMonth}"
                        // 将选择的日期赋值给TextView
                        datepicker.text = mDate
                        strDate = mDate
                    },
                    mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }
        }
    }

    fun save() {
        AssetsDatabase.getInstance(this).billDao().add(bill)
//        println(bill)
    }
}

