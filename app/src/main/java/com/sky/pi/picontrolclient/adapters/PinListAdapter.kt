package com.sky.pi.picontrolclient.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sky.pi.picontrolclient.inflateLayout
import com.sky.pi.picontrolclient.models.Pin

class PinListAdapter(
    private val itemActionListener: ItemActionListener,
    private val adapterViewType: AdapterViewType
) : ListAdapter<Pin, PinViewHolder>(
    PinListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder =
        PinViewHolder(
            itemActionListener = itemActionListener,
            pinView = parent.inflateLayout(adapterViewType.getLayoutIdForViewTypeId(viewType))
        )

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) =
        holder.setDataToView(getItem(position))

    override fun getItemViewType(position: Int): Int =
        adapterViewType.getViewTypeIdForItem(getItem(position).operationData::class)
}