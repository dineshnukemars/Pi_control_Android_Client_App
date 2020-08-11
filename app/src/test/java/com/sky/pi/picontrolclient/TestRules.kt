package com.sky.pi.picontrolclient

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler

@ExperimentalCoroutinesApi
open class MainCoroutineRule : AfterEachCallback, BeforeEachCallback {
    private var dispatcher: TestCoroutineDispatcher? = null

    override fun beforeEach(context: ExtensionContext?) {
        dispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(dispatcher!!)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        dispatcher?.cleanupTestCoroutines()
        dispatcher = null
    }

    operator fun invoke(block: suspend TestCoroutineScope.() -> Unit) {
        dispatcher?.runBlockingTest(block)
    }
}

class LiveDataTestRule : AfterEachCallback, BeforeEachCallback, TestExecutionExceptionHandler {
    private val errorList = ArrayList<Throwable>()
    private val taskExecutor = object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable): Unit = runnable.run()
        override fun postToMainThread(runnable: Runnable): Unit = runnable.run()
        override fun isMainThread(): Boolean = true
    }

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(taskExecutor)
    }

    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable) {
        errorList.add(throwable)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        val errorListCopy = ArrayList(errorList)
        errorList.clear()

        assertTrue(errorListCopy.isEmpty()) {
            errorListCopy
                .asSequence()
                .mapTo(mutableListOf(), Throwable::toString)
                .joinToString { it }
        }
    }
}