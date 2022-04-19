package com.simprok.simprokcore.android.sample.storage

import android.content.SharedPreferences
import com.simprok.simprokcore.MachineLayerType
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokcore.android.sample.AppState
import com.simprok.simprokmachine.machines.Machine

class StorageLayer(
    private val prefs: SharedPreferences
) : MachineLayerType<AppState, StorageLayerState, StorageLayerEvent> {

    override val machine: Machine<StorageLayerState, StorageLayerEvent>
        get() = StorageLayerMachine(prefs)

    override fun map(state: AppState): StorageLayerState = StorageLayerState(state.value)

    override fun reduce(state: AppState?, event: StorageLayerEvent): ReducerResult<AppState> =
        ReducerResult.Set(AppState(event.value))
}