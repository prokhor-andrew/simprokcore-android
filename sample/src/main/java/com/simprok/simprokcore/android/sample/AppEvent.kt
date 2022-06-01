package com.simprok.simprokcore.android.sample

sealed interface AppEvent {

    object UILayerEvent : AppEvent

    data class StorageLayerEvent(val value: Int) : AppEvent
}