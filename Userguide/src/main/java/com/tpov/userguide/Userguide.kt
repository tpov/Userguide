package com.tpov.userguide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
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

        if (
            activity != null && !activity.isFinishing
            && (getCounterValue() >= options.countKey  //Общий ключ > ключ этого гайда или ключ гайда = 0
                    || options.countKey == 0)
            && getCounterView(item.id) < options.countRepeat
        ) {
            initDot(
                item,
                text,
                titulText,
                image,
                video,
                callback,
                showOriginalView = options.countRepeat - getCounterView(item.id) == 1
            )


//            val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
//            rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    if (image != null) {
//                        if (video != null) {
//                            // Handle image and video case
//                        }
//                    }
//                }
//            })
        }
    }

    private fun initDot(
        item: View,
        text: String? = null,
        titulText: String?,
        image: Drawable?,
        video: String?,
        callback: (() -> Unit)?,
        showOriginalView: Boolean
    ) {
        DotView().showDot(item, text, titulText, image, video, context, callback, showOriginalView)

        val guideItem = GuideItem(item, text, image, video)
        guideItems.add(guideItem)
    }

    private fun getAllGuideItems(): List<GuideItem> {
        return guideItems.toList()
    }

    fun addGuideNewVersion(
        text: String,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null,
        options: Options = Options()
    ) {
        if (getCounterView(0) < options.countRepeat) {
            MainView().showDialog(
                text = text,
                titulText = titulText,
                image = image,
                video = video,
                context = context
            )
            incrementView(0)
        }
    }

    fun addNotification(
        text: String,
        key: Int = 0,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null
    ) {
        if (key == getCounterValue() && key == 0) {
            MainView().showDialog(
                text = text,
                titulText = titulText,
                image = image,
                video = video,
                context = context
            )
        }
    }

    fun showInfoFragment(text: String, fragmentManager: FragmentManager) {
        val fragment = InfoFragment.newInstance(getAllGuideItems().toList())
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    //count++
    fun setCounterValue() {
        SharedPrefManager.incrementCounter(context)
    }

    fun setCounterValue(count: Int) {
        SharedPrefManager.setCounter(context, count)
    }

    private fun getCounterValue(): Int {
        return SharedPrefManager.getCounter(context)
    }

    private fun getCounterView(idView: Int) = SharedPrefManager.getCounterView(context, idView)

    private fun setCounterView(idView: Int) = SharedPrefManager.setCounterView(context, idView)

    private fun incrementView(idView: Int) = SharedPrefManager.incrementCounterDialogView(context, idView)

}