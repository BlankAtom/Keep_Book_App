/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment:Fragment() {

    protected var mContext: Context? = null

    /**
     * @return 布局id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int
    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initData()
    }

    /**
     * 给fragment简单的初始化布局
     *
     */
    protected abstract fun initData()

    protected open fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
        mContext = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(event: String) {
        println(event)
    }
}