package com.tpov.userguide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

class DotView {

    fun showDot(item: View, text: String, titulText: String?, image: Drawable?, video: String?, context: Context) {
        Log.d("showDot", "showDot() called with text: $text")

        val originalForeground = item.foreground ?: ColorDrawable(Color.TRANSPARENT)
        val dotDrawable = DotDrawable(originalForeground)

        item.foreground = dotDrawable

        val listenersList = mutableListOf<View.OnClickListener>()

        val oldOnClickListener = item.getTag(item.id) as? View.OnClickListener
        if (oldOnClickListener != null) {
            listenersList.add(oldOnClickListener)
        }

        val newOnClickListener = View.OnClickListener {
            MainView().showDialog(text, titulText, image, video, context)
            Log.d("showDot", "New onClickListener executed with text: $text")
        }

        listenersList.add(newOnClickListener)

        val combinedOnClickListener = CombinedOnClickListener(listenersList)
        item.setOnClickListener(combinedOnClickListener)

        val viewTreeObserver = item.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to avoid duplicate calls
                item.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Add the first listener if it exists
                if (oldOnClickListener != null) {
                    item.setOnClickListener(oldOnClickListener)
                }
            }
        })
    }

    class CombinedOnClickListener(
        private val listeners: List<View.OnClickListener>
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            for (listener in listeners) {
                listener.onClick(view)
            }
        }
    }

    private class DotDrawable(private val foregroundDrawable: Drawable) : Drawable() {
        private val dotColor = Color.RED
        private val dotRadius = 6f // Размер точки в пикселях

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