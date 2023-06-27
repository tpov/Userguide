package com.tpov.userguide

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentManager

interface UserguideLibrary {
    fun addGuide(item: View, text: String, titulText: String?, image: Drawable?, video: String?)
    fun addGuideNewVersion(key: Int, text: String, titulText: String?, image: Drawable?, video: String?)
    fun showInfoFragment(text: String, fragmentManager: FragmentManager)
    fun setCount()
    fun setCount(count: Int)
    fun getCount(): Int
}