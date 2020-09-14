package com.sky.pi.client.ui.compose.screens


import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.utils.fullDescription

@Composable
fun PinSettingsScreen(vm: PinViewModel) {
    Row {
        val selectedPinState = remember { mutableStateOf(-1) }
        showPinList(vm, selectedPinState)
        Box(modifier = Modifier.width(1.dp).fillMaxHeight(), backgroundColor = Color.Yellow)
        showPinConfig(vm, selectedPinState)
    }
}

@Composable
private fun showPinList(vm: PinViewModel, selectedPinState: MutableState<Int>) {
    val pinListState = vm.pinListLD.observeAsState(emptyList())
    LazyColumnFor(pinListState.value) {
        TextButton(
            contentColor = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(10.dp),
            onClick = {
                selectedPinState.value = it.pinNo
            }
        ) {
            Text(text = it.fullDescription())
        }
    }
}

@Composable
private fun showPinConfig(vm: PinViewModel, selectedPinState: MutableState<Int>) {
    val value = selectedPinState.value
    if (value != -1) {
        Text(text = "$value")
    }
}