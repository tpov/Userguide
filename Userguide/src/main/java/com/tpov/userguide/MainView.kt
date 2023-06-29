package com.tpov.userguide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
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
        titulText: String?,
        image: Drawable?,
        video: String?,
        context: Context
    ) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setWindowAnimations(R.style.UserguideDialogAnimation)

        val dialogView = View.inflate(context, R.layout.userguide_dialog_layout, null)
        val animationView: LottieAnimationView = dialogView.findViewById(R.id.anv_true)
        val videoIcon = dialogView.findViewById<ImageView>(R.id.imv_video)

        animationView.setAnimation("tick.json")
        animationView.playAnimation()
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.setOnClickListener {
            dialog.closeOptionsMenu()
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
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_video)
        dialog.setCancelable(true)

        dialog.show()

        val videoView = dialog.findViewById<PlayerView>(R.id.playerView)

        initializePlayer(context, videoView, video)

        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(dialog.window?.attributes)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        dialog.window?.attributes = layoutParams
    }

    private fun initializePlayer(context: Context, videoView: PlayerView, video: String) {
        val player = ExoPlayer.Builder(context) // <- context
            .build()

        // create a media item.
        val mediaItem = MediaItem.Builder()
            .setUri(video)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        // Create a media source and pass the media item
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(context) // <- context
        )
            .createMediaSource(mediaItem)

        // Finally assign this media source to the player
        player.apply {
            setMediaSource(mediaSource)
            playWhenReady = true // start playing when the exoplayer has setup
            seekTo(0, 0L) // Start from the beginning
            prepare() // Change the state from idle.
        }.also {
            // Do not forget to attach the player to the view
            videoView.player = it
        }
    }
}