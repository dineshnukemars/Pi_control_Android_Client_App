package com.sky.pi.client.ui.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.ui.compose.ui.landingPage
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingPage : AppCompatActivity() {

    private val viewModel by viewModel<PinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initUI(viewModel)
        }
    }

}

@Composable
fun initUI(viewModel: PinViewModel) {
    landingPage {
        Scaffold(topBar = {
            Text(
                text = "Raspi Layout",
                style = MaterialTheme.typography.h4
            )
        }) {
            mainContent(viewModel)
        }
    }
}

@Composable
fun mainContent(vm: PinViewModel) {
    val pinListState: State<List<Pin>> = vm.pinListLD.observeAsState(emptyList())
    val leftPins = vm.getBoardPins().asSequence().filter { it.isLeft }.toList()
    val rightPins = vm.getBoardPins().asSequence().filter { !it.isLeft }.toList()

    Row {
        populateCheckboxes(leftPins, pinListState, vm)
        populateCheckboxes(rightPins, pinListState, vm)
    }
}

@Composable
private fun populateCheckboxes(
    leftPins: List<Pin>,
    observeAsState: State<List<Pin>>,
    vm: PinViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxHeight().weight(1f).padding(4.dp),
        horizontalGravity = Alignment.Start
    ) {
        leftPins.forEach { pin ->
            val isChecked = observeAsState.value.find { it.pinNo == pin.pinNo } != null
            itemView(pin, isChecked, vm)
        }
    }
}

@Composable
private fun itemView(
    pin: Pin,
    isChecked: Boolean,
    vm: PinViewModel
) {
    Row(verticalGravity = Alignment.CenterVertically) {
        Text(text = "${pin.pinNo}", style = MaterialTheme.typography.subtitle2)
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                if (it) vm.addPin(pinNo = pin.pinNo)
                else vm.deletePin(pinNo = pin.pinNo)
            })
        Text(text = pinDescription(pin), fontSize = 12.sp, style = MaterialTheme.typography.body2)
    }
}