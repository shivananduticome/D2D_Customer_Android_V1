package com.d2d.customer.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.d2d.customer.R

class ProgressDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            /*val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))*/

            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.custom_dialog_progress)

/* Custom setting to change TextView text,Color and Text Size according to your Preference*/

            val progressTv = dialog.findViewById(R.id.progress_tv) as TextView
            progressTv.text = context.resources.getString(R.string.loading)
            progressTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            progressTv.textSize = 19F
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            //dialog.show()
            return dialog
        }
    }
}