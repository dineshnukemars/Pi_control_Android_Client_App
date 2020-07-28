package com.sky.pi.picontrolclient.adapters

import androidx.annotation.LayoutRes
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.models.OperationData
import kotlin.reflect.KClass

class AdapterViewType {
    private val viewTypeArray = arrayOf(
        ViewType(
            id = 0,
            opType = OperationData.NONE::class,
            layoutId = R.layout.item_pindata
        ),
        ViewType(
            id = 1,
            opType = OperationData.INPUT::class,
            layoutId = R.layout.item_input_card
        ),
        ViewType(
            id = 2,
            opType = OperationData.SWITCH::class,
            layoutId = R.layout.item_switch_card
        ),
        ViewType(
            id = 3,
            opType = OperationData.BLINK::class,
            layoutId = R.layout.item_blink_card
        ),
        ViewType(
            id = 4,
            opType = OperationData.PWM::class,
            layoutId = R.layout.item_pwm_card
        )
    )

    fun getLayoutIdForViewTypeId(viewType: Int): Int = viewTypeArray.find {
        it.id == viewType
    }?.layoutId ?: throw Error("viewType not found")

    fun getViewTypeIdForItem(kClass: KClass<out OperationData>): Int = viewTypeArray.find {
        it.opType == kClass
    }?.id ?: throw Error("operation type not found")

    private data class ViewType<T : Any>(
        val id: Int,
        val opType: KClass<T>,
        @LayoutRes val layoutId: Int
    )
}