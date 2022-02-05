/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan

fun getHintTextSize(origin:String): SpannedString{

    val ss = SpannableString(origin)
    val ass = AbsoluteSizeSpan(16,true)
    ss.setSpan(ass,0,ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return SpannedString(ss)
}