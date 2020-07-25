package com.sky.pi.picontrolclient.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sky.pi.picontrolclient.PinData
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.getGpioText
import com.sky.pi.picontrolclient.inflateLayout
import kotlinx.android.synthetic.main.item_pin_title_with_options.view.*

class PinListAdapter(
    private val onClickItem: (pinNo: Int) -> Unit
) :
    ListAdapter<PinData, PinListAdapter.PinViewHolder>(
        PinListDiffCallback()
    ) {

    inner class PinViewHolder(
        pinView: View
    ) : RecyclerView.ViewHolder(pinView) {

        @SuppressLint("SetTextI18n")
        fun setDataToView(pinData: PinData) {
            itemView.pinTextV.text = getGpioText(pinData)
            itemView.pinTextV.setOnClickListener { onClickItem(pinData.pinNo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        return PinViewHolder(parent.inflateLayout(R.layout.item_pindata))
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.setDataToView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
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
