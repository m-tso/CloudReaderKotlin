package com.example.cloudreaderkotloin.bussiness.common.utils

import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.cloudreaderkotloin.R

fun showSimpleDialog(
    v: View,
    content: String?,
    positiveText: String?,
    negativeText: String?,
    clickListener: DialogInterface.OnClickListener?
) {
    val builder =
        AlertDialog.Builder(v.context)
    val view =
        View.inflate(v.context, R.layout.dialog_simple, null)
    val titleTop = view.findViewById<TextView>(R.id.title_top)
    titleTop.text = content
    val dialog = builder.setView(view)
        .setPositiveButton(positiveText, clickListener)
        .setNegativeButton(negativeText, null)
        .create()
    dialog.show()

    dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        .setTextColor(v.context.resources.getColor(R.color.colorTheme))
    dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        .setTextColor(v.context.resources.getColor(R.color.colorTheme))


}