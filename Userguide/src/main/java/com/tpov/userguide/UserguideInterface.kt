package com.tpov.userguide

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.FragmentManager

internal interface UserguideInterface {
    fun addGuide(item: View, text: String, titulText: String?, image: Drawable?, video: String?, callback: () -> Unit)
    fun addGuideNewVersion(key: Int, text: String, titulText: String?, image: Drawable?, video: String?)
    fun showInfoFragment(text: String, fragmentManager: FragmentManager)
    fun setCount()
    fun setCount(count: Int)
    fun getCount(): Int
}