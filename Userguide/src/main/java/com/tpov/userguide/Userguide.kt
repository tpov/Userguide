package com.tpov.userguide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.FragmentManager

class Userguide(private val context: Context) : UserguideInterface {
    private val guideItems: MutableList<GuideItem> = mutableListOf()

    override fun addGuide(item: View, text: String, titulText: String?, image: Drawable?, video: String?) {
        val activity = item.context as? Activity
        if (activity != null && !activity.isFinishing) {

            val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            initDot(item, text, titulText, image, video)
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (image != null) {
                        if (video != null) {

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
        video: String?
    ) {
        DotView().showDot(item, text, titulText, image, video, context)

        SharedPrefManager.decrementCounterView(context, item.id)
        Toast.makeText(context, "view dot", Toast.LENGTH_SHORT).show()
        val guideItem = GuideItem(item, text, image, video)
        guideItems.add(guideItem)
    }

    private fun getAllGuideItems(): List<GuideItem> {
        return guideItems.toList()
    }

    override fun addGuideNewVersion(key: Int, text: String, titulText: String?, image: Drawable?, video: String?) {
        MainView().showDialog(text, titulText, image, video, context)
    }

    override fun showInfoFragment(text: String, fragmentManager: FragmentManager) {
        val fragment = InfoFragment.newInstance(getAllGuideItems().toList())
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    //count++
    override fun setCount() {
        SharedPrefManager.incrementCounter(context)
    }

    override fun setCount(count: Int) {
        SharedPrefManager.setCounter(context, count)
    }

    override fun getCount(): Int {
        return SharedPrefManager.getCounter(context)
    }

}