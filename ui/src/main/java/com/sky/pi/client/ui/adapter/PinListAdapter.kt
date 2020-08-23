package com.sky.pi.client.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sky.pi.client.libs.models.Pin

class PinListAdapter(
    private val itemActionListener: ItemActionListener,
    private val adapterViewType: AdapterViewType
) : ListAdapter<Pin, PinViewHolder>(
    PinListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder =
        PinViewHolder(itemActionListener, pinLayoutView(parent, viewType))

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) =
        holder.setDataToView(getItem(position))

    override fun getItemViewType(position: Int): Int =
        adapterViewType.getViewTypeIdForItem(getItem(position).operation::class)

    private fun pinLayoutView(
        parent: ViewGroup,
        viewType: Int
    ) = LayoutInflater.from(parent.context)
        .inflate(adapterViewType.getLayoutIdForViewTypeId(viewType), parent, false)
}