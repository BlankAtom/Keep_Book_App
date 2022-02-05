/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.adapter.MoneyInputFilter
import com.thread0.bookkeeping.view.layout.RepaymentNumberPicker
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity


/**
 * TODO：1、此界面需要可以编辑金额、备注和还款日期
 *       2、根据传入的数据分为编辑还是新增,并显示当前资产账户名称
 *       3、金额需要设置仅能填写数字、小数点和负号，金额必须为负数，保存时需提示用户
 *       4、点击保存才存编辑内容进数据库，需提示是否保存成功
 */
class AddCreditActivity : SimpleActivity() {

    private lateinit var titleView: TextView
    private lateinit var amountInput: EditText
    private lateinit var dateInput: TextView
    private lateinit var noteInput: EditText
    private var debDay = 0
    private var state = 1
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_assets)
        titleView = findViewById(R.id.title_add_assets_credit)
        amountInput = findViewById(R.id.input_add_credit_assets_invest)
        noteInput = findViewById(R.id.input_note_add_credit_assets)

        val title = intent.getStringExtra("name")
        Log.i("AddCreditActivity", title?:"null")
        if( title != null){
            titleView.text = title
        }


        dateInput = findViewById(R.id.btn_choose_date_add_credit_assets)
        dateInput.setOnClickListener {
            val numberPicker = RepaymentNumberPicker(
                context = this,
                separatorColor = ContextCompat.getColor(this, R.color.colorAccent),
                textColor = ContextCompat.getColor(this, R.color.colorPrimary),
                textSize = resources.getDimensionPixelSize(R.dimen.numberpicker_textsize),
                textStyle = Typeface.BOLD_ITALIC,
                defaultValue = 10,
                minValue = 1,
                maxValue = 31,
//                fontName = "Hand.ttf",
                formatter = NumberPicker.Formatter {
                    return@Formatter "每月 $it 号"
                }
            )
            presentPickerInAlert(numberPicker, getString(R.string.alert_custom_title))

        }

        fillBlankByIntent()


        val btnSave = findViewById<Button>(R.id.btn_add_credit_assets_save)
        btnSave.setOnClickListener { saved() }

        val btnBack = findViewById<ImageButton>(R.id.btn_add_credit_assets_back)
        btnBack.setOnClickListener { onBackPressed() }

        amountInput.doAfterTextChanged {
            val s = it.toString()
            if( s != "" && s != "-") {
                if( s.toFloat() > 0 ) {
                    val si = "-$s"
                    amountInput.setText(si)
                    amountInput.setSelection(amountInput.text.length )
                }
            }
        }
        amountInput.filters = arrayOf(MoneyInputFilter())
        amountInput.requestFocus()
    }

    private fun saved() {
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
                    id = id,name = name, amount= amount, date = debDay, notes = demo)
            )
        )

        setResult(state)
        finish()
    }
    private fun presentPickerInAlert(numberPicker: NumberPicker, title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(numberPicker)
            .setNegativeButton(getString(android.R.string.cancel), null)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                debDay = numberPicker.value
                dateInput.text = String.format(getString(R.string.refund_date_format), debDay)
            }
            .show()
    }
    private fun fillBlankByIntent() {
        var name = intent.getStringExtra("name")
        var amount = intent.getStringExtra("invest")
        var note = intent.getStringExtra("note")
        var debtDay = intent.getStringExtra("date")
        val assets = intent.getParcelableExtra<AssetsEntity>("assets")
        this.state = intent.getIntExtra("state", 1)
        if( assets != null) {
            this.id = assets.id
            name = assets.name
            amount = assets.amount.toString()
            note = assets.notes
            debtDay = assets.date.toString()
        }
        this.titleView.text = name ?: "错误的资产：0x01"
        this.amountInput.setText(amount ?: "")
        this.noteInput.setText(note)
        if (debtDay != null) {
            this.debDay = debtDay.toInt()
        } else {
            this.debDay = 1
        }
        dateInput.text = String.format(getString(R.string.refund_date_format), debDay)
//        this.demoInput.setText(note ?: "")
    }
}