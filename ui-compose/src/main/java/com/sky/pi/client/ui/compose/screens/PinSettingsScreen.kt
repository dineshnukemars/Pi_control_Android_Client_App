package com.sky.pi.client.ui.compose.screens


import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.models.Operation
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.libs.utils.fullDescription

private val pinListWidth = 160.dp

@Composable
fun PinSettingsScreen(vm: PinViewModel) {
    ConstraintLayout(modifier = Modifier.padding(2.dp)) {
        val selectedPinState = remember { mutableStateOf(-1) }
        val (pinListRef, dividerRef, pinSettingsRef) = createRefs()
        val pinListState: State<List<Pin>> = vm.pinListLD.observeAsState(emptyList())
        showDivider(dividerRef)
        showPinList(pinListRef, dividerRef, pinListState, selectedPinState)
        showPinConfig(pinSettingsRef, dividerRef, vm, pinListState, selectedPinState)
    }
}

@Composable
private fun ConstraintLayoutScope.showDivider(dividerRef: ConstrainedLayoutReference) {
    Box(modifier = Modifier.width(2.dp).fillMaxHeight().constrainAs(dividerRef) {
        start.linkTo(parent.start, margin = pinListWidth)
    }, backgroundColor = Color.Yellow)
}

@Composable
private fun ConstraintLayoutScope.showPinList(
    pinListRef: ConstrainedLayoutReference,
    dividerRef: ConstrainedLayoutReference,
    pinListState: State<List<Pin>>,
    selectedPinState: MutableState<Int>
) {

    LazyColumnFor(
        modifier = Modifier.preferredWidth(pinListWidth).constrainAs(pinListRef) {
            start.linkTo(parent.start)
            end.linkTo(dividerRef.start)
        }, items = pinListState.value
    ) {
        TextButton(
            contentColor = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                selectedPinState.value = it.pinNo
            }
        ) {
            Text(text = it.fullDescription())
        }
    }
}

@Composable
private fun ConstraintLayoutScope.showPinConfig(
    pinSettingsRef: ConstrainedLayoutReference,
    dividerRef: ConstrainedLayoutReference,
    vm: PinViewModel,
    pinListState: State<List<Pin>>,
    selectedPinState: MutableState<Int>
) {
    Column(modifier = Modifier.constrainAs(pinSettingsRef) {
        start.linkTo(dividerRef.end, margin = 5.dp)
    }) {
        val pin = pinListState.value.find { it.pinNo == selectedPinState.value } ?: return@Column
        Column {
            pinOperationRadioBtn(pin, vm, Operation.BLINK())
            pinOperationRadioBtn(pin, vm, Operation.SWITCH())
            pinOperationRadioBtn(pin, vm, Operation.PWM())
            pinOperationRadioBtn(pin, vm, Operation.INPUT())
        }
    }
}

@Composable
private fun pinOperationRadioBtn(pin: Pin, radioState: PinViewModel, operationType: Operation) {
    Row {
        RadioButton(selected = pin.operation == operationType, onClick = {
            radioState.updatePin(operation = operationType, pinNo = pin.pinNo)
        })
        Text(operationType.getName())
    }
}