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
    private val onClickItem: (itemAction: ItemAction, pinData: PinData) -> Unit
) : ListAdapter<PinData, PinListAdapter.PinViewHolder>(
    PinListDiffCallback()
) {

    inner class PinViewHolder(
        pinView: View
    ) : RecyclerView.ViewHolder(pinView) {

        @SuppressLint("SetTextI18n")
        fun setDataToView(pinData: PinData) {
            itemView.pinTextV.text = "gpio ${pinData.gpioNo} type ${pinData.gpioType}"
            itemView.deletePinImgV.setOnClickListener {
                onClickItem(ItemAction.RemovePin, pinData)
            }
            itemView.pinConfigureImgV.setOnClickListener {
                onClickItem(ItemAction.ConfigurePin, pinData)
            }

            when (pinData.operationType) {
                is OperationType.INPUT -> {
                    println("not implemented yet")
                }
                is OperationType.PWM -> {
                    itemView.pwmSeekBarV.setSeekBarListener {
                        onClickItem(ItemAction.UpdateData, pinData)
                    }
                }
                is OperationType.SWITCH -> {
                    itemView.pinSwitchV.setOnCheckedChangeListener { _, isChecked ->
                        onClickItem(
                            ItemAction.UpdateData,
                            pinData.copy(operationType = OperationType.SWITCH(isChecked))
                        )
                    }
                }
                is OperationType.BLINK -> {
                    itemView.onTimeSeekBarV.setSeekBarListener {
                        onClickItem(ItemAction.UpdateData, pinData)
                    }
                }
                is OperationType.NONE -> doNothing()
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
            it.opType == getItem(position).operationType::class
        }?.id ?: throw Error("operation type not found")
    }

    private val viewTypeArray =
        arrayOf(
            ViewTypeData(0, OperationType.NONE::class, R.layout.item_pindata),
            ViewTypeData(1, OperationType.INPUT::class, R.layout.item_input_card),
            ViewTypeData(2, OperationType.SWITCH::class, R.layout.item_switch_card),
            ViewTypeData(3, OperationType.BLINK::class, R.layout.item_blink_card),
            ViewTypeData(4, OperationType.PWM::class, R.layout.item_pwm_card)
        )

    private data class ViewTypeData<T : Any>(
        val id: Int,
        val opType: KClass<T>,
        @LayoutRes val layoutId: Int
    )

    fun buildPwmPin() {

    }

    fun buildSwitchPin() {

    }

    fun buildBlinkPin() {

    }

    fun buildInputPin() {

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

enum class ItemAction {
    RemovePin,
    ConfigurePin,
    UpdateData
}