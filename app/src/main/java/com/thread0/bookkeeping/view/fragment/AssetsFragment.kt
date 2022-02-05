/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.AccountType
import com.thread0.bookkeeping.data.MessageEvent
import com.thread0.bookkeeping.data.getAssetsType
import com.thread0.bookkeeping.database.AssetsDatabase
import com.thread0.bookkeeping.entity.room.AssetsEntity
import com.thread0.bookkeeping.view.activity.*
import com.thread0.bookkeeping.view.adapter.AssetsRecyclerAdapter
import org.greenrobot.eventbus.Subscribe

/**
 * 资产界面
 *
 *      TODO:资产分类
 *          1、现金账户
 *              1.1、现金
 *              1.2、微信
 *              1.3、支付宝
 *          2、金融账户
 *              2.1、银行卡
 *              2.2、存折
 *          3、信用账户
 *              3.1、信用卡
 *              3.2、蚂蚁花呗
 *              3.3、蚂蚁借呗
 *              3.4、网商贷
 *              3.5、京东白条
 *          4、债权账户
 *              4.1、报销
 *              4.2、借出
 *              4.3、押金
 *          5、虚拟账户
 *              5.1、饭卡
 *              5.2、公交卡
 *              5.3、购物卡
 *              5.4、礼券
 *          6、投资账户
 *              6.1、股票
 *              6.2、基金
 *              6.3、黄金
 *
 * TODO:1、本界面需显示资产列表，同种资产分类的放置与一块，不同资产分类间需有间隔做区分，列表顶部显示净资产
 *      2、资产列表需要可以由用户添加，所以此界面需要一个AddAssetsActivity的入口
 *      3、点击列表的item根据当前item的资产分类，进入不同的界面
 *          现金账户、债券账户、虚拟账户、投资账户，进入AddAssetsDetailsActivity界面
 *          金融账户，进入AddFinancialAssetsActivity界面
 *          信用账户，信用卡进入AddCreditCardActivity界面、其他信用账户进入AddCreditActivity界面，此分类下仅支持添加负债
 *      4、列表需要一个按钮来控制各项资产的显示与隐藏（当净资产隐藏时，才显示各项资产的显示与隐藏按钮）
 *      5、此界面当资产列表为空时，需添加默认资产列表
 *          现金、支付宝、微信、银行卡、蚂蚁花呗、信用卡、押金
 *      6、当数据库内的数据发生变化时，需及时更新此界面
 *      6、需统合当前界面以及AddAssetsActivity、AddAssetsDetailsActivity、AddFinancialAssetsActivity、AddCreditCardActivity、AddCreditActivity界面提示的需求来设计数据库表
 *      tips：res文件夹内有一些icon可以使用，也可自行添加其他icon，最后项目完成时记得删除所有没用到的图片
 */
class AssetsFragment : BaseFragment() {

    private lateinit var assets: ArrayList<AssetsEntity>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewFinancial: RecyclerView
    private lateinit var recyclerViewCredit: RecyclerView
    private lateinit var recyclerViewCreditor: RecyclerView
    private lateinit var recyclerViewVirtual: RecyclerView
    private lateinit var recyclerViewInvestment: RecyclerView
    private lateinit var editIntent: Intent
    private lateinit var netWorthView: TextView
    private var netWorthAmount: Float = 0.00f
    private var visibleAll = true
    private val startActivityForEditAssets: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val blindBtn = view.findViewById<ImageView>(R.id.btn_assets_blind)
        val imgBtn = view.findViewById<ImageButton>(R.id.btn_add_assets)
        val addBtn = view.findViewById<RelativeLayout>(R.id.add_assets_btn_layout)
        netWorthView = view.findViewById(R.id.assets_net_worth_amount)
        recyclerView = view.findViewById(R.id.assets_rec_view_crash)
        recyclerViewFinancial = view.findViewById(R.id.assets_rec_view_financial)
        recyclerViewCredit = view.findViewById(R.id.assets_rec_view_credit)
        recyclerViewCreditor = view.findViewById(R.id.assets_rec_view_creditor)
        recyclerViewVirtual = view.findViewById(R.id.assets_rec_view_virtual)
        recyclerViewInvestment = view.findViewById(R.id.assets_rec_view_investment)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerViewFinancial.layoutManager = LinearLayoutManager(mContext)
        recyclerViewInvestment.layoutManager = LinearLayoutManager(mContext)
        recyclerViewVirtual.layoutManager = LinearLayoutManager(mContext)
        recyclerViewCreditor.layoutManager = LinearLayoutManager(mContext)
        recyclerViewCredit.layoutManager = LinearLayoutManager(mContext)

