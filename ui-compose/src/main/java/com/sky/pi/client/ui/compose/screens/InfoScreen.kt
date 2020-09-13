package com.sky.pi.client.ui.compose.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sky.pi.client.controller.viewmodels.InfoData
import com.sky.pi.client.controller.viewmodels.InfoViewModel


@Composable
fun InfoScreen(infoViewModel: InfoViewModel) {

    populateList(modifier = Modifier, infoViewModel.infocategories)
}

@Composable
private fun populateList(modifier: Modifier, list: List<InfoData>) {
    LazyColumnFor(list) {
        when (it) {
            is InfoData.InfoCategory -> {
                val expandState = remember { mutableStateOf(false) }
                val isExpanded = expandState.value
                Button(modifier = modifier, onClick = {
                    expandState.value = !isExpanded
                }) {
                    Text("cat - ${it.title}")
                }
                if (isExpanded) {
                    populateList(modifier.padding(start = 10.dp), it.list)
                }
            }
            is InfoData.InfoDescription -> Text(
                modifier = modifier,
                text = "${it.title} ${it.description}"
            )
        }
    }
}