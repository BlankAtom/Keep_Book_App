package com.thread0.bookkeeping.view.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.bankList
import com.thread0.bookkeeping.view.adapter.BankItemRecyclerAdapter
import top.xuqingquan.base.view.activity.SimpleActivity

class BankChooseActivity : SimpleActivity() {
    private lateinit var bankView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_choose)

        bankView = findViewById(R.id.bank_choose_view)
        bankView.adapter = BankItemRecyclerAdapter(bankList) {
            setResult(it.bankName)
            finish()
        }
        bankView.layoutManager = LinearLayoutManager(this)

        val back = findViewById<ImageButton>(R.id.btn_bank_choose_back)
        back.setOnClickListener{onBackPressed()}

    }
}