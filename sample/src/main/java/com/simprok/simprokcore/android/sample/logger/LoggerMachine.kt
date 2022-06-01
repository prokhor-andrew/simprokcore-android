package com.simprok.simprokcore.android.sample.logger

import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.machines.ChildMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO

class LoggerMachine: ChildMachine<String, Nothing> {

    override val dispatcher: CoroutineDispatcher
        get() = IO

    override fun process(input: String?, callback: Handler<Nothing>) {
        println(input ?: "loading")
    }
}