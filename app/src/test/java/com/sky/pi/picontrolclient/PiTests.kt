package com.sky.pi.picontrolclient

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.sky.pi.picontrolclient.repo.FakePiRepoImpl
import com.sky.pi.picontrolclient.repo.PinRepoImpl
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import com.sky.pi.repo.models.Operation
import com.sky.pi.repo.models.PinLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


@ExperimentalCoroutinesApi
internal class PiTests {

    @get:Rule
    val exceptedException: ExpectedException = ExpectedException.none()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PinViewModel

    private val pinLayout: PinLayout = PinLayout()
    private val piRepo = FakePiRepoImpl()

    @Before
    fun setup() {
        viewModel = PinViewModel(
            piRepo = piRepo,
            pinRepo = PinRepoImpl(pinLayout)
        )
    }

    @After
    fun tearDown() {
        viewModel.disconnectServer()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `add pin then added pin to list`() {
        val pinNo = 6
        viewModel.addPin(pinNo)
        add1To5Pins()
        val pin = pinLayout.pinForPinNo(pinNo)
        val pinList = viewModel.pinListLD.value

        assertEquals(6, pinList?.size)
        assertEquals(pin, pinList?.find { it.pinNo == pinNo })
    }

    @Test
    fun `add duplicate pin then throws Error`() {
        exceptedException.expect(Error::class.java)

        add1To5Pins()
        viewModel.addPin(1)
    }


    @Test
    fun `delete pin then removed pin from list`() {
        val pinNo = 6
        viewModel.addPin(pinNo)
        add1To5Pins()
        viewModel.deletePin(pinNo)
        val pinList = viewModel.pinListLD.value

        assertEquals(5, pinList?.size)
        assertEquals(null, pinList?.find { it.pinNo == pinNo })
    }

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

    @Test
    fun `change blink progress, then Operation Blink high time is progress`() {

    }

    @Test
    fun `change blink selector value, then Operation Blink Wave Period is progress`() {

    }

    private fun add1To5Pins() {
        viewModel.addPin(1)
        viewModel.addPin(2)
        viewModel.addPin(3)
        viewModel.addPin(4)
        viewModel.addPin(5)
    }
}