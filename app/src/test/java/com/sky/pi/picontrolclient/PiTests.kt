package com.sky.pi.picontrolclient

import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.impl.FakePiRepoImpl
import com.sky.pi.repo.impl.PinRepoImpl
import com.sky.pi.repo.impl.pi4bPinList
import com.sky.pi.repo.models.Operation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@ExtendWith(MainCoroutineRule::class, LiveDataTestRule::class)
internal class PiTests {

    private lateinit var viewModel: PinViewModel

    private val piRepo = FakePiRepoImpl()

    @BeforeEach
    fun setup() {
        viewModel = PinViewModel(
            piRepo = piRepo,
            pinRepo = PinRepoImpl(pi4bPinList)
        )
    }

    @AfterEach
    fun tearDown() {
        viewModel.disconnectServer()
        viewModel.viewModelScope.cancel()
    }

    @Nested
    @DisplayName("Add/Remove pin from list")
    inner class ModifyPinList {

        @Test
        fun `add pin`() {
            val pinNo = 6
            add1To5Pins()
            viewModel.addPin(pinNo)
            val pinList = viewModel.pinListLD.value

            assertEquals(6, pinList?.size)
            assertEquals(pi4bPinList.find { it.pinNo == pinNo }, pinList?.find { it.pinNo == pinNo })
        }

        @Test
        fun `add duplicate pin throws Error`() {
            Assertions.assertThrows(Error::class.java) {
                viewModel.addPin(1)
                viewModel.addPin(1)
            }
        }

        @Test
        fun `delete pin`() {
            val pinNo = 6
            viewModel.addPin(pinNo)
            add1To5Pins()
            viewModel.deletePin(pinNo)
            val pinList = viewModel.pinListLD.value

            assertEquals(5, pinList?.size)
            assertEquals(null, pinList?.find { it.pinNo == pinNo })
        }

        private fun add1To5Pins() {
            viewModel.addPin(1)
            viewModel.addPin(2)
            viewModel.addPin(3)
            viewModel.addPin(4)
            viewModel.addPin(5)
        }
    }


    @Nested
    @DisplayName("Change Pin-operation value")
    inner class PinOperations {

        @Test
        fun `change Switch pin ON & OFF, then Operation Switch is ON & OFF`() {
            val pinNo = 6
            viewModel.addPin(pinNo)
            piRepo.commandSuccess = true
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))

            piRepo.commandSuccess = false
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(false))
            val operation = viewModel.pinListLD.value?.find { it.pinNo == pinNo }?.operation

            assertEquals(Operation.SWITCH(true), operation)
        }

        @Test
        fun `Command success False and change Switch pin ON & OFF, then Operation Switch is ON & OFF`() {
            val pinNo = 6
            viewModel.addPin(pinNo)
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))

            piRepo.commandSuccess = false
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(false))
            val operation1 = viewModel.pinListLD.value?.find { it.pinNo == pinNo }?.operation

            assertEquals(Operation.SWITCH(true), operation1)

            piRepo.commandSuccess = true
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(false))


            piRepo.commandSuccess = false
            viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))
            val operation2 = viewModel.pinListLD.value?.find { it.pinNo == pinNo }?.operation

            assertEquals(Operation.SWITCH(false), operation2)
        }

        @Test
        fun `change PWM progress, then Operation PWM duty cycle is progress`() {
            val pinNo = 6
            val dutyCycle = 0.3f
            viewModel.addPin(pinNo)
            viewModel.updatePin(pinNo = pinNo, operation = Operation.PWM(dutyCycle = dutyCycle))
            val operation = viewModel.pinListLD.value?.find { it.pinNo == pinNo }?.operation

            assertEquals(Operation.PWM(dutyCycle = dutyCycle), operation)
        }
    }
}