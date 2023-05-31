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
            reverseFont: Boolean? = false, //for change language alert
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
                val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window?.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                lp.gravity = Gravity.CENTER
                dialog.window?.attributes = lp
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(isCancellable ?: true)
                dialog.setCancelable(isCancellable ?: true)

                binding.textViewTitle.text = title
                binding.textViewMessage.text = msg
                binding.buttonNegative.text = noBtn
                binding.buttonPositive.text = yesBtn

                binding.buttonNegative.visibility = if (singleBtn) View.GONE else View.VISIBLE
                binding.divMiddle.visibility = if (singleBtn) View.GONE else View.VISIBLE
                binding.buttonNegative.setOnClickListener {
                    dialog.dismiss()
                    alertDialogInterface.onNoClick()
                }
                binding.buttonPositive.setOnClickListener {
                    dialog.dismiss()
                    alertDialogInterface.onYesClick()
                }
                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}