package com.sky.pi.client.ui.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.libs.logD
import com.sky.pi.client.libs.models.Pin
import com.sky.pi.client.ui.compose.ui.Styles
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
            bodyContent = { bodyContent(viewModel) },
            bottomBar = { bottomBar() }
        )
    }
}

@Composable
private fun bottomBar() = BottomAppBar(cutoutShape = RoundedCornerShape(20.dp)) {
    IconButton(
        onClick = { logD("info clicked") },
        icon = { Icon(Icons.Filled.Info) }
    )
}

@Composable
private fun topBar() = TopAppBar(
    title = { Text(text = "Raspi Layout", style = Styles.topBarTitle) },
    actions = {
        IconButton(
            onClick = { logD("info clicked") },
            icon = { Icon(Icons.Filled.Info) }
        )
    }
)

@Composable
fun bodyContent(vm: PinViewModel) = Row {
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