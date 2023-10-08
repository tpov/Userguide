package com.tpov.userguide

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


@UnstableApi //This is main dialog show information for user
internal class MainView : AppCompatActivity() {

    fun showDialog(
        text: String,
        context: Context,
        titulText: String? = null,
        image: Drawable? = null,
        video: String? = null,
        theme: Drawable?,
        item: View? = null,
        clickButton: (Int) -> (Unit)
    ) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setWindowAnimations(R.style.UserguideDialogAnimation)
        if (theme != null) dialog.window?.setBackgroundDrawable(theme)

        val dialogView = View.inflate(context, R.layout.userguide_dialog_layout, null)
        val bOk = dialogView.findViewById<ImageView>(R.id.imv_ok)
        val videoIcon = dialogView.findViewById<ImageView>(R.id.imv_video)
        val imageView = dialogView.findViewById<ImageView>(R.id.imv_icon)
        val firstLayout = dialogView.findViewById<LinearLayout>(R.id.firstLayout1)

        bOk.setOnClickListener {
            dialog.dismiss()
            clickButton(item?.id ?: 0)
        }

        val container =
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.parent as? View
        container?.setBackgroundColor(Color.TRANSPARENT)
        dialog.setContentView(dialogView)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            dialog.findViewById<TextView>(R.id.tv_text)?.text = text
            dialog.findViewById<TextView>(R.id.tv_titul)?.text = titulText

            if (image == null) imageView.visibility = View.GONE
            else imageView.setImageDrawable(image)

            val behavior = BottomSheetBehavior.from(bottomSheet ?: run {
                Toast.makeText(context, "Bottom sheet is null", Toast.LENGTH_SHORT).show()
                return@setOnShowListener
            })
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = false
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            if (video != null) {
                videoIcon.visibility = View.VISIBLE
                firstLayout.visibility = View.VISIBLE

                videoIcon.setOnClickListener {
                    showVideoDialog(video, context)
                }
            } else {
                videoIcon.visibility = View.GONE
                firstLayout.visibility = View.GONE
            }
        }
        dialog.show()
    }

    private fun showVideoDialog(video: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video))
        context.startActivity(intent)
    }
}