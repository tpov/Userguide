package com.tpov.userguide

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager

/**
 * This library is designed to simplify the use of applications for users,
 * Its functionality is based on drawing a point on the element, and displaying a dialog box that slides out from below.
 *
 * The library contains some functions that allow you to use the dialog box as a regular Alert.dialog,
 * calling it in one line, with different parameters
 *
 * ╔═══════════════════════════════╗
 * ║                               ║
 * ║              Title            ║
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
     * @param titleText Title guide.
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
        titleText: String? = null,
        icon: Drawable? = null,
        video: String? = null,
        callback: (() -> Unit)? = null,
        vararg generalView: View = emptyArray(),
        options: Options = Options()
    ) {

        if (
            (getCounterValue() >= options.countKey
                    || options.countKey == 0)
            && getCounterView(view?.id) < options.countRepeat
        ) {
            // todo Create listener when view != null
            view?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    view.removeOnAttachStateChangeListener(this)

                    initDot(
                        view,
                        generalView,
                        text,
                        titleText,
                        icon,
                        video,
                        callback,
                        options
                    )
                }

                override fun onViewDetachedFromWindow(v: View) {
                }
            })
        }
    }

    private fun initDot(
        item: View?,
        generalView: Array<out View> = emptyArray(),
        text: String? = null,
        titleText: String?,
        image: Drawable?,
        video: String?,
        callback: (() -> Unit)?,
        options: Options
    ) {
        DotView().showDot(
            item,
            generalView,
            text,
            titleText,
            image,
            video,
            context,
            callback,
            options,
            theme,
            buttonClick = {
                incrementView(it)
            }
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
            DotView().showDot(
                item,
                context = context,
                options = Options(countRepeat = 1),
                theme = theme
            )
            incrementView(item.id)
        }
    }
    /**
     * This method calls the dialog immediately after it is called,
     * by default it is called only once.
     *
     * @param text Manual text.
     * @param titleText Title guide.
     * @param icon icon that will be next to the text of the manual.
     * @param video Video that will be next to the text of the manual.
     * @param callback Note that this library intercepts all listeners that contain the view element,
     * For this, the callback parameter has been created,
     * The callback function will be called after clicking on the View element.
     * @param options Additional options for displaying this dialog
     */
    fun addGuideNewVersion(
        text: String,
        titleText: String? = null,
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
                titulText = titleText,
                image = icon,
                video = video,
                context = context,
                theme = theme
            )
        }
    }

    /**
     * Just display a dialog with options
     *
     * @param text Manual text.
     * @param options Additional options for displaying this dialog
     * @param titleText Title guide.
     * @param icon icon that will be next to the text of the manual.
     * @param video Video that will be next to the text of the manual.
     */
    fun addNotification(
        text: String,
        options: Options = Options(),
        titleText: String? = null,
        icon: Drawable? = null,
        video: String? = null
    ) {
        if (options.countKey == getCounterValue() && options.countKey == 0) {
            MainView().showDialog(
                text = text,
                titulText = titleText,
                image = icon,
                video = video,
                context = context,
                theme = theme
            )
        }
    }

    /**
     * Display a full-screen dialog box that will display a list of all the guides that the user has seen.
     * The list of guides to display is taken from the addGuide methods
     *
     * ╔═══════════════════════════════════════════════╗
     * ║                                               ║
     * ║   ╔═══════════════════════════════════════╗   ║
     * ║   ║                Guide1                 ║   ║
     * ║   ║   ┌────────┬───────────┐──────────┐   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   │ *icon* │   *text*  │  *video* │   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   └────────┴───────────┘──────────┘   ║   ║
     * ║   ╚═══════════════════════════════════════╝   ║
     * ║   ╔═══════════════════════════════════════╗   ║
     * ║   ║                Guide2                 ║   ║
     * ║   ║   ┌────────┬───────────┐──────────┐   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   │ *icon* │   *text*  │  *video* │   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   └────────┴───────────┘──────────┘   ║   ║
     * ║   ╚═══════════════════════════════════════╝   ║
     * ║   ╔═══════════════════════════════════════╗   ║
     * ║   ║                Guide3                 ║   ║
     * ║   ║   ┌────────┬───────────┐──────────┐   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   │ *icon* │   *text*  │  *video* │   ║   ║
     * ║   ║   │        │           │          │   ║   ║
     * ║   ║   └────────┴───────────┘──────────┘   ║   ║
     * ║   ╚═══════════════════════════════════════╝   ║
     * ║                                               ║
     * ╚═══════════════════════════════════════════════╝
     *
     * @param text Manual text.
     */
    fun showInfoFragment(text: String, fragmentManager: FragmentManager) {
        val fragment = InfoFragment.newInstance(getAllGuideItems().toList())
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * This method increases the counter by 1,
     * the counter is needed in order to display all the guides that have a countKey >= of this number
     */
    fun setCounterValue() {
        SharedPrefManager.incrementCounter(context)
    }

    /**
     * This method sets the number you need,
     * a counter is needed to display all guides that have a countKey >= of this number
     */
    fun setCounterValue(count: Int) {
        SharedPrefManager.setCounter(context, count)
    }

    private fun getCounterValue(): Int {
        return SharedPrefManager.getCounter(context)
    }

    private fun getCounterView(idView: Int?) = SharedPrefManager.getCounterView(context, idView)

    private fun setCounterView(idView: Int) = SharedPrefManager.setCounterView(context, idView)

    private fun incrementView(idView: Int) =
        SharedPrefManager.incrementCounterDialogView(context, idView)

}