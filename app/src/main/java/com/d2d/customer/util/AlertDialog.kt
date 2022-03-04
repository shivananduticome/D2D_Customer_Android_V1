package com.d2d.customer.util
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

class AlertDialog {
   companion object
    fun alertDialog(context: Context,alertTitle:String,message:String): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
            builder.setTitle(alertTitle)
            builder.setMessage(message)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
            builder.setPositiveButton("Yes"){ _, _ ->
            Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
              }

        //performing cancel action
        builder.setNeutralButton("Cancel"){ _, _ ->
           Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }

        return builder
    }
}