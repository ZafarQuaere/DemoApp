package com.zaf.econnecto.ui.fragments.add_business

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.interfaces.DialogButtonClick


class AddBizScreen1ViewModel : ViewModel() {

    fun openBottomSheetDialog(activity: Activity?, dialogBtn: DialogButtonClick) {
        val view: View = (activity)!!.layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.textOk).setOnClickListener{
            dialogBtn.onOkClick()
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.textCancel).setOnClickListener{
            dialogBtn.onCancelClick()
            dialog.dismiss()
        }
        dialog.show()

    }

}
