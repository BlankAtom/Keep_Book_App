/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.xuqingquan.utils.*

class GuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO：引导界面，暂无内容，同学可自行安排本界面逻辑

        startActivity<MainActivity>()
        finish()
    }
}
