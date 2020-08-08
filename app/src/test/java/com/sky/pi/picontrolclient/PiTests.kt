package com.sky.pi.picontrolclient

import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.impl.FakePiRepoImpl
import com.sky.pi.repo.impl.PinRepoImpl
import com.sky.pi.repo.impl.findPinElseThrowError
import com.sky.pi.repo.impl.pi4bPinList
import com.sky.pi.repo.models.Operation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension


@ExperimentalCoroutinesApi
@ExtendWith(LiveDataTestRule::class)
internal class PiTests {

    @JvmField
    @RegisterExtension
    val coroutineRule = MainCoroutineRule()

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
            val pinList = viewModel.pinListLD.value ?: throw Error()

            assertEquals(6, pinList.size)
            assertEquals(
                findPinElseThrowError(pi4bPinList, pinNo),
                findPinElseThrowError(pinList, pinNo)
            )
        }

        @Test
        fun `add duplicate pin throws Error`() {
            assertThrows(Error::class.java) {
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
            val pinList = viewModel.pinListLD.value ?: throw Error()

            assertEquals(5, pinList.size)
            assertThrows(Error::class.java) {
                findPinElseThrowError(pinList, pinNo)
            }
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
    @DisplayName("Change Pin Operation")
    inner class ChangePinType {
        val pinNo = 6

        private fun findPinOperationOnLiveData() =
            viewModel.pinListLD.value?.find { it.pinNo == pinNo }?.operation

        private fun actualToastMessage() =
            viewModel.toastLD.value

        @BeforeEach
        fun setup() {
            viewModel.addPin(pinNo)
            piRepo.commandSuccess = true
        }

        @Nested
        inner class ApiSuccess {
            @Test
            fun `None to Switch`() {
                val expected = Operation.SWITCH(true)
                viewModel.updatePin(pinNo = pinNo, operation = expected)

                assertEquals(expected, findPinOperationOnLiveData())
            }

            @Test
            fun `Switch to Pwm`() {

                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))

                val expected = Operation.PWM(1000)
                viewModel.updatePin(pinNo = pinNo, operation = expected)


                assertEquals(expected, findPinOperationOnLiveData())
            }

            @Test
            fun `Switch ON & OFF`() {
                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))
                assertEquals(
                    Operation.SWITCH(true),
                    findPinOperationOnLiveData()
                )

                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(false))
                assertEquals(
                    Operation.SWITCH(false),
                    findPinOperationOnLiveData()
                )
            }


            @Test
            fun `Pwm change dutyCycle`() {
                var expected = Operation.PWM(dutyCycle = 0.6f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)
                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )

                expected = Operation.PWM(dutyCycle = 0.15f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)
                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )

                expected = Operation.PWM(dutyCycle = 1f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)
                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )
            }

            @Test
            fun `Blink change values`() {
                var expected = Operation.BLINK(wavePeriod = 10, highTime = 0.6f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)
                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )

                expected = Operation.BLINK(wavePeriod = 5, highTime = 0.6f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)
                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )
            }

        }

        @Nested
        inner class ApiFailure {

            @Test
            fun `None to Switch`() {
                piRepo.commandSuccess = false
                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))

                assertEquals(Operation.NONE, findPinOperationOnLiveData())
                assertEquals("Something went Wrong", actualToastMessage())
            }

            @Test
            fun `Switch to Pwm`() {
                piRepo.commandSuccess = true
                val expected = Operation.SWITCH(true)
                viewModel.updatePin(pinNo = pinNo, operation = expected)

                piRepo.commandSuccess = false
                viewModel.updatePin(pinNo = pinNo, operation = Operation.PWM(1000))

                assertEquals(expected, findPinOperationOnLiveData())
            }

            @Test
            fun `Switch ON & OFF`() {
                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(true))
                assertEquals(
                    Operation.SWITCH(true),
                    findPinOperationOnLiveData()
                )

                viewModel.updatePin(pinNo = pinNo, operation = Operation.SWITCH(false))
                assertEquals(
                    Operation.SWITCH(false),
                    findPinOperationOnLiveData()
                )
            }


            @Test
            fun `Pwm change dutyCycle`() {
                piRepo.commandSuccess = true
                val expected = Operation.PWM(dutyCycle = 0.6f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)

                piRepo.commandSuccess = false
                viewModel.updatePin(pinNo = pinNo, operation = Operation.PWM(dutyCycle = 0.3f))

                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )
            }

            @Test
            fun `Blink change values`() {
                piRepo.commandSuccess = true
                val expected = Operation.BLINK(wavePeriod = 10, highTime = 0.6f)
                viewModel.updatePin(pinNo = pinNo, operation = expected)

                piRepo.commandSuccess = false
                viewModel.updatePin(
                    pinNo = pinNo,
                    operation = Operation.BLINK(wavePeriod = 5, highTime = 0.8f)
                )

                assertEquals(
                    expected,
                    findPinOperationOnLiveData()
                )
            }
        }
    }
}