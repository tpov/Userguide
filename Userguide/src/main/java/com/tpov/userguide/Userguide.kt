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

/**
 * This library is designed to simplify the use of applications for users,
 * Its functionality is based on drawing a point on the element, and displaying a dialog box that slides out from below.
 *
 * The library contains some functions that allow you to use the dialog box as a regular Alert.dialog,
 * calling it in one line, with different parameters
 *
 * ╔═══════════════════════════════╗
 * ║                               ║
 * ║              Titul            ║
 * ║                               ║
 * ║   ┌────────┬──────────────┐   ║
 * ║   │        │              │   ║
 * ║   │ *icon* │    *text*    │   ║
 * ║   │        │              │   ║
 * ║   └────────┴──────────────┘   ║
 * ║   ┌────────┬──────────────┐   ║
 * ║   │ *Button│   *Button    │   ║
 * ║   │ open   │     Ok*      │   ║
 * ║   │ video* │              │   ║
 * ║   └────────┴──────────────┘   ║
 * ║                               ║
 * ╚═══════════════════════════════╝
 *
 * @param context context to display the dialog box
 * @param theme Theme for dialog box
 */

class UserGuide(private val context: Context, private val theme: Drawable? = null) {
    private val guideItems: MutableList<GuideItem> = mutableListOf()

    /**
     * Adds a point on the view element, then after clicking on the view - the "callback" code is executed
     * and displays a dialog box with the contents
     *
     * @param view The View element to which the tutorial will be applied.
     * @param text Manual text.
     * @param titul Title guide.
     * @param icon icon that will be next to the text of the manual.
     * @param video Video that will be next to the text of the manual.
     * @param callback Note that this library intercepts all listeners that contain the view element,
     * For this, the callback parameter has been created,
     * The callback function will be called after clicking on the View element.
     *
     * @param options Other settings for the appearance of the guide.
     */
    fun addGuide(
        view: View?,
        text: String,
        titul: String? = null,
        icon: Drawable? = null,
        video: String? = null,
        callback: (() -> Unit)? = null,
        vararg generalView: View = emptyArray(),
        options: Options = Options()
    ) {

        Log.d("dwwdwdwd", "1")
        if (
            (getCounterValue() >= options.countKey  //Общий ключ > ключ этого гайда или ключ гайда = 0
                    || options.countKey == 0)
        ) {
            // todo Create listener when view != null
            view?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    // Удалите слушателя, чтобы не вызывать его снова
                    view.removeOnAttachStateChangeListener(this)

                    if (getCounterView(view.id) < options.countRepeat) {
                        Log.d("dwwdwdwd", "2")
                        initDot(
                            view,
                            generalView,
                            text,
                            titul,
                            icon,
                            video,
                            callback,
                            showOriginalView = options.countRepeat - getCounterView(view.id) == 1
                        )
                    }
                }

                override fun onViewDetachedFromWindow(v: View) {
                    // Здесь можно выполнить дополнительные действия, если необходимо
                }
            })
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

    /**
     * Displays only a point without a dialog box appearing
     */
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
        titul: String? = null,
        icon: Drawable? = null,
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
                titulText = titul,
                image = icon,
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
        if (options.countKey == getCounterValue() && options.countKey == 0) {
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