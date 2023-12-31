package com.tpov.userguide

import android.graphics.drawable.Drawable
import android.view.View

internal data class GuideItem(
    val view: View?,
    val text: String?,
    val image: Drawable?,
    val video: String?
)