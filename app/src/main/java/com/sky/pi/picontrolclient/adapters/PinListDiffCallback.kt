package com.sky.pi.picontrolclient.adapters

import androidx.recyclerview.widget.DiffUtil
import com.sky.pi.picontrolclient.PinData

class PinListDiffCallback : DiffUtil.ItemCallback<PinData>() {
    override fun areItemsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem.pinNo == newItem.pinNo
    }

    override fun areContentsTheSame(oldItem: PinData, newItem: PinData): Boolean {
        return oldItem == newItem
    }
}