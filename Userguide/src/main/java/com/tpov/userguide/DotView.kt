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


    fun showDot(item: View, text: String, titulText: String?, image: Drawable?, video: String?, context: Context) {
        val originalForeground = item.foreground ?: ColorDrawable(Color.TRANSPARENT)
        val dotDrawable = DotDrawable(originalForeground)

        item.foreground = dotDrawable

        val listenersList = mutableListOf<View.OnClickListener>()

        // Сохраняем существующий слушатель (если есть)
        val oldOnClickListener = item.getTag(item.id) as? View.OnClickListener
        if (oldOnClickListener != null) {
            listenersList.add(oldOnClickListener)
        }

        // Создаем новый слушатель
        val newOnClickListener = View.OnClickListener {
            // Выполняем все слушатели из списка
            for (listener in listenersList) {
                listener.onClick(item)
            }

            // Добавляем логику нового слушателя
            MainView().showDialog(text, titulText, image, video, context)
            Log.d("osfefjse", "$text")
        }

        // Добавляем новый слушатель в список и устанавливаем его на элемент
        listenersList.add(newOnClickListener)
        item.setOnClickListener(newOnClickListener)
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