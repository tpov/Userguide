package com.tpov.userguide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import kotlin.coroutines.coroutineContext

class Userguide(private val context: Context, val theme: Drawable? = null) {
    private val guideItems: MutableList<GuideItem> = mutableListOf()

    fun addGuide(
        view: View?,
        text: String,
        titulText: String? = null,
        iconDialog: Drawable? = null,
        video: String? = null,
        callback: (() -> Unit)? = null,
        vararg generalView: View = emptyArray(),
        options: Options = Options()
    ) {
        val activity = view?.context as? Activity
        view?.viewTreeObserver?.addOnDrawListener {
            Log.d("gfesfse", "fun onGlobalLayout() 1")
            initDot(
                view,
                generalView,
                text,
                titulText,
                iconDialog,
                video,
                callback,
                showOriginalView = options.countRepeat - getCounterView(view.id) == 1
            )
        }
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            Log.d("gfesfse", "fun onGlobalLayout() 2")
            initDot(
                view,
                generalView,
                text,
                titulText,
                iconDialog,
                video,
                callback,
                showOriginalView = options.countRepeat - getCounterView(view.id) == 1
            )
        }
        if (
            activity != null && !activity.isFinishing
            && (getCounterValue() >= options.countKey || options.countKey == 0)
            && getCounterView(view.id) < options.countRepeat
        ) {

            Log.d("gfesfse", "fun onGlobalLayout() 0")
            view.viewTreeObserver.addOnDrawListener {
                Log.d("gfesfse", "fun onGlobalLayout() 1")
                initDot(
                    view,
                    generalView,
                    text,
                    titulText,
                    iconDialog,
                    video,
                    callback,
                    showOriginalView = options.countRepeat - getCounterView(view.id) == 1
                )
            }
            view.viewTreeObserver.addOnGlobalLayoutListener {
                Log.d("gfesfse", "fun onGlobalLayout() 2")
                initDot(
                    view,
                    generalView,
                    text,
                    titulText,
                    iconDialog,
                    video,
                    callback,
                    showOriginalView = options.countRepeat - getCounterView(view.id) == 1
                )
            }


        }
    }

    private fun initDot(
        item: View?,
        generalView: Array<out View> = emptyArray(),
        text: String? = null,
        titulText: String?,
        image: Drawable?,
        video: String?,
        callback: (() -> Unit)?,
        showOriginalView: Boolean
    ) {
        DotView().showDot(item, generalView, text, titulText, image, video, context, callback, showOriginalView, theme)

        val guideItem = GuideItem(item, text, image, video)
        guideItems.add(guideItem)
    }

    private fun getAllGuideItems(): List<GuideItem> {
        return guideItems.toList()
    }

    fun addOnlyDot(
        item: View
    ) {
        if (getCounterView(item.id) == 0) {
            DotView().showDot(item, context = context, showOriginalView = true, theme = theme)
            incrementView(item.id)
        }
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
                context = context,
                theme = theme
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
                context = context,
                theme = theme
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

    private fun incrementView(idView: Int) =
        SharedPrefManager.incrementCounterDialogView(context, idView)

}