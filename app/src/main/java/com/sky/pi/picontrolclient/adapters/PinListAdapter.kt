package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.*
import kotlinx.android.synthetic.main.item_blink_card.view.*
import kotlinx.android.synthetic.main.item_pin_title_with_options.view.*
import kotlinx.android.synthetic.main.item_pwm_card.view.*
import kotlinx.android.synthetic.main.item_switch_card.view.*
import kotlin.reflect.KClass

class PinListAdapter(
    private val onDeletePin: (pinNo: Int) -> Unit,
    private val onConfigurePin: (pinNo: Int) -> Unit,
    private val onUpdatePin: (pinNo: Int, operationData: OperationData) -> Unit
) : ListAdapter<PinData, PinListAdapter.PinViewHolder>(
    PinListDiffCallback()
) {
    private val viewTypeArray =
        arrayOf(
            ViewType(0, OperationData.NONE::class, R.layout.item_pindata),
            ViewType(1, OperationData.INPUT::class, R.layout.item_input_card),
            ViewType(2, OperationData.SWITCH::class, R.layout.item_switch_card),
            ViewType(3, OperationData.BLINK::class, R.layout.item_blink_card),
            ViewType(4, OperationData.PWM::class, R.layout.item_pwm_card)
        )

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
        ) {
            itemView.onTimeSeekBarV.setSeekBarListener {
                onUpdatePin(pinNo, operationData.copy(highTime = it.progress.toFloat()))
            }
        }

        private fun listenSwitchChange(
            pinNo: Int,
            operationData: OperationData.SWITCH
        ) {
            itemView.pinSwitchV.setOnCheckedChangeListener { _, isChecked ->
                onUpdatePin(pinNo, operationData.copy(isOn = isChecked))
            }
        }

        private fun listenPwmSeekBarChange(
            pinNo: Int,
            operationData: OperationData.PWM
        ) {
            itemView.pwmSeekBarV.setSeekBarListener {
                onUpdatePin(pinNo, operationData.copy(dutyCycle = it.progress.toFloat() / 100))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        val layoutId = viewTypeArray.find {
            it.id == viewType
        }?.layoutId ?: throw Error("viewType not found")
        return PinViewHolder(parent.inflateLayout(layoutId))
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.setDataToView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypeArray.find {
            it.opType == getItem(position).operationData::class
        }?.id ?: throw Error("operation type not found")
    }

    private data class ViewType<T : Any>(
        val id: Int,
        val opType: KClass<T>,
        @LayoutRes val layoutId: Int
    )
}

class PinListDiffCallback : DiffUtil.ItemCallback<PinData>() {
    override fun areItemsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem.pinNo == newItem.pinNo
    }

    override fun areContentsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem == newItem
    }
}