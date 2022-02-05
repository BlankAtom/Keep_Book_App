/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.fragment

import android.graphics.Color
import android.text.Html
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.data.CostType
import com.thread0.bookkeeping.data.getCostType
import com.thread0.bookkeeping.entity.IncomeAndExpenditure
import kotlinx.android.synthetic.main.fragment_chart.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * 图标界面
 * TODO：1、展示用户本周/本月的：
 *          1.1、总支出
 *          1.2、每日平均支出
 *          1.3、同上周/上月比较，示例："较上周总支出上涨/下降800.00元，日均上涨/下降114.29元"，上涨时金额显示红色，下降时金额显示绿色
 *          1.4、当周/当月每日消费组成折线图进行展示
 *          1.5、当周/当月的消费类别组成饼图展示（需提前确定消费所属类别）
 *       2、需要提供切换周/月视图的按钮
 */
class ChartFragment : BaseFragment() {
    //保存数据
    private var dataObjects: List<IncomeAndExpenditure> = listOf()

    //保存折线图数据
    private var pricelist: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

    //保存饼图数据
    private var piecostlist: MutableList<IncomeAndExpenditure> = ArrayList()

    //判断周/月标志,true为周,false为月
    private var flag: Boolean = true

    //保存最高支付
    private var max_data = IncomeAndExpenditure(
        price = 0f,
        date = Date(),
        isIncome = false,
        tag = ""
    )

    //保存最高支出方向
    private var max_entry: PieEntry = PieEntry(0f, "")

    //保存上周/月总支出
    private var last_cost: Float = 0f

    //保存本月最大天数
    private var max_day: Int = 0


    override fun getLayoutId(): Int {
        return R.layout.fragment_chart
    }

    private fun instantChartItem(p: Float, d: String, i: Boolean, t: String) : IncomeAndExpenditure {
        val simple = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return IncomeAndExpenditure(price = p, date = simple.parse(d)!!, isIncome = i, tag = t)
    }
    override fun initData() {
        //重置
        dataObjects = listOf()
        pricelist = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        piecostlist = ArrayList()
        max_data = IncomeAndExpenditure(
            price = 0f,
            date = Date(),
            isIncome = false,
            tag = ""
        )
        max_entry = PieEntry(0f, "")
        last_cost = 0f

        //date统一格式
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        dataObjects = listOf(
            instantChartItem(500f,  "2021-07-01", false, "娱乐" ),
            instantChartItem(100f,  "2021-07-05" ,  false, "娱乐"  ),
            instantChartItem(150f,  "2021-07-07", false, "饮料"),
            instantChartItem(100f,  "2021-07-08", false, "地铁"),
            instantChartItem(100f,  "2021-07-09", false, "红包"),
            instantChartItem(100f,  "2021-07-10", false, "水果"),
            instantChartItem(10.31f,"2021-07-11" , false, "水果"),
            instantChartItem(10.5f, "2021-07-10", false, "奶" ),
            instantChartItem(100f,  "2021-07-11", false, "奶茶" ),
        )
        val s1: String
        val s2: String
        val s3: String
        if (flag) {
            pricelist = FloatArray(7)
            val date: Date = Date()
            //判断当前星期几
            val day: Int = when (SimpleDateFormat("EEEE", Locale.CHINA).format(date)) {
                "Monday", "星期一" -> 1
                "Tuesday", "星期二" -> 2
                "Wednesday", "星期三" -> 3
                "Thursday", "星期四" -> 4
                "Friday", "星期五" -> 5
                "Saturday", "星期六" -> 6
                "Sunday", "星期日" -> 7
                else -> 0
            }
            val calendar = Calendar.getInstance()

            calendar.time = date
            calendar.add(Calendar.DATE, 1 - day)
            val date1: Date = calendar.time//周一日期
            s1 = sdf.format(date1)

            calendar.time = date1
            calendar.add(Calendar.DATE, 6)
            val date2: Date = calendar.time//周天日期
            s2 = sdf.format(date2)

            calendar.time = date1
            calendar.add(Calendar.DATE, -7)
            val date3: Date = calendar.time//上周一日期
            s3 = sdf.format(date3)
//            Log.d("测试",s1)
//            Log.d("测试",s2)
//            Log.d("测试",s3)

        } else {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DATE, 1)
            val date1: Date = calendar.time//本月第一天
            s1 = sdf.format(date1)

            calendar.roll(Calendar.DATE, -1)
            val date2: Date = calendar.time//本月最后一天
            max_day = calendar.get(Calendar.DATE)
            s2 = sdf.format(date2)

            calendar.time = date1
            calendar.add(Calendar.MONTH, -1)
            val date3: Date = calendar.time
            s3 = sdf.format(date3)

            pricelist = FloatArray(max_day)
        }
        for (i in dataObjects.indices) {
            val data = dataObjects[i]
            if (sdf.format(data.date) >= s1 && sdf.format(data.date) <= s2 && !data.isIncome) {
                val index: Int =
                    ((data.date.time - sdf.parse(s1)!!.time) / (24 * 60 * 60 * 1000)).toInt()
                pricelist[index] += data.price
                if (max_data.price < data.price)
                    max_data = data
                piecostlist.add(data)
            }
            if (sdf.format(data.date) < s1 && sdf.format(data.date) >= s3) {
                last_cost += data.price
            }
        }

        initLineChart()
        initPieChart()
        showLineChart()
        showPieChart()
        initText()
        rb1.setOnClickListener {
            flag = true
            initData()
        }
        rb2.setOnClickListener {
            flag = false
            initData()
        }
    }


    private fun initText() {
        var day = 7
        var weekOrMonth = "周"
        if (!flag) {
            day = max_day
            weekOrMonth = "月"
        }
        val day_upper_cost: Float = last_cost / day
        var total_cost_price = 0f

        for (i in pricelist.indices)
            total_cost_price += pricelist[i]

        val format = DecimalFormat("0.##")
        format.roundingMode = RoundingMode.FLOOR

        val avg_cost_price: Float = total_cost_price / day
        val text: String =
            "较上" + weekOrMonth + "总支出上涨<font color='#FF0000'>" + (total_cost_price - last_cost).toString() +
                    "</font>元，日均上涨<font color='#FF0000'>" + format.format(avg_cost_price - day_upper_cost) + "</font>元"
        compare.text = Html.fromHtml(text)
        total_cost.text = total_cost_price.toString()
        avg_cost.text = format.format(avg_cost_price)
        val color: String = when (max_entry.label) {
            CostType.socialNorms.typeName -> "'#feb64d'"
            CostType.dailyUse.typeName -> "'#ff7c7c'"
            CostType.transportation.typeName -> "'#9287e7'"
            CostType.recreation.typeName -> "'#60acfc'"
            CostType.foodAndDrink.typeName -> "'#00FA9A'"
            else -> "'#EE82EE'"
        }
        val text2: String = "主要支出方向:" + "<font color=" + color + ">" + max_entry.label + "</font>"

        main_cost.text = Html.fromHtml(text2)
        val strMaxTag = "最高的一笔: ${max_data.tag}"
        max_text.text = strMaxTag
        max_cost.text = max_data.price.toString()
    }


    private fun initPieChart() {
        pie_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val ratio = 360 / pie_chart.data.yValueSum//比例系数,每单位值所占度数
                val index = (h?.x ?: 0f).toInt()
                val angleByIndex =
                    { i: Int -> pie_chart.data.dataSet.getEntryForIndex(i).value }//根据编号获得区域的值
                var angle = 90f//初始旋转90度,加上默认的270度正好回原点
                for (i in index downTo 0) {
                    angle -= angleByIndex(i) * ratio//旋转度数减去所选区域与前编号区域的度数
                }
                angle += angleByIndex(index) * ratio / 2//回退本区域一半度数,正好指向正下方
                pie_chart.isRotationEnabled = true
                pie_chart.rotationAngle = angle
                pie_chart.isRotationEnabled = false
                //以下为详情显示
                val des = "该类别花费了${h?.y ?: 0}元"
                pie_cost_arrow.text="^"
                pie_cost.text = des
            }

            override fun onNothingSelected() {
                pie_cost_arrow.text=""
                pie_cost.text = ""
            }
        })
        pie_chart.legend.isEnabled=false
        pie_chart.maxAngle = 360f
        pie_chart.setTouchEnabled(true)
        pie_chart.setUsePercentValues(true)
        pie_chart.setDrawEntryLabels(true)
        pie_chart.setRotationEnabled(true)

        pie_chart.centerText = "支出类别"// 圆环中心的文字，会自动适配不会被覆盖


    }

    private fun showPieChart() {
        val typeCost  = floatArrayOf(0f,0f,0f,0f,0f,0f,0f)
        val typeNames :MutableList<String> = arrayListOf(
            CostType.socialNorms.typeName,
            CostType.dailyUse.typeName,
            CostType.transportation.typeName,
            CostType.recreation.typeName,
            CostType.foodAndDrink.typeName,
            CostType.others.typeName
        )
        for (i in piecostlist.indices) {
            val d = piecostlist[i]
            val type = getCostType(d.tag)
            when (type) {
                CostType.socialNorms -> typeCost[0] += d.price
                CostType.dailyUse -> typeCost[1] += d.price
                CostType.transportation -> typeCost[2] += d.price
                CostType.recreation -> typeCost[3] += d.price
                CostType.foodAndDrink -> typeCost[4] += d.price
                else -> typeCost[5] += d.price
            }
        }
        val pieEntries = mutableListOf<PieEntry>()
        for(i in typeCost.indices){
            if(typeCost[i]!=0f)
                pieEntries.add(PieEntry(typeCost[i], typeNames[i]))
        }
        for (i in pieEntries.indices) {
            if (max_entry.value < pieEntries[i].value)
                max_entry = pieEntries[i]
//            Log.d("测试1",max_entry.value.toString()+max_entry.label)
        }
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.setColors(
            Color.parseColor("#feb64d"),
            Color.parseColor("#ff7c7c"),
            Color.parseColor("#9287e7"),
            Color.parseColor("#60acfc"),
            Color.parseColor("#00FA9A"),
            Color.parseColor("#EE82EE")
        )
        val pieData = PieData(pieDataSet)
        pie_chart.data = pieData
        pie_chart.setBackgroundColor(Color.WHITE)//整个图标的背景色
        pieData.setDrawValues(true) //设置是否显示区域百分比的值
        val description = Description()
        description.text = ""
        pie_chart.description = description
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextColor(Color.parseColor("#FFFFFF"))
        pieData.setValueTextSize(10f)
        pie_chart.invalidate()
        pie_chart.animateX(1400)
    }


    private fun initLineChart() {
        line_chart.run {
            isDoubleTapToZoomEnabled = false
            isScaleXEnabled = false
            isScaleYEnabled = false
        }
        line_chart.legend.run {
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            setDrawInside(true)
            form = Legend.LegendForm.CIRCLE
            orientation = Legend.LegendOrientation.VERTICAL
        }
        val des = line_chart.description
        des.text = "日期"
        line_chart.xAxis.run {
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            axisMinimum = 0f
            axisMaximum = 7f
            if (!flag)
                axisMaximum = max_day.toFloat()
            labelCount = 7
            textSize = 10f
            labelRotationAngle = 45f
            if (flag) {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toString()) {
                            "0.0" -> "周一"
                            "1.0" -> "周二"
                            "2.0" -> "周三"
                            "3.0" -> "周四"
                            "4.0" -> "周五"
                            "5.0" -> "周六"
                            "6.0" -> "周日"
                            else -> ""
                        }
                    }
                }
            } else {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.0f",value+1)
                    }
                }
            }

        }
        line_chart.axisLeft.run {
            setDrawGridLines(false)
            axisMinimum = 0f

            var num = 0f
            for (i in pricelist.indices)
                if (num < pricelist[i])
                    num = pricelist[i]
            axisMaximum = ((Math.floor((num / 50).toDouble())+1)*50).toFloat()
            labelCount= (Math.pow(num.toDouble(),1.0/10)*4).toInt()
//            Log.d("测试",labelCount.toString())

        }
        line_chart.axisRight.isEnabled = false
    }

    fun showLineChart() {
        val entries = mutableListOf<Entry>()
//        toast(pricelist.maxOrNull().toString())
        for (i in pricelist.indices) {
            entries.add(Entry(i.toFloat(), pricelist[i]))
        }

        val dataSet = LineDataSet(entries, "支出轨迹")
        dataSet.run {
            color = Color.RED
            setCircleColor(Color.RED)
            setDrawCircleHole(false)
            circleRadius = 2.toFloat()
            mode = LineDataSet.Mode.LINEAR
        }
        dataSet.setColors(Color.BLUE)
        val lineData = LineData(dataSet)
        line_chart.data = lineData

        line_chart.setBackgroundColor(Color.WHITE)//整个图标的背景色
        lineData.setDrawValues(false)
        line_chart.invalidate()

    }

}