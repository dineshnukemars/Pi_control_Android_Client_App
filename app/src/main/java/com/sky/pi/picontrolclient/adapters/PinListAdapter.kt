package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.*
import kotlinx.android.synthetic.main.item_blink_card.view.*
import kotlinx.android.synthetic.main.item_pin_title_with_options.view.*
import kotlinx.android.synthetic.main.item_pwm_card.view.*
import kotlinx.android.synthetic.main.item_switch_card.view.*

class PinListAdapter(
    private val onDeletePin: (pinNo: Int) -> Unit,
    private val onConfigurePin: (pinNo: Int) -> Unit,
    private val onUpdatePin: (pinNo: Int, operationData: OperationData) -> Unit,
    private val adapterViewType: AdapterViewType
) : ListAdapter<PinData, PinListAdapter.PinViewHolder>(
    PinListDiffCallback()
) {
    inner class PinViewHolder(pinView: View) : RecyclerView.ViewHolder(pinView) {

        @SuppressLint("SetTextI18n")
        fun setDataToView(pinData: PinData) {
            val pinNo = pinData.pinNo
            itemView.pinTextV.text = "gpio ${pinData.gpioNo} type ${pinData.gpioType}"
            itemView.deletePinImgV.setOnClickListener { onDeletePin(pinNo) }
            itemView.pinConfigureImgV.setOnClickListener { onConfigurePin(pinNo) }
            setLayoutSpecificData(pinData, pinNo)
        }

        private fun setLayoutSpecificData(
            pinData: PinData,
            pinNo: Int
        ): Unit = when (val operationData = pinData.operationData) {
            is OperationData.INPUT -> println("not implemented yet")
            is OperationData.PWM -> listenPwmSeekBarChange(pinNo, operationData)
            is OperationData.SWITCH -> listenSwitchChange(pinNo, operationData)
            is OperationData.BLINK -> listenBlinkSeekBarChange(pinNo, operationData)
            is OperationData.NONE -> doNothing()
        }

        private fun listenBlinkSeekBarChange(
            pinNo: Int,
            operationData: OperationData.BLINK
        ): Unit = itemView.onTimeSeekBarV.setSeekBarListener {
            onUpdatePin(pinNo, operationData.copy(highTime = it.progress.toFloat()))
        }

        private fun listenSwitchChange(
            pinNo: Int,
            operationData: OperationData.SWITCH
        ): Unit = itemView.pinSwitchV.setOnCheckedChangeListener { _, isChecked ->
            onUpdatePin(pinNo, operationData.copy(isOn = isChecked))
        }

        private fun listenPwmSeekBarChange(
            pinNo: Int,
            operationData: OperationData.PWM
        ): Unit = itemView.pwmSeekBarV.setSeekBarListener {
            onUpdatePin(pinNo, operationData.copy(dutyCycle = it.progress.toFloat() / 100))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder =
        PinViewHolder(parent.inflateLayout(adapterViewType.getLayoutIdForViewTypeId(viewType)))

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) =
        holder.setDataToView(getItem(position))

    override fun getItemViewType(position: Int): Int =
        adapterViewType.getViewTypeIdForItem(getItem(position).operationData::class)
}