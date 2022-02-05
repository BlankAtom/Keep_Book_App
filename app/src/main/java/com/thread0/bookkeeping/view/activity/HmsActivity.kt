/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.activity

import android.os.Bundle
import com.thread0.bookkeeping.R
import com.thread0.bookkeeping.view.widget.CircleDot
import kotlinx.android.synthetic.main.activity_hms.*
import top.xuqingquan.base.view.activity.SimpleActivity

/**
 * TODO:Hms界面
 *      人像分割模型包
 *      文档：https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides-V5/image-segmentation-0000001050040109-V5#ZH-CN_TOPIC_0000001050990259__section1658976113112
 */
class HmsActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hms)

        // 设置点击事件
        setClickEvent()
    }

    private fun setClickEvent() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        photoImgIV.setOnClickListener {
            //TODO:点击前往选择图片，选择完成后显示在此
        }
        redCircleDot.setOnClickListener(object : CircleDot.OnClickListener {
            override fun onClick() {
                //TODO：开始运用HMSCore的抠图能力，抠出人像，并设置背景色为红色后显示到photoImgIV，处理过程需要有处理中的提示
            }
        })
        blueCircleDot.setOnClickListener(object : CircleDot.OnClickListener {
            override fun onClick() {
                //TODO：开始运用HMSCore的抠图能力，抠出人像，并设置背景色为蓝色后显示到photoImgIV，处理过程需要有处理中的提示
            }
        })
        whiteCircleDot.setOnClickListener(object : CircleDot.OnClickListener {
            override fun onClick() {
                //TODO：开始运用HMSCore的抠图能力，抠出人像，并设置背景色为白色后显示到photoImgIV，处理过程需要有处理中的提示
            }
        })
    }
}