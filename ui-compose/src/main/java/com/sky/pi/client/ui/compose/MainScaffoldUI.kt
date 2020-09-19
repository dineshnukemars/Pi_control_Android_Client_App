package com.sky.pi.client.ui.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.SettingsInputHdmi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.sky.pi.client.controller.viewmodels.InfoViewModel
import com.sky.pi.client.controller.viewmodels.PinViewModel
import com.sky.pi.client.ui.compose.screens.AppSettingsScreen
import com.sky.pi.client.ui.compose.screens.BoardLayoutScreen
import com.sky.pi.client.ui.compose.screens.InfoScreen
import com.sky.pi.client.ui.compose.screens.PinSettingsScreen
import com.sky.pi.client.ui.compose.ui.Styles
import com.sky.pi.client.ui.compose.ui.landingPage
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScaffoldUI : AppCompatActivity() {
    private val pinViewModel by viewModel<PinViewModel>()
    private val infoViewModel by viewModel<InfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { initScreen() }
    }

    @Composable
    private fun initScreen() {
        landingPage(darkTheme = false) {
            val screenState = remember { mutableStateOf(CurrentScreen.BoardLayout) }
            Scaffold(
                topBar = { topBar(screenState) },
                bodyContent = { bodyContent(screenState) },
                bottomBar = { bottomBar(screenState) }
            )
        }
    }

    @Composable
    private fun bodyContent(screenState: MutableState<CurrentScreen>) =
        when (screenState.value) {
            CurrentScreen.BoardLayout -> BoardLayoutScreen(pinViewModel)
            CurrentScreen.PinSettings -> PinSettingsScreen(pinViewModel)
            CurrentScreen.Help -> InfoScreen(infoViewModel)
            CurrentScreen.AppSettings -> AppSettingsScreen()
        }

    @Composable
    private fun topBar(screenState: MutableState<CurrentScreen>) = TopAppBar(
        title = { Text(text = screenState.value.screenName, style = Styles.topBarTitle) }
    )

    @Composable
    private fun bottomBar(screenState: MutableState<CurrentScreen>) =
        BottomAppBar {
            val modifier = Modifier.weight(1f)
            IconButton(
                onClick = { screenState.value = CurrentScreen.BoardLayout },
                icon = { Icon(Icons.Filled.SelectAll) },
                modifier = modifier
            )
            IconButton(
                onClick = { screenState.value = CurrentScreen.PinSettings },
                icon = { Icon(Icons.Filled.SettingsInputHdmi) },
                modifier = modifier
            )
            IconButton(
                onClick = { screenState.value = CurrentScreen.Help },
                icon = { Icon(Icons.Filled.HelpCenter) },
                modifier = modifier
            )
            IconButton(
                onClick = { screenState.value = CurrentScreen.AppSettings },
                icon = { Icon(Icons.Filled.SettingsApplications) },
                modifier = modifier
            )
        }

    private enum class CurrentScreen(val screenName: String) {
        BoardLayout("Board Layout"),
        PinSettings("Pin Settings"),
        Help("Help"),
        AppSettings("App Settings")
    }
}