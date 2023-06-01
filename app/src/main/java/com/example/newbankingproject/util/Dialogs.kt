package com.example.newbankingproject.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.DialogAlertBinding
import com.example.newbankingproject.listener.AlertDialogInterface

/**Dialogs  is responsible to show custom alert*/
class Dialogs {
    companion object {
        fun showCustomAlert(
            activity: Activity,
            title: String,
            msg: String,
            yesBtn: String,
            noBtn: String,
            singleBtn: Boolean = false,
            isCancellable: Boolean? = true,
            alertDialogInterface: AlertDialogInterface,
        ) {
            try {
                val dialog = Dialog(activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val binding: DialogAlertBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(activity),
                    R.layout.dialog_alert, null, false
                )
                dialog.setContentView(binding.root)
                val lp: WindowManager.LayoutParams = WindowManager.LayoutParams().apply {
                    copyFrom(dialog.window?.attributes)
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    gravity = Gravity.CENTER
                }
                dialog.apply {
                    window?.attributes = lp
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setCanceledOnTouchOutside(isCancellable ?: true)
                    setCancelable(isCancellable ?: true)

                }
                binding.apply {
                    textViewTitle.text = title
                    textViewMessage.text = msg
                    buttonNegative.text = noBtn
                    buttonPositive.text = yesBtn

                    buttonNegative.visibility = if (singleBtn) View.GONE else View.VISIBLE
                    divMiddle.visibility = if (singleBtn) View.GONE else View.VISIBLE
                    buttonNegative.setOnClickListener {
                        dialog.dismiss()
                        alertDialogInterface.onNoClick()
                    }
                    buttonPositive.setOnClickListener {
                        dialog.dismiss()
                        alertDialogInterface.onYesClick()
                    }
                }
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}