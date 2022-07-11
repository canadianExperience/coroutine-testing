package com.me.coroutine_testing.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NumberCruncher(
    private val coroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) {

    private val _resultsShared = MutableSharedFlow<Int>()
    val resultsShared get() = _resultsShared.asSharedFlow()

    private val _resultsState = MutableStateFlow(-1)
    val resultsState get() = _resultsState.asStateFlow()

    fun calculateShared() {

        coroutineScope.launch(dispatchers.default()) {
            val result = longRunningOperation()

            // We've added a 5s delay here to make testing even harder.
            // Ideally, production code would respect this delay, but unit tests would not it will slow down your test suite.
            delay(5_000)

            _resultsShared.emit(result)
        }
    }

    fun calculateState() {

        coroutineScope.launch(dispatchers.default()) {
            val result = longRunningOperation()

            // We've added a 5s delay here to make testing even harder.
            // Ideally, production code would respect this delay, but unit tests would not it will slow down your test suite.
            delay(5_000)

            addState(result)
        }
    }

    suspend fun addState(result: Int) {
        _resultsState.emit(result)
    }


    suspend fun getResult(): Int {
        return withContext(dispatchers.default()){
            //Always returns 0
            longRunningOperation()
        }
    }

    private fun longRunningOperation(): Int {
        //Some long running operation
        val list = mutableListOf<Int>()

        for (i in 0..1_000) {
            list.add(i)
        }
        for (i in 0..20_000) {
            list.shuffle()
            list.sort()
        }

        return list.first()
    }
}