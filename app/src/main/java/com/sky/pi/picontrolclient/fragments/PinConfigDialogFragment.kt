package com.sky.pi.picontrolclient.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.dialog_config_pin.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinConfigDialogFragment : DialogFragment() {
    private val viewModel by sharedViewModel<PinViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_config_pin, null)

        view.pinTypeRadioGroupV.setOnCheckedChangeListener { group: RadioGroup, checkedId: Int ->

        }
        builder.setView(view)
            .setPositiveButton("Apply") { _, _ ->

            }
            .setNegativeButton("Cancel") { _, _ ->
                dialog?.cancel()
            }
        return builder.create()
    }
}