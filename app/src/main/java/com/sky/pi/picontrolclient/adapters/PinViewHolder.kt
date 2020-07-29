package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.doNothing
import com.sky.pi.picontrolclient.models.OperationData
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.setSeekBarListener
import kotlinx.android.synthetic.main.item_blink_card.view.*
import kotlinx.android.synthetic.main.item_pin_title_with_options.view.*
import kotlinx.android.synthetic.main.item_pwm_card.view.*
import kotlinx.android.synthetic.main.item_switch_card.view.*

class PinViewHolder(
    private val itemActionListener: ItemActionListener,
    pinView: View
) : RecyclerView.ViewHolder(pinView) {

    @SuppressLint("SetTextI18n")
    fun setDataToView(pin: Pin) {
        val pinNo = pin.pinNo
        itemView.pinTextV.text = "gpio ${pin.gpioNo} type ${pin.pinType}"
        itemView.deletePinImgV.setOnClickListener { itemActionListener.onDeletePin(pinNo) }
        itemView.pinConfigureImgV.setOnClickListener { itemActionListener.onConfigurePin(pinNo) }
        setLayoutSpecificData(pin, pinNo)
    }

    private fun setLayoutSpecificData(
        pin: Pin,
        pinNo: Int
    ): Unit = when (val operationData = pin.operationData) {
        is OperationData.INPUT -> println("not implemented yet")
        is OperationData.PWM -> setupPwmV(pinNo, operationData)
        is OperationData.SWITCH -> setupSwitchView(pinNo, operationData)
        is OperationData.BLINK -> setupBlinkView(pinNo, operationData)
        is OperationData.NONE -> doNothing()
    }

    private fun setupBlinkView(
        pinNo: Int,
        operationData: OperationData.BLINK
    ) {
        itemView.onTimeSeekBarV.progress = operationData.highTime.toInt()
        itemView.onTimeSeekBarV.setSeekBarListener {
            itemActionListener.onUpdatePin(
                pinNo,
                operationData.copy(highTime = it.progress.toFloat())
            )
        }
    }

    private fun setupSwitchView(
        pinNo: Int,
        operationData: OperationData.SWITCH
    ) {
        itemView.pinSwitchV.isChecked = operationData.isOn
        itemView.pinSwitchV.setOnCheckedChangeListener { _, isChecked ->
            itemActionListener.onUpdatePin(pinNo, operationData.copy(isOn = isChecked))
        }
    }

    private fun setupPwmV(
        pinNo: Int,
        operationData: OperationData.PWM
    ) {
        itemView.pwmSeekBarV.progress = (operationData.dutyCycle * 100f).toInt()
        itemView.pwmSeekBarV.setSeekBarListener {
            itemActionListener.onUpdatePin(pinNo, operationData.copy(dutyCycle = it.progress.toFloat() / 100))
        }
    }
}