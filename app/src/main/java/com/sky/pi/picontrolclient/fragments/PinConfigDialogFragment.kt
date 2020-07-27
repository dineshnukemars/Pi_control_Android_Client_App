package com.sky.pi.picontrolclient.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sky.pi.picontrolclient.OperationData
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.dialog_config_pin.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinConfigDialogFragment : DialogFragment() {
    private val viewModel by sharedViewModel<PinViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pinNo = arguments?.getInt(KEY_PIN_NO) ?: throw Error("pinNo not found")
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_config_pin, null)
        view.blinkRadioBtnV.isChecked = true
        return getCustomDialog(view, pinNo)
    }

    private fun getCustomDialog(
        view: View,
        pinNo: Int
    ): AlertDialog = with(AlertDialog.Builder(requireActivity())) {
        setView(view)
        setPositiveButton("Apply") { _, _ ->
            when (view.pinTypeRadioGroupV.checkedRadioButtonId) {
                R.id.blinkRadioBtnV -> viewModel.updatePinData(pinNo, OperationData.BLINK())
                R.id.inputRadioBtnV -> viewModel.updatePinData(pinNo, OperationData.INPUT())
                R.id.pwmRadioBtnV -> viewModel.updatePinData(pinNo, OperationData.PWM())
                R.id.switchRadioBtnV -> viewModel.updatePinData(pinNo, OperationData.SWITCH())
            }
        }
        setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
        create()
    }

    companion object {
        private const val KEY_PIN_NO = "key_pinNo"

        fun showDialog(pinNo: Int, fragmentManager: FragmentManager) {
            val pinConfigDialogFragment = PinConfigDialogFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_PIN_NO, pinNo)
            pinConfigDialogFragment.arguments = bundle
            pinConfigDialogFragment.show(fragmentManager, "PinConfigDialog")
        }
    }
}