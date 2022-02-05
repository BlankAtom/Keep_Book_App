/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.adapter.MoneyInputFilter
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity

/**
 * TODO:1、此界面仅需编辑余额和备注
 *      2、根据传入的数据分为编辑还是新增,并显示当前资产账户名称
 *      3、金额需要设置仅能填写数字和小数点
 *      4、点击保存按钮才存编辑内容进数据库，需提示是否保存成功
 */
class AddAssetsDetailsActivity : SimpleActivity() {

    private lateinit var titleView: TextView
    private lateinit var investInput: EditText
    private lateinit var demoInput: EditText
    private var state = 1
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_assets_details)
        titleView = findViewById(R.id.title_add_assets_detail)
        investInput = findViewById(R.id.input_add_assets_invest)
        demoInput = findViewById(R.id.input_add_assets_demo)

        fillBlankByIntent()

        // 返回键，点击后返回上一个活动，调用onBackPressed方法
        val backBtn = findViewById<ImageButton>(R.id.btn_add_assets_detail_back)
        val saveBtn = findViewById<Button>(R.id.btn_add_assets_save)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        // 保存键，点击后保存数据并结束活动
        saveBtn.setOnClickListener {
            save()
        }
        investInput.filters = arrayOf(MoneyInputFilter())

    }

    /**
     * 读取标题文字、金额、备注内容，保存为Message并通过Eventbus 发送
     */
    fun save() {
        val name = titleView.text.toString()
        val invest: Float = if( investInput.text.toString().isEmpty()){
            "0.00".toFloat()
        } else {
            investInput.text.toString().toFloat()
        }
        val demo =  demoInput.text.toString()
        EventBus.getDefault().post(
            MessageEvent().put(
                AssetsEntity(
                    id = id,
                    name = name,
                    amount= invest,
                    notes = demo,
                )
            )
        )
        setResult(state)
        finish()
    }

    /**
     * 从intent获取数据并填充
     * 没有数据的情况下使用默认数据
     */
    private fun fillBlankByIntent() {
        var name = intent.getStringExtra("name")
        var amount = intent.getStringExtra("invest")
        var note = intent.getStringExtra("note")
        this.state = intent.getIntExtra("state", 1)

        val assets = intent.getParcelableExtra<AssetsEntity>("assets")

        if( assets != null) {
            id = assets.id
            name = assets.name
            amount = assets.amount.toString()
            note = assets.notes
        }
        this.titleView.text = name ?: "错误的资产：0x01"
        this.investInput.setText(amount ?: "")
        this.demoInput.setText(note ?: "")
    }

}