        imgBtn.setOnClickListener {
            gotoAddAssets()
        }

        blindBtn.setOnClickListener {
            if( visibleAll ) {
                blindBtn.setImageResource(R.mipmap.icon_assets_closed)
                netWorthView.text = "****"
                netWorthView.setTextColor(Color.BLACK)
            }
            else {
                blindBtn.setImageResource(R.mipmap.icon_assets_blind)
                netWorthView.text = "$netWorthAmount"
            }
            visibleAll = !visibleAll
            for (i in assets) {
                i.visible = visibleAll
            }
            setAdapter()
        }
        addBtn.setOnClickListener {
            gotoAddAssets()
        }
//        imgBtn.
        refresh()
        setAdapter()
    }

    private fun setAdapter() {
        val crash = assets.filter {
            getAssetsType(it.name) == AccountType.CRASH_ACCOUNT
        }
        val financial = assets.filter {
            getAssetsType(it.name) == AccountType.FINANCIAL_ACCOUNT
        }
        val credit = assets.filter {
            getAssetsType(it.name) == AccountType.CREDIT_ACCOUNT
        }
        val creditor = assets.filter {
            getAssetsType(it.name) == AccountType.CREDITORS_ACCOUNT
        }
        val virtual = assets.filter {
            getAssetsType(it.name) == AccountType.VIRTUAL_ACCOUNT
        }
        val investment = assets.filter {
            getAssetsType(it.name) == AccountType.INVESTMENT_ACCOUNT
        }
        recyclerView.adapter = AssetsRecyclerAdapter(crash) {
            assetsClicker(it)
        }
        recyclerViewFinancial.adapter = AssetsRecyclerAdapter(financial) {
            assetsClicker(it)
        }
        recyclerViewCredit.adapter = AssetsRecyclerAdapter(credit) {
            assetsClicker(it)
        }
        recyclerViewCreditor.adapter = AssetsRecyclerAdapter(creditor) {
            assetsClicker(it)
        }
        recyclerViewVirtual.adapter = AssetsRecyclerAdapter(virtual) {
            assetsClicker(it)
        }
        recyclerViewInvestment.adapter = AssetsRecyclerAdapter(investment) {
            assetsClicker(it)
        }
    }

    private fun assetsClicker(it: AssetsEntity) {
        this.editIntent = instanceIntent(it.name)
        editIntent.putExtra("assets", it)
        startActivityForEditAssets.launch(editIntent)
    }
    private fun instanceIntent(name: String) : Intent {
        return when (getAssetsType(name)) {
            AccountType.NET_WORTH, AccountType.ALWAYS_USE,
            AccountType.CRASH_ACCOUNT, AccountType.VIRTUAL_ACCOUNT,
            AccountType.INVESTMENT_ACCOUNT, AccountType.CREDITORS_ACCOUNT -> {
                Intent(mContext, AddAssetsDetailsActivity::class.java)
            }
            AccountType.CREDIT_ACCOUNT -> {
                Intent(mContext, AddCreditActivity::class.java)
            }
            AccountType.CREDIT_CARD -> {
                Intent(mContext, AddCreditCardActivity::class.java)
            }
            AccountType.FINANCIAL_ACCOUNT -> {
                Intent(mContext, AddFinancialAssetsActivity::class.java)
            }
            else -> Intent(mContext, AddSelfDefinedAccountActivity::class.java)
        }
    }

    override fun getLayoutId() = R.layout.fragment_assets

    private fun gotoAddAssets() {
        val intent = Intent(mContext, AddAssetsActivity::class.java)
        startActivity(intent)
    }
    private fun refresh() {
        val get = AssetsDatabase.getInstance(mContext!!).assetsDao().get()
        assets.clear()
        assets.addAll(get)
        var amount = 0f
        for (v in assets) {
            amount += v.amount
        }

        netWorthAmount = amount
        netWorthView.text = netWorthAmount.toString()
        if(netWorthAmount < 0) {
            netWorthView.setTextColor(Color.RED)
        } else {
            netWorthView.setTextColor(Color.BLACK)
        }
    }

    override fun initData() {
        assets = ArrayList()
    }
    @Subscribe
    fun handleEvent(event: MessageEvent) {
        val data = event.getAssets()
        var flag = true
        for( v in assets){
            if( data.id == v.id ) {
                AssetsDatabase.getInstance(mContext!!).assetsDao().update(data)
                flag = false
                break
            }
        }
        if( flag )
            insertAssets(data)
        refresh()
        setAdapter()
    }

    private fun insertAssets(it: AssetsEntity) {
        AssetsDatabase.getInstance(mContext!!).assetsDao().add(it)
    }
}