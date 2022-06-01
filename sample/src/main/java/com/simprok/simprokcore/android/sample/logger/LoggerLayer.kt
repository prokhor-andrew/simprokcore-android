package com.simprok.simprokcore.android.sample.logger

import com.simprok.simprokcore.ConsumerLayerType
import com.simprok.simprokcore.android.sample.AppEvent
import com.simprok.simprokcore.android.sample.AppState
import com.simprok.simprokmachine.machines.Machine

class LoggerLayer : ConsumerLayerType<AppState, AppEvent, String, Nothing> {

    override val machine: Machine<String, Nothing>
        get() = LoggerMachine()

    override fun map(state: AppState): String = "${state.value}"
}