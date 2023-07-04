package com.tpov.userguide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View

class DotView {

    fun showDot(
        item: View,
        generalView: Array<out View> = emptyArray(),
        text: String? = null,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null,
        context: Context,
        callback: (() -> Unit)? = null,
        showOriginalView: Boolean,
        theme: Drawable?
    ) {

        var showDialog = true

        val viewsToSetDot = listOf(item) + generalView.toList()
        viewsToSetDot.forEachIndexed { index, view ->
            val originalForeground = view.foreground ?: ColorDrawable(Color.TRANSPARENT)
            val dotDrawable = DotDrawable(originalForeground)
            view.foreground = dotDrawable

            item.setOnClickListener {
                if (!showDialog) {
                    callback?.invoke()
                } else {
                    SharedPrefManager.incrementCounterDialogView(context, view.id)
                    callback?.invoke()
                    if (text != null) {
                        MainView().showDialog(
                            text = text,
                            titulText = titulText,
                            image = image,
                            video = video,
                            context = context,
                            theme = theme
                        )
                    }
                    Log.d("osfefjse", "$text")
                    if (showOriginalView) {
                        viewsToSetDot.forEach { v ->
                            v.foreground = originalForeground
                        }
                        showDialog = false
                    }
                }
            }
        }
    }

    fun setCounterKey(context: Context) {
        SharedPrefManager.incrementCounter(context)
    }

    fun setCounterKey(count: Int, context: Context) {
        SharedPrefManager.setCounter(context, count)
    }

    private class DotDrawable(private val foregroundDrawable: Drawable) : Drawable() {
        private val dotColor = Color.RED
        private val dotRadius = 6f

        override fun draw(canvas: Canvas) {
            foregroundDrawable.bounds = bounds
            foregroundDrawable.draw(canvas)

            val centerX = bounds.right.toFloat() - dotRadius * 3
            val centerY = bounds.bottom.toFloat() - dotRadius * 5

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = dotColor
            canvas.drawCircle(centerX, centerY, dotRadius, paint)
        }

        override fun setAlpha(alpha: Int) {
            foregroundDrawable.alpha = alpha
        }

        override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
            foregroundDrawable.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return foregroundDrawable.opacity
        }
    }
}