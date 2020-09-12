package com.sky.pi.client.ui.compose.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.ui.compose.description
import com.sky.pi.client.ui.compose.isContainsPin
import com.sky.pi.client.ui.compose.isEnabled
import com.sky.pi.client.ui.compose.to2DigitString
import com.sky.pi.client.ui.compose.ui.Styles


@Composable
fun BoardLayoutScreen(vm: PinViewModel) = Row {
    listCheckboxesOnColumn(vm.leftPinListLD, vm)
    listCheckboxesOnColumn(vm.rightPinListLD, vm)
}

@Composable
private fun listCheckboxesOnColumn(
    pinList: List<Pin>,
    vm: PinViewModel,
) {
    val modifier = Modifier
        .fillMaxHeight()
        .padding(bottom = 56.dp)
        .weight(1f)
        .background(Color(0xffff00ff))
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier,
        horizontalGravity = Alignment.Start
    ) {
        pinList.forEach { itemView(it, vm) }
    }
}

@Composable
private fun itemView(
    pin: Pin,
    vm: PinViewModel,
) = Row(verticalGravity = Alignment.CenterVertically) {
    Text(text = pin.pinNo.to2DigitString(), style = Styles.contentSubtitle1)
    setCheckbox(vm, pin)
    Text(text = pin.description(), style = Styles.contentSubtitle2)
}

@Composable
private fun setCheckbox(
    vm: PinViewModel,
    pin: Pin,
) {
    val pinListState = vm.pinListLD.observeAsState(emptyList())
    val isChecked = pinListState.value.isContainsPin(pin)
    Checkbox(
        enabled = pin.isEnabled(),
        checkMarkColor = MaterialTheme.colors.secondary,
        checked = isChecked,
        onCheckedChange = { vm.pinChecked(it, pin.pinNo) }
    )
}