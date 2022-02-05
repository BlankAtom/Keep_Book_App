/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.adapter.MoneyInputFilter
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity


/**
 * TODO：1、顶部title显示自定义
 *       2、编辑名称（保存后是账户名称）
 *       3、编辑金额，仅能填写数字和小数点
 *       4、编辑备注
 *       5、点击保存才存编辑内容进数据库，需提示是否保存成功
 */
class AddSelfDefinedAccountActivity : SimpleActivity() {

    private lateinit var nameInput: EditText
    private lateinit var investInput: EditText
    private lateinit var demoInput: EditText

    private var id = 0
    private var state = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_assets_user_defined)

        nameInput = findViewById(R.id.input_name_add_assets_user_defined)
        investInput = findViewById(R.id.input_invest_add_assets_user_defined)
        demoInput = findViewById(R.id.input_demo_add_assets_user_defined)
        investInput.filters = arrayOf(MoneyInputFilter())

        val btnBack = findViewById<ImageButton>(R.id.btn_add_assets_user_defined_back)
        val btnSave = findViewById<Button>(R.id.btn_add_assets_user_defined_save)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnSave.setOnClickListener {
            saved()
            setResult(state)
            finish()
        }
        fillBlankByIntent()
        nameInput.requestFocus()
    }

    private fun fillBlankByIntent() {
        var name = intent.getStringExtra("name")
        var amount = intent.getStringExtra("invest")
        var note = intent.getStringExtra("note")
        val assets = intent.getParcelableExtra<AssetsEntity>("assets")
        this.state = intent.getIntExtra("state", 1)
        if( assets != null) {
            this.id = assets.id
            name = assets.name
            amount = assets.amount.toString()
            note = assets.notes
        }
        if( name == "添加自定义资产") {
            name = ""
        }
        this.nameInput.setText(name)
        this.investInput.setText(amount ?: "")
        this.demoInput.setText(note)
    }

    /**
     * 读取标题文字、金额、备注内容，保存为Message并通过Eventbus 发送
     * 测试通过
     */
    fun saved() {
        var name = nameInput.text.toString()
        if( name == "" ) {
            name = "请输入名称"
        }
        val investStr = investInput.text.toString()
        var invest = 0.00f
        if(investStr.isNotEmpty()) {
            invest = investStr.toFloat()
        }

        val demo =  demoInput.text.toString()
        EventBus.getDefault().post(
            MessageEvent().put(
                AssetsEntity(
                    id = id,
                    name = name,
                    notes = demo,
                    amount = invest
                )
            )
        )
    }
}
