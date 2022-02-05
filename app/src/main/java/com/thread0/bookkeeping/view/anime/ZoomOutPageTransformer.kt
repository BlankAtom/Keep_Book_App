package com.thread0.bookkeeping.view.anime

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * 提供页面切换时的动态效果，缩小切换动画
 */
class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    private val minScala = 0.85f
    private val minAlpha = 0.5f
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = minScala.coerceAtLeast(1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horizonMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horizonMargin - vertMargin / 2
                    } else {
                        horizonMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (minAlpha +
                            (((scaleFactor - minScala) / (1 - minScala)) * (1 - minAlpha)))
                }
                else -> { // (1,+Infinity]
                    // 页面离开到右侧
                    alpha = 0f
                }
            }
        }
    }
}