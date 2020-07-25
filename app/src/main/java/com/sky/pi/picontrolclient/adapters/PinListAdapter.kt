package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.OperationType
import com.sky.pi.picontrolclient.PinData
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.inflateLayout
import kotlinx.android.synthetic.main.item_pin_title_with_options.view.*
import kotlinx.android.synthetic.main.item_switch_card.view.*

class PinListAdapter(
    private val onClickItem: (itemAction: ItemAction) -> Unit
) : ListAdapter<PinData, PinListAdapter.PinViewHolder>(
    PinListDiffCallback()
) {

    inner class PinViewHolder(
        pinView: View
    ) : RecyclerView.ViewHolder(pinView) {

        @SuppressLint("SetTextI18n")
        fun setDataToView(pinData: PinData) {
            itemView.pinTextV.text = "gpio ${pinData.gpioNo} type ${pinData.gpioType}"
            itemView.deletePinImgV.setOnClickListener { onClickItem(ItemAction.Remove(pinData)) }
            itemView.pinConfigureImgV.setOnClickListener { onClickItem(ItemAction.Configure(pinData)) }

            itemView.pinSwitchV.setOnCheckedChangeListener { _, isChecked ->
                val copy = pinData.copy(operationType = OperationType.SWITCH(isChecked))
                onClickItem(ItemAction.UpdateOperationData(copy))
            }

            when (pinData.operationType) {
                is OperationType.INPUT -> {
                    println("not implemented yet")
                }
                is OperationType.PWM -> TODO()
                is OperationType.SWITCH -> TODO()
                is OperationType.BLINK -> TODO()
                OperationType.NONE -> TODO()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        return PinViewHolder(parent.inflateLayout(viewType))
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.setDataToView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position).operationType) {
        is OperationType.NONE -> R.layout.item_pindata
        is OperationType.INPUT -> R.layout.item_input_card
        is OperationType.PWM -> R.layout.item_pwm_card
        is OperationType.SWITCH -> R.layout.item_switch_card
        is OperationType.BLINK -> R.layout.item_blink_card
    }
}

class PinListDiffCallback : DiffUtil.ItemCallback<PinData>() {
    override fun areItemsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem.pinNo == newItem.pinNo
    }

    override fun areContentsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem == newItem
    }
}

sealed class ItemAction {
    data class Remove(val pinData: PinData) : ItemAction()
    data class Configure(val pinData: PinData) : ItemAction()
    data class UpdateOperationData(val pinData: PinData) : ItemAction()
}