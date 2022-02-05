/*
 * @Copyright: 2020-2021 www.thread0.com Inc. All rights reserved.
 */
package com.thread0.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thread0.bookkeeping.view.anime.ZoomOutPageTransformer
import com.thread0.bookkeeping.view.fragment.AccountBookFragment
import com.thread0.bookkeeping.view.fragment.AssetsFragment
import com.thread0.bookkeeping.view.fragment.ChartFragment
import top.xuqingquan.base.view.activity.SimpleActivity



const val NUM_PAGES = 3

/**
 * TODO:1、根据当前布局按顺序加载AccountBookFragment、AssetsFragment,ChartFragment
 *      2、左右滑动可切换fragment
 *      3、增加底部栏的响应
 */
class MainActivity : SimpleActivity() {
    /**
     * Fragment列表，会在 onCreate() 中得到初始化
     */
    val fragList = ArrayList<Fragment>()

    /**
     * viewpager2的本地变量
     * 在 onCreate#onCreate() 中初始化
     */
    private lateinit var viewPager: ViewPager2

    /**
     * 底部导航的本地变量
     * 在 onCreate#onCreate() 中初始化
     */
    private lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定到视图
        setContentView(R.layout.activity_main)
        // 从视图获取到ViewPager
        viewPager = findViewById(R.id.view_pager2)
        // 从视图获取到底部导航
        navigation = findViewById(R.id.bnve)

        // 向frag列表添加三个主要页面
        fragList.add(AccountBookFragment())
        val assetsFragment = AssetsFragment()
        fragList.add(assetsFragment)
        fragList.add(ChartFragment())

        // 将自定义的ViewPager适配器实例添加到ViewPager中
        viewPager.adapter = MyAdapter(this)
        with(viewPager) {
            // 这里是为了添加淡入淡出效果
            setPageTransformer(ZoomOutPageTransformer())
            // 页面切换回调，其实可以写得更高明一点，有时间再改吧。
            // 将页面滑动结果传给底部导航，不然会出现滑动成功但是底部不变的情况
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    when(position){
                        0 -> navigation.selectedItemId = R.id.account_book
                        1 -> navigation.selectedItemId = R.id.assets
                        2 -> navigation.selectedItemId = R.id.borrowing
                    }
                }
            })
        }

        // 底部导航点击效果
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.account_book -> {
                    viewPager.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.assets -> {
                    viewPager.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.borrowing -> {
                    viewPager.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    /**
     * 实现的结果是如果是第一页，则调用超类的返回函数，呈现的是从软件退出到桌面
     * 如果不是第一页，则返回前一页
     */
    override fun onBackPressed() {
        if( viewPager.currentItem == 0 ) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem-1
        }
    }

    /**
     * 内部适配器，主要是实现Fragment页面状态的适配
     */
    inner class MyAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment {
            return fragList[position]
        }

    }
}