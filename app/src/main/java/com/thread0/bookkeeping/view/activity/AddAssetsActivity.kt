/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.*
import com.thread0.bookkeeping.view.adapter.AssetsGroupAdapter
import top.xuqingquan.base.view.activity.SimpleActivity


/**
 * TODO:1、展示AssetsFragment界面内的资产分类列表，以供用户选择添加的资产账户，一级列表（如：现金账户）需要展示，但是无点击效果，二级列表（如：支付宝）点击进入对应界面
 *          现金账户、债券账户、虚拟账户、投资账户，进入AddAssetsDetailsActivity界面
 *          金融账户，进入AddFinancialAssetsActivity界面
 *          信用账户，信用卡进入AddCreditCardActivity界面、其他信用账户进入AddCreditActivity界面，此分类下仅支持添加负债
 *      2、需要增加自定义按钮，进入AddSelfDefinedAccountActivity界面
 *      3、进入对应界面后，保存成功后返回AssetsFragment界面，保存失败则返回本界面。
 */
class AddAssetsActivity : SimpleActivity() {

    /**
     * 全部资产类型列表视图
     */
    private lateinit var assetsListView: ListView

    /**
     * 常用资产列表视图
     */
    private lateinit var assetsCommonListView: ListView

    /**
     * 全部资产列表
     */
    private val assetsList: ArrayList<String> = ArrayList()

    /**
     * 全部资产列表标签
     */
    private val assetsListTag: ArrayList<String> = ArrayList()

    /**
     * 常用资产列表
     */
    private val commonList = ArrayList<String>()

    /**
     * 常用资产列表标签
     */
    private val commonTag = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_assets)

        // Init View
        assetsCommonListView = findViewById(R.id.add_assets_common_list_view)
        assetsListView = findViewById(R.id.add_assets_list_view)
        val btnBack = findViewById<ImageButton>(R.id.btn_add_assets_back)
        val btnAdd = findViewById<Button>(R.id.btn_add_assets)
        val search = findViewById<EditText>(R.id.search_view)

        // Init Data
        setData()

        // Init ListView Adapter
        val adapter = AssetsGroupAdapter(this, assetsList, assetsListTag) {
            clickItem(it ?: "添加自定义资产")
        }
        val commonAdapter = AssetsGroupAdapter(this, commonList, commonTag) {
            clickItem(it ?: "添加自定义资产")
        }
        assetsListView.adapter = adapter
        assetsCommonListView.adapter = commonAdapter

        setListViewHeight(assetsListView)
        setListViewHeight(assetsCommonListView)
        // 获取“添加自定义资产”按钮，并为其添加点击响应
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddSelfDefinedAccountActivity::class.java)
            openIntent(intent, commonUseAssets[commonUseAssets.size - 1])
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Set SearchView's Query
//        search.
        search.doAfterTextChanged {
            if( it.toString().isEmpty() ){
//                    commonAdapter.filter.filter("null")
                setListViewHeight(assetsCommonListView)
            } else {
                assetsCommonListView.layoutParams.height = 0
            }
            adapter.filter.filter(it.toString())
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {  //判断得到的焦点控件是否包含EditText
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            //得到输入框在屏幕中上下左右的位置
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略
        return false
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        if (me.action == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            val v = currentFocus //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v!!.windowToken) //收起键盘
            }
        }
        return super.dispatchTouchEvent(me)
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
    fun setListViewHeight(view: ListView) {
        var total = 0
        val adapter = view.adapter
        if( adapter != null ) {
            for (i in 0 until adapter.count) {
                val item = adapter.getView(i, null, view )
                if( item != null ) {
                    item.measure(0, 0)
                    total += item.measuredHeight
                }
            }
            val params = view.layoutParams
            params.height = total + (view.dividerHeight * (view.count - 1))
            view.layoutParams = params
        }
    }
    private fun setData() {
        // 从DataSource添加资产
        // 现金
        assetsList.add(assetsGroupName[1])
        assetsList.addAll(crashAssets)
        // 金融
        assetsList.add(assetsGroupName[2])
        assetsList.addAll(financialAssets)
        // Credit
        assetsList.add(assetsGroupName[3])
        assetsList.addAll(creditCardAssets)
        assetsList.addAll(creditAssets)
        // Creditor
        assetsList.add(assetsGroupName[4])
        assetsList.addAll(creditorAssets)
        // Virtual
        assetsList.add(assetsGroupName[5])
        assetsList.addAll(virtualAssets)
        // Investment
        assetsList.add(assetsGroupName[6])
        assetsList.addAll(investmentAssets)

        // Tag
        assetsListTag.addAll( assetsGroupName.subList(1, assetsGroupName.size))

        // Common
        commonTag.add(assetsGroupName[0])
        commonList.add(assetsGroupName[0])
        commonList.addAll(commonUseAssets)


    }

    private fun clickItem(s: String) {
        when (getAssetsType(s)){
            AccountType.NET_WORTH,AccountType.ALWAYS_USE,AccountType.CRASH_ACCOUNT,
            AccountType.VIRTUAL_ACCOUNT, AccountType.INVESTMENT_ACCOUNT, AccountType.CREDITORS_ACCOUNT ->  {
                if( s == commonUseAssets[commonUseAssets.size - 1]){
                    val intent = Intent(this, AddSelfDefinedAccountActivity::class.java)
                    openIntent(intent, s)
                }else {
                    val intent = Intent(this, AddAssetsDetailsActivity::class.java)
                    openIntent(intent, s)
                }
            }
            AccountType.FINANCIAL_ACCOUNT -> {
                val intent = Intent(this, AddFinancialAssetsActivity::class.java)
                openIntent(intent, s)
            }
            AccountType.CREDIT_ACCOUNT -> {
                val intent = Intent(this, AddCreditActivity::class.java)
                openIntent(intent, s)
            }
            AccountType.CREDIT_CARD -> {
                val intent = Intent(this, AddCreditCardActivity::class.java)
                openIntent(intent, s)
            }
            else -> {
                val intent = Intent( this, AddSelfDefinedAccountActivity::class.java)
                openIntent(intent, s)
            }
        }
    }

    private fun openIntent(intent: Intent, title: String) {
        intent.putExtra("name", title)
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if( it.resultCode > 0)
                finish()
        }.launch(intent)

    }

}
