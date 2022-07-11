package com.me.coroutine_testing

import com.me.coroutine_testing.data.NumberCruncher
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class NumberCruncherTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @Test
    fun `test single coroutine`(){
        runTest {
            val cruncher = NumberCruncher(
                this,
                coroutineTestRule.testDispatcherProvider
            )

            assertEquals(0, cruncher.getResult())
        }
    }

    @Test
    fun `test shared flow`() {
        runTest {
            val cruncher = NumberCruncher(
                this,
                coroutineTestRule.testDispatcherProvider
            )

            cruncher.calculateShared()
            assertEquals(0, cruncher.resultsShared.first())
        }
    }

    @Test
    fun `test state flow`() {
        runTest {
            val cruncher = NumberCruncher(
                this,
                coroutineTestRule.testDispatcherProvider
            )

            val results = mutableListOf<Int>()

            val job = launch(coroutineTestRule.testDispatcher) { cruncher.resultsState.toList(results) }

            cruncher.addState(1)
            cruncher.addState(3)
            cruncher.addState(5)

            runCurrent()

            // -1 Default value

            assertEquals(listOf(-1,1,3,5), results)

            job.cancel()
        }
    }
}