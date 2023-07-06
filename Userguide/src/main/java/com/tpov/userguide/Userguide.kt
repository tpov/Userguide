package com.tpov.userguide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.FragmentManager

class Userguide(private val context: Context, val theme: Drawable? = null) {
    private val guideItems: MutableList<GuideItem> = mutableListOf()

    fun addGuide(
        view: View,
        text: String,
        titulText: String? = null,
        iconDialog: Drawable? = null,
        video: String? = null,
        callback: (() -> Unit)? = null, //Укажите код который должен выполнится после нажатия на view, поскольку эта библиотека перехватывает слушатель на view
        vararg generalView: View = emptyArray(), //Во всех переданных элементах будет отображаться точка пока будет отображаться точка на view
        options: Options = Options()
    ) {
        val activity = view.context as? Activity

        if (
            activity != null && !activity.isFinishing
            && (getCounterValue() >= options.countKey  //Общий ключ > ключ этого гайда или ключ гайда = 0
                    || options.countKey == 0)
            && getCounterView(view.id) < options.countRepeat
        ) {
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

            // todo Create listener when view != null
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
        generalView: Array<out View> = emptyArray(),
        text: String? = null,
        titulText: String?,
        image: Drawable?,
        video: String?,
        callback: (() -> Unit)?,
        showOriginalView: Boolean
    ) {
        DotView().showDot(
            item,
            generalView,
            text,
            titulText,
            image,
            video,
            context,
            callback,
            showOriginalView,
            theme
        )

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
        val packageName = context.applicationContext.packageName
        val packageManager = context.applicationContext.packageManager
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = packageInfo.versionCode

        if (getCounterView(0) < options.countRepeat && (options.countKeyVersion == versionCode || options.countKeyVersion == 0)) {
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
        options: Options = Options(),
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null
    ) {
        if (options.countKey == getCounterValue() && options.countKey == 0 ) {
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