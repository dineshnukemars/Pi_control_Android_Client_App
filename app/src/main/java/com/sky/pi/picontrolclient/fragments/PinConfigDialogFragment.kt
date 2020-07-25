package com.sky.pi.picontrolclient.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sky.pi.picontrolclient.R

class PinConfigDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val customView = requireActivity().layoutInflater.inflate(R.layout.dialog_config_pin, null)
            builder.setView(customView)
                .setPositiveButton("Apply") { _, _ ->
                    TODO()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}