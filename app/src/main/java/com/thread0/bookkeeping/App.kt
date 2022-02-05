/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping

import android.app.Application
import top.xuqingquan.app.ScaffoldConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ScaffoldConfig.getInstance(this).debug(true)
    }
}