package com.sky.pi.repo.pirepo.entities

import com.sky.backend.grpc.pi.BoardInfoResponse

data class BoardInfo(
    val make: String,
    val model: String,
    val memory: Int,
    val libraryPath: String,
    val adcVRef: Float
) {

    constructor(response: BoardInfoResponse) : this(
        make = response.make,
        model = response.model,
        memory = response.memory,
        libraryPath = response.libraryPath,
        adcVRef = response.adcVRef
    )
}