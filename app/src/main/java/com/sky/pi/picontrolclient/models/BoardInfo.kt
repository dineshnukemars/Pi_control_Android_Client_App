package com.sky.pi.picontrolclient.models

import com.sky.backend.grpc.pi.BoardInfoResponse

data class BoardInfo(
    val make: String,
    val model: String,
    val memory: Int,
    val libraryPath: String,
    val adcVRef: Float
) {

    constructor(boardInfoResponse: BoardInfoResponse) : this(
        make = boardInfoResponse.make,
        model = boardInfoResponse.model,
        memory = boardInfoResponse.memory,
        libraryPath = boardInfoResponse.libraryPath,
        adcVRef = boardInfoResponse.adcVRef
    )
}