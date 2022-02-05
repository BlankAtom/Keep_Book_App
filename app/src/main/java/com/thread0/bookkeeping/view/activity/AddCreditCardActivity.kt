/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.data.bankNameList
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.adapter.MoneyInputFilter
import com.thread0.bookkeeping.view.layout.RepaymentNumberPicker
import org.greenrobot.eventbus.EventBus
import top.xuqingquan.base.view.activity.SimpleActivity

/**
 * TODO：1、此界面需要可以编辑金额、备注和还款日期外还需要选择所属银行（可以通过dialog或进入新界面进行选择，string文件内有常见银行列表）
 *       2、根据传入的数据分为编辑还是新增,并显示当前资产账户名称
 *       3、金额需要设置仅能填写数字、小数点和负号，金额必须为负数，保存时需提示用户
 *       4、点击保存才存编辑内容进数据库，需提示是否保存成功
 */
class AddCreditCardActivity : SimpleActivity() {
    private  var debDay: Int = 0
    private  var bankName : Int = -1
    private var state = 1

    private lateinit var amount: EditText
    private lateinit var dateText: TextView
    private lateinit var bankText: TextView
    private lateinit var noteInput: EditText
    private lateinit var titleView: TextView

    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card_assets)


        val title = intent.getStringExtra("name")
        titleView = findViewById(R.id.title_add_assets_credit_card)
        if( title != null){
            titleView.text = title
        }

        val back = findViewById<ImageButton>(R.id.btn_add_assets_credit_card_back)
        val save = findViewById<Button>(R.id.btn_add_assets_credit_card_save)
        amount = findViewById(R.id.input_amount_add_credit_card_assets)
        dateText = findViewById(R.id.btn_choose_date_add_credit_card_assets)
        noteInput = findViewById(R.id.input_note_add_credit_card_assets)
        bankText = findViewById(R.id.btn_choose_bank_add_credit_card)
        back.setOnClickListener{ onBackPressed() }
        save.setOnClickListener { save() }
        dateText.setOnClickListener {  openDate() }
        bankText.setOnClickListener { openBankChoose() }
        amount.doAfterTextChanged {
            val s = it.toString()
            if( s != "" && s != "-") {
                if( s.toFloat() > 0 ) {
                    val si = "-$s"
                    amount.setText(si)
                    amount.setSelection(amount.text.length )
                }
            }
        }
        amount.filters = arrayOf(MoneyInputFilter())
        amount.requestFocus()

        fillBlankByIntent()
    }

    private fun fillBlankByIntent() {
        var name = intent.getStringExtra("name")
        var amount = intent.getStringExtra("invest")
        var note = intent.getStringExtra("note")
        var debtDay = intent.getStringExtra("date")
        var bank = intent.getStringExtra("bank")
        val assets = intent.getParcelableExtra<AssetsEntity>("assets")
        state = intent.getIntExtra("state", 1)
        if( assets != null) {
            this.id = assets.id
            name = assets.name
            amount = assets.amount.toString()
            note = assets.notes
            debtDay = assets.date.toString()
            bankName = assets.bankName
            bank = if( bankName >= 0 ) {
                bankNameList[assets.bankName]
            } else {
                getString(R.string.add_bank)
            }
        }
        this.titleView.text = name ?: "错误的资产：0x01"
        this.amount.setText(amount ?: "")
        this.noteInput.setText(note)
        if (debtDay != null) {
            this.debDay = debtDay.toInt()
        } else {
            this.debDay = 1
        }
        dateText.text = String.format(getString(R.string.refund_date_format), debDay)
        this.bankText.text = bank ?: getString(R.string.add_bank)

    }
    private fun save() {
        val name = titleView.text.toString()
        val amount: Float = if( amount.text.toString().isEmpty()){
            "0.00".toFloat()
        } else {
            amount.text.toString().toFloat()
        }
        val demo =  noteInput.text.toString()
        EventBus.getDefault().post(
            MessageEvent().put(
                AssetsEntity(
                    id = id,
                    name = name,
                    amount= amount,
                    date = debDay,
                    notes = demo,
                    bankName = bankName
                )
            )
        )
        setResult(state)
        finish()
    }
    private fun openBankChoose() {
        val intent = Intent(this, BankChooseActivity::class.java)
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            bankName = it.resultCode
            bankText.text = bankNameList[bankName]
        }.launch(intent)
    }
    private fun openDate() {
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
    private fun presentPickerInAlert(numberPicker: NumberPicker, title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(numberPicker)
            .setNegativeButton(getString(android.R.string.cancel), null)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                debDay = numberPicker.value
                dateText.text = String.format(getString(R.string.refund_date_format), debDay)
            }
            .show()
    }
}