package com.sky.pi.client.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sky.pi.client.libs.models.Pin

class PinListDiffCallback : DiffUtil.ItemCallback<Pin>() {
    override fun areItemsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return oldItem.pinNo == newItem.pinNo
    }

    override fun areContentsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return oldItem == newItem
    }
}