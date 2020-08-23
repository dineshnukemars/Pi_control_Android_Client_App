package com.sky.pi.client.ui.adapter

import androidx.annotation.LayoutRes
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.ui.R
import kotlin.reflect.KClass

class AdapterViewType {
    private val viewTypeArray = arrayOf(
        ViewType(
            id = 0,
            opType = Operation.NONE::class,
            layoutId = R.layout.item_pin_not_configured
        ),
        ViewType(
            id = 1,
            opType = Operation.INPUT::class,
            layoutId = R.layout.item_input_card
        ),
        ViewType(
            id = 2,
            opType = Operation.SWITCH::class,
            layoutId = R.layout.item_switch_card
        ),
        ViewType(
            id = 3,
            opType = Operation.BLINK::class,
            layoutId = R.layout.item_blink_card
        ),
        ViewType(
            id = 4,
            opType = Operation.PWM::class,
            layoutId = R.layout.item_pwm_card
        )
    )

    fun getLayoutIdForViewTypeId(viewType: Int): Int = viewTypeArray.find {
        it.id == viewType
    }?.layoutId ?: throw Error("viewType not found")

    fun getViewTypeIdForItem(kClass: KClass<out Operation>): Int = viewTypeArray.find {
        it.opType == kClass
    }?.id ?: throw Error("operation type not found")

    private data class ViewType<T : Any>(
        val id: Int,
        val opType: KClass<T>,
        @LayoutRes val layoutId: Int
    )
}