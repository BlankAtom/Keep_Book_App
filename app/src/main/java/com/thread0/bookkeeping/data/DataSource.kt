package com.thread0.bookkeeping.data


import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.entity.AssetsItemEntity
import com.thread0.bookkeeping.entity.BankData


data class AssetsTable(val id: Int, val name: String, val amount: Float, val assets_type: Int, val notes: String = "", val bank_name:String = "", val is_common: Int = 0) {
    companion object {
        val TABLE_NAME = "assets"
        val ID = "_id"
        val NAME = "name"
        val AMOUNT = "amount"
        val NOTES = "notes"
        val BANK_NAME = "bank_name"
        val ASSETS_TYPE = "assets_type"
        val IS_COMMON = "is_common"
    }
}

val eventStringTitle = "name"
val eventStringAmount = "invest"
val eventStringNote = "demo"
val eventStringBankName = "bank"
val eventStringRefundDate = "date"

val assetsData = listOf<String>(
    "净资产","微信","支付宝","蚂蚁花呗","押金",
    "添加自定义资产","现金","存折","银行卡","信用卡",
    "蚂蚁借呗","网商贷","京东白条","报销","借出","借出",
    "饭卡","公交卡","购物卡","礼券","股票","基金","黄金",
)
val assetsIcons = mapOf<String, @androidx.annotation.DrawableRes Int>(
    Pair("净资产", R.mipmap.icon_assets_assets),
    Pair("微信", R.mipmap.icon_assets_wechat),
    Pair("支付宝", R.mipmap.icon_assets_alipay),
    Pair("蚂蚁花呗", R.mipmap.icon_mayihuabei),
    Pair("押金", R.mipmap.icon_yajin),
    Pair("添加自定义资产",R.mipmap.icon_zidingyi),
    Pair("现金", R.mipmap.icon_xianjin),
    Pair("存折", R.mipmap.icon_cunzhe),
    Pair("银行卡", R.mipmap.icon_yinhangka),
    Pair("信用卡", R.mipmap.icon_xinyongka),
    Pair("蚂蚁借呗", R.mipmap.icon_mayijiebei),
    Pair("网商贷", R.mipmap.icon_wangshangdai),
    Pair("京东白条", R.mipmap.icon_jingdongbaitiao),
    Pair("报销", R.mipmap.icon_baoxiao),
    Pair("借出", R.mipmap.icon_jiechu),
    Pair("饭卡", R.mipmap.icon_fanka),
    Pair("公交卡", R.mipmap.icon_gongjiaoka),
    Pair("购物卡", R.mipmap.icon_gouwuka),
    Pair("礼券", R.mipmap.icon_liquan),
    Pair("股票", R.mipmap.icon_gupiao),
    Pair("基金", R.mipmap.icon_jijin),
    Pair("黄金", R.mipmap.icon_huangjin)
)

val commonUseAssets = listOf<String>(
    "微信", "支付宝", "银行卡", "信用卡", "蚂蚁花呗", "押金", "添加自定义资产"
)
val crashAssets = listOf(
    "现金", "微信", "支付宝"
)
val financialAssets = listOf(
    "银行卡", "存折"
)
val creditAssets = listOf (
    "蚂蚁花呗", "蚂蚁借呗", "网商贷", "京东白条"
)
val creditCardAssets = listOf(
    "信用卡"
)
val creditorAssets = listOf (
    "报销", "借出", "押金"
)
val virtualAssets = listOf(
    "饭卡", "公交卡", "购物卡", "礼券"
)
val investmentAssets = listOf(
    "股票", "基金", "黄金"
)
val assetsGroupName = arrayListOf<String>(
    "常用", "现金账户", "金融账户", "信用账户", "债权账户", "虚拟账户", "投资账户"
)

fun getAssetsType(n: String) :  AccountType{
    return when(n) {
        "现金", "微信", "支付宝" -> AccountType.CRASH_ACCOUNT
        "银行卡", "存折" -> AccountType.FINANCIAL_ACCOUNT
        "蚂蚁花呗", "蚂蚁借呗", "网商贷", "京东白条" -> AccountType.CREDIT_ACCOUNT
        "信用卡" -> AccountType.CREDIT_CARD
        "报销", "借出", "押金" -> AccountType.CREDITORS_ACCOUNT
        "饭卡", "公交卡", "购物卡", "礼券" -> AccountType.VIRTUAL_ACCOUNT
        "股票", "基金", "黄金" -> AccountType.INVESTMENT_ACCOUNT

        else -> AccountType.SELF_DEFINED
    }
}

fun isInCommonUse(n: String) : Boolean {
    return commonUseAssets.indexOf(n) >= 0;
}

enum class AccountType{
    NET_WORTH,
    ALWAYS_USE,
    CRASH_ACCOUNT,
    FINANCIAL_ACCOUNT,
    CREDIT_ACCOUNT,
    CREDIT_CARD,
    CREDITORS_ACCOUNT,
    VIRTUAL_ACCOUNT,
    INVESTMENT_ACCOUNT,
    SELF_DEFINED
}

val bankNameList = arrayListOf<String>(
    "工商银行", "建设银行", "中国银行", "农业银行", "招商银行", "平安银行",
    "交通银行", "中信银行", "兴业银行", "浦发银行", "光大银行", "邮政银行",
    "其他银行",
)

val bankList = arrayListOf(
    BankData(0, R.mipmap.bank_gongshangyinhang),
    BankData(1, R.mipmap.bank_jiansheyinhang),
    BankData(2, R.mipmap.bank_zhongguoyinhang),
    BankData(3, R.mipmap.bank_nongyeyinhang),
    BankData(4, R.mipmap.bank_zhaoshangyinhang),
    BankData(5, R.mipmap.bank_pinganyinhang),
    BankData(6, R.mipmap.bank_jiaotongyinhang),
    BankData(7, R.mipmap.bank_zhongxinyinhang),
    BankData(8, R.mipmap.bank_xingyeyinhang),
    BankData(9, R.mipmap.bank_pufayinhang),
    BankData(10, R.mipmap.bank_guangdayinhang),
    BankData(11, R.mipmap.bank_youzhengyinhang),
    BankData(12,R.mipmap.bank_qitayinhangv)
)


enum class CostType(val typeName :String){
    socialNorms("人情世故"),
    dailyUse("日用百货"),
    transportation("交通出行"),
    recreation("休闲娱乐"),
    foodAndDrink("餐饮美食"),
    others("其他")
}
fun getCostType(n: String) :  CostType{
    return when(n) {
        "红包"->CostType.socialNorms
        "买菜","衣服","水果","生活用品","化妆品"->CostType.dailyUse
        "公交","地铁","打的"->CostType.transportation
        "娱乐","购物"->CostType.recreation
        "早餐","午餐","晚餐","外卖","奶茶","饮料"->CostType.foodAndDrink
        else -> CostType.others
    }
}