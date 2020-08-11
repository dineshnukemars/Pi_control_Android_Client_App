package com.sky.pi.repo.impl

import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.Pin

fun findElseThrow(list: List<Pin>, pinNo: Int): Pin =
    list.find { it.pinNo == pinNo } ?: throw Error("WTF")

fun addPinToList(boardPinList: List<Pin>, list: List<Pin>, pinNo: Int): List<Pin> {
    ifAlreadyExistThenThrow(list, pinNo)
    val mutableList = list.toMutableList()
    mutableList.add(findElseThrow(boardPinList, pinNo))
    return mutableList
}

fun deletePinFromList(list: List<Pin>, pinNo: Int): List<Pin> =
    list.asSequence().filterNot { it.pinNo == pinNo }.toList()

fun updatePinOnList(list: List<Pin>, pinNo: Int, operation: Operation): List<Pin> =
    list.asSequence()
        .findAndUpdate(
            newItem = findElseThrow(list, pinNo).copy(operation = operation),
            predicate = { it.pinNo == pinNo })
        .toList()

fun ifAlreadyExistThenThrow(
    list: List<Pin>,
    pinNo: Int
) {
    val pin = list.find { it.pinNo == pinNo }
    if (pin != null) throw Error("WTF")
}