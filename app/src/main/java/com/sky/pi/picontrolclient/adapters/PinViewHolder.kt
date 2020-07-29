package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.doNothing
import com.sky.pi.picontrolclient.models.Operation
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
    ): Unit = when (val operationData = pin.operation) {
        is Operation.INPUT -> println("not implemented yet")
        is Operation.PWM -> setupPwmV(pinNo, operationData)
        is Operation.SWITCH -> setupSwitchView(pinNo, operationData)
        is Operation.BLINK -> setupBlinkView(pinNo, operationData)
        is Operation.NONE -> doNothing()
    }

    private fun setupBlinkView(
        pinNo: Int,
        operation: Operation.BLINK
    ): Unit = with(itemView.onTimeSeekBarV) {
        progress = operation.highTime.toInt()
        setSeekBarListener {
            itemActionListener.onUpdatePin(
                pinNo = pinNo,
                operation = operation.copy(highTime = progress.toFloat())
            )
        }
    }

    private fun setupSwitchView(
        pinNo: Int,
        operation: Operation.SWITCH
    ): Unit = with(itemView.pinSwitchV) {
        isChecked = operation.isOn
        setOnClickListener {
            itemActionListener.onUpdatePin(
                pinNo = pinNo,
                operation = operation.copy(isOn = isChecked)
            )
        }
    }

    private fun setupPwmV(
        pinNo: Int,
        operation: Operation.PWM
    ): Unit = with(itemView.pwmSeekBarV) {
        progress = (operation.dutyCycle * 100f).toInt()
        setSeekBarListener {
            itemActionListener.onUpdatePin(
                pinNo = pinNo,
                operation = operation.copy(dutyCycle = progress.toFloat() / 100)
            )
        }
    }
}