package com.sky.pi.picontrolclient

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.internal.AssumptionViolatedException
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.junit.runners.model.MultipleFailureException

@ExperimentalCoroutinesApi
open class MainCoroutineRule : AfterEachCallback, BeforeEachCallback {
    private var _testDispatcher: TestCoroutineDispatcher? = null
    private val testDispatcher: TestCoroutineDispatcher
        get() = _testDispatcher ?: error("dispatcher is not initiated")

    override fun beforeEach(context: ExtensionContext?) {
        _testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        _testDispatcher = null
    }

    operator fun invoke(block: suspend TestCoroutineScope.() -> Unit) {
        testDispatcher.runBlockingTest(block)
    }
}

class LiveDataTestRule : AfterEachCallback, BeforeEachCallback, TestExecutionExceptionHandler {

    private val errorList = ArrayList<Throwable>()

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable): Unit = runnable.run()

                override fun postToMainThread(runnable: Runnable): Unit = runnable.run()

                override fun isMainThread(): Boolean = true
            })
    }

    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {
        when (throwable) {
            is AssumptionViolatedException -> {
                errorList.add(throwable)
                println("skipped quietly $errorList $throwable")
            }
            is Throwable -> {
                errorList.add(throwable)
                println("failed quietly $errorList $throwable")
            }
        }
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        val errorListCopy = ArrayList(errorList)
        errorList.clear()
        MultipleFailureException.assertEmpty(errorListCopy)
    }
}