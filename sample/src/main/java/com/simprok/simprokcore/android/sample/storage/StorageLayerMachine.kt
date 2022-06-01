package com.simprok.simprokcore.android.sample.storage

import android.content.SharedPreferences
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.machines.ChildMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StorageLayerMachine(
    private val prefs: SharedPreferences
) : ChildMachine<StorageLayerState, StorageLayerEvent> {

    private val key = "storage"

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun process(input: StorageLayerState?, callback: Handler<StorageLayerEvent>) {
        if (input != null) {
            prefs.edit().putInt(key, input.value).apply()
        } else {
            callback(StorageLayerEvent(prefs.getInt(key, 0)))
        }
    }
}