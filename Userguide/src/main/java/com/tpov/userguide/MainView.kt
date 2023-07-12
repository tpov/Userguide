package com.tpov.userguide

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


@UnstableApi //This is main dialog show information for user
class MainView : AppCompatActivity() {

    fun showDialog(
        text: String,
        context: Context,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null,
        theme: Drawable?,
        item: View,
        clickButton: (Int) -> (Unit)
    ) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setWindowAnimations(R.style.UserguideDialogAnimation)
        if (theme != null) dialog.window?.setBackgroundDrawable(theme)

        val dialogView = View.inflate(context, R.layout.userguide_dialog_layout, null)
        val animationView = dialogView.findViewById<ImageView>(R.id.imv_ok)
        val videoIcon = dialogView.findViewById<ImageView>(R.id.imv_video)

        animationView.setOnClickListener {
            clickButton(item.id)
            dialog.dismiss()
        }

        val container =
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.parent as? View
        container?.setBackgroundColor(Color.TRANSPARENT)
        dialog.setContentView(dialogView)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            dialog.findViewById<TextView>(R.id.tv_text)?.text = text
            dialog.findViewById<TextView>(R.id.tv_titul)?.text = titulText
            dialog.findViewById<ImageView>(R.id.imv_icon)?.setImageDrawable(image)
            val behavior = BottomSheetBehavior.from(bottomSheet ?: run {
                Toast.makeText(context, "Bottom sheet is null", Toast.LENGTH_SHORT).show()
                return@setOnShowListener
            })
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            if (video != null) {
                videoIcon.visibility = View.VISIBLE

                videoIcon.setOnClickListener {
                    showVideoDialog(video, context)
                }
            } else videoIcon.visibility = View.GONE
        }
        dialog.show()
    }

    private fun showVideoDialog(video: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video))
        context.startActivity(intent)
    }
}