package com.sky.pi.client.controller.viewmodels

import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {

    val infocategories: List<InfoData> = listOf(
        InfoData.InfoCategory(
            id = 22,
            title = "my cat1",
            list = listOf(
                InfoData.InfoDescription(33, "mytitle33", "mydescription33"),
                InfoData.InfoDescription(34, "mytitle34", "mydescription34"),
                InfoData.InfoDescription(35, "mytitle35", "mydescription35"),
                InfoData.InfoDescription(36, "mytitle36", "mydescription36"),
                InfoData.InfoCategory(
                    id = 223,
                    title = "my cat2",
                    list = listOf(
                        InfoData.InfoDescription(3, "mytitle3", "mydescription3"),
                        InfoData.InfoDescription(4, "mytitle4", "mydescription4"),
                        InfoData.InfoDescription(5, "mytitle5", "mydescription5"),
                        InfoData.InfoDescription(6, "mytitle6", "mydescription6"),
                    )
                )
            )
        ),
        InfoData.InfoDescription(43, "mytitle43", "mydescription43"),
        InfoData.InfoDescription(53, "mytitle53", "mydescription53"),
        InfoData.InfoDescription(63, "mytitle63", "mydescription63"),
        InfoData.InfoDescription(73, "mytitle73", "mydescription73"),
    )
}

sealed class InfoData {
    data class InfoCategory(val id: Int, val title: String, val list: List<InfoData>) : InfoData()
    data class InfoDescription(val id: Int, val title: String, val description: String) : InfoData()
}