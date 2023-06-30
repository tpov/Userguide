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
        text: String,
        titulText: String?,
        image: Drawable?,
        video: String?,
        context: Context,
        callback: (() -> Unit)?,
        options: Options
    ) {
        val originalForeground = item.foreground ?: ColorDrawable(Color.TRANSPARENT)
        val dotDrawable = DotDrawable(originalForeground)

        item.foreground = dotDrawable

        item.setOnClickListener {
            SharedPrefManager.incrementCounterDialogView(context, item.id)
            callback?.invoke()

            MainView().showDialog( text = text,
                titulText = titulText,
                image = image,
                video = video,
                context = context
            )

            Log.d("osfefjse", "$text")
            item.foreground = originalForeground
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