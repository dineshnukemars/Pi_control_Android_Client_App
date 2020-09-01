package com.sky.pi.client.ui.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.logD
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.ui.compose.ui.TextStyles
import com.sky.pi.client.ui.compose.ui.landingPage
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingPage : AppCompatActivity() {
    private val viewModel by viewModel<PinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { initScreen(viewModel) }
    }
}

@Composable
fun initScreen(viewModel: PinViewModel) {
    landingPage(darkTheme = false) {
        Scaffold(
            topBar = { topBar() },
            bodyContent = { bodyContent(viewModel) }
        )
    }
}

@Composable
private fun topBar() = TopAppBar(
    title = { Text(text = "Raspi Layout", style = TextStyles.topBarTitle) },
    actions = {
        IconButton(
            onClick = { logD("info clicked") },
            icon = { Icon(Icons.Filled.Info) }
        )
    }
)

@Composable
fun bodyContent(vm: PinViewModel) = Row(Modifier.padding(4.dp)) {
    listCheckboxesOnColumn(vm.leftPinListLD, vm)
    listCheckboxesOnColumn(vm.rightPinListLD, vm)
}

@Composable
private fun listCheckboxesOnColumn(
    pinList: List<Pin>,
    vm: PinViewModel,
) = Column(
    verticalArrangement = Arrangement.SpaceEvenly,
    modifier = Modifier.fillMaxHeight().weight(1f).padding(2.dp),
    horizontalGravity = Alignment.Start
) {
    pinList.forEach { itemView(it, vm) }
}

@Composable
private fun itemView(
    pin: Pin,
    vm: PinViewModel,
) = Row(verticalGravity = Alignment.CenterVertically) {
    Text(text = pin.pinNo.to2DigitString(), style = TextStyles.contentSubtitle1)
    setCheckbox(vm, pin)
    Text(text = pin.description(), style = TextStyles.contentSubtitle2)
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