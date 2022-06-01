package com.simprok.simprokcore.android.sample.storage

import android.content.SharedPreferences
import com.simprok.simprokcore.MachineLayerType
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokcore.android.sample.AppEvent
import com.simprok.simprokcore.android.sample.AppState
import com.simprok.simprokmachine.machines.Machine

class StorageLayer(
    private val prefs: SharedPreferences
) : MachineLayerType<AppState, AppEvent, StorageLayerState, StorageLayerEvent> {

    override val machine: Machine<StorageLayerState, StorageLayerEvent>
        get() = StorageLayerMachine(prefs)

    override fun mapState(state: AppState): StorageLayerState = StorageLayerState(state.value)

    override fun mapEvent(event: StorageLayerEvent): AppEvent = AppEvent.StorageLayerEvent(event.value)
}