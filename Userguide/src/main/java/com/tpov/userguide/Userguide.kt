package com.tpov.userguide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.FragmentManager

class Userguide(private val context: Context) {
    private val guideItems: MutableList<GuideItem> = mutableListOf()

    fun addGuide(
        item: View,
        text: String,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null,
        callback: (() -> Unit)? = null,
        options: Options = Options()
    ) {
        val activity = item.context as? Activity
        Log.d("esoigfhiosehif", "activity != null && !activity.isFinishing: ${activity != null && !activity.isFinishing}")
        Log.d("esoigfhiosehif", "getCounterValue() ${getCounterValue()}")
        Log.d("esoigfhiosehif", "options.countKey ${options.countKey}")
        Log.d("esoigfhiosehif", "options.countRepeat ${options.countRepeat}")
        Log.d("esoigfhiosehif", "getCounterView(item.id) ${getCounterView(item.id)}")

        if (
            activity != null && !activity.isFinishing
            && getCounterValue() >= options.countKey
            && getCounterView(item.id) < options.countRepeat)
        {
            val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            initDot(item, text, titulText, image, video, callback, options)
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (image != null) {
                        if (video != null) {
                            // Handle image and video case
                        }
                    }
                }
            })
        }
    }

    private fun initDot(
        item: View,
        text: String,
        titulText: String?,
        image: Drawable?,
        video: String?,
        callback: (() -> Unit)?,
        options: Options
    ) {
        DotView().showDot(item, text, titulText, image, video, context, callback)

        Toast.makeText(context, "view dot", Toast.LENGTH_SHORT).show()
        val guideItem = GuideItem(item, text, image, video)
        guideItems.add(guideItem)
    }

    private fun getAllGuideItems(): List<GuideItem> {
        return guideItems.toList()
    }

    fun addGuideNewVersion(
        key: Int,
        text: String,
        titulText: String?,
        image: Drawable?,
        video: String?
    ) {
        MainView().showDialog(
            text = text,
            titulText = titulText,
            image = image,
            video = video,
            context = context
        )
    }

    fun showInfoFragment(text: String, fragmentManager: FragmentManager) {
        val fragment = InfoFragment.newInstance(getAllGuideItems().toList())
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    //count++
    fun setCount() {
        SharedPrefManager.incrementCounter(context)
    }

    fun setCount(count: Int) {
        SharedPrefManager.setCounter(context, count)
    }

    private fun getCounterValue(): Int {
        return SharedPrefManager.getCounter(context)
    }

    private fun getCounterView(idView: Int) = SharedPrefManager.getCounterView(context, idView)

}