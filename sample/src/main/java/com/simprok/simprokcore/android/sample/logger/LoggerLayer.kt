package com.simprok.simprokcore.android.sample.logger

import com.simprok.simprokcore.ConsumerLayerType
import com.simprok.simprokcore.android.sample.AppState
import com.simprok.simprokmachine.api.BasicMachine
import com.simprok.simprokmachine.machines.Machine
import kotlinx.coroutines.Dispatchers

class LoggerLayer : ConsumerLayerType<AppState, String, Nothing> {

    override val machine: Machine<String, Nothing>
        get() = BasicMachine(Dispatchers.IO) { input, _ ->
            println(input)
        }

    override fun map(state: AppState): String = "${state.value}"
}