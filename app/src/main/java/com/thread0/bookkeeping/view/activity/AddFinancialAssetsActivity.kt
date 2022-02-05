/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.data.bankNameList
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.adapter.MoneyInputFilter
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity

/**
 * TODO：1、此界面需要可以编辑余额和备注外还需要选择所属银行（可以通过dialog或进入新界面进行选择，string文件内有常见银行列表）
 *       2、根据传入的数据分为编辑还是新增,并显示当前资产账户名称
 *       3、金额需要设置仅能填写数字和小数点
 *       4、点击保存才存编辑内容进数据库，需提示是否保存成功
 */
class AddFinancialAssetsActivity : SimpleActivity() {

    private lateinit var titleView: TextView
    private lateinit var amountInput: EditText
    private lateinit var bankText: TextView
    private lateinit var noteInput: EditText
    private var state = 1
    private var bankName = -1
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_assets)

        titleView = findViewById(R.id.title_add_assets_financial)
        amountInput = findViewById(R.id.input_amount_add_financial_card_assets)
        bankText = findViewById(R.id.btn_choose_bank_add_financial_card)
        noteInput = findViewById(R.id.input_note_add_financial_card_assets)
        amountInput.filters = arrayOf(MoneyInputFilter())

        val save = findViewById<Button>(R.id.btn_add_financial_assets_save)
        val back = findViewById<ImageButton>(R.id.btn_add_financial_assets_back)
        save.setOnClickListener { save() }
        back.setOnClickListener { onBackPressed() }
        bankText.setOnClickListener { intoChooseBank() }
        fillBlankByIntent()
    }

    private fun intoChooseBank() {
        val intent = Intent(this, BankChooseActivity::class.java)
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            bankName = it.resultCode
            bankText.text = bankNameList[bankName]
        }.launch(intent)
    }
    private fun save() {
        val name = titleView.text.toString()
        val amount: Float = if( amountInput.text.toString().isEmpty()){
            "0.00".toFloat()
        } else {
            amountInput.text.toString().toFloat()
        }
        val demo =  noteInput.text.toString()
        EventBus.getDefault().post(
            MessageEvent().put(
                AssetsEntity(
                    id = id,
                    name = name,
                    amount= amount,
                    notes = demo,
                    bankName = bankName
                )
            )
        )

        setResult(state)
        finish()
    }
    private fun fillBlankByIntent() {
        var name = intent.getStringExtra("name")
        var amount = intent.getStringExtra("invest")
        var note = intent.getStringExtra("note")
        val assets = intent.getParcelableExtra<AssetsEntity>("assets")
        var bank = intent.getStringExtra("bank")
        state = intent.getIntExtra("state", 1)

        if( assets != null) {
            this.id = assets.id
            name = assets.name
            amount = assets.amount.toString()
            note = assets.notes
            bankName = assets.bankName
            bank = if( bankName >= 0 ) {
                bankNameList[assets.bankName]
            } else {
                getString(R.string.add_bank)
            }
        }
        if ( bank == null ) {
            bankText.text = getString(R.string.add_bank)
        } else {
            this.bankText.text = bank
        }
        this.titleView.text = name ?: "错误的资产：0x01"
        this.amountInput.setText(amount ?: "")
        this.noteInput.setText(note)
    }
}