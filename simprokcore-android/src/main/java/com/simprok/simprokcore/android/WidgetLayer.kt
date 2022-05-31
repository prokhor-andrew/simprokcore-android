//
//  WidgetLayer.kt
//  simprokcore-android
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokcore.android

import com.simprok.simprokandroid.WidgetMachine
import com.simprok.simprokcore.Layer
import com.simprok.simprokcore.MachineLayerObject
import com.simprok.simprokmachine.api.Mapper

sealed interface WidgetLayer<Event, State> {

    val layer: Layer<Event, State>

    interface Type<GlobalEvent, GlobalState, State, Event> : WidgetLayer<GlobalEvent, GlobalState> {

        val machine: WidgetMachine<State, Event>

        fun mapState(state: GlobalState): State

        fun mapEvent(event: Event): GlobalEvent

        override val layer: Layer<GlobalEvent, GlobalState>
            get() = get(machine, ::mapState, ::mapEvent)
    }

    data class Object<GlobalEvent, GlobalState, State, Event>(
        private val machine: WidgetMachine<State, Event>,
        private val stateMapper: Mapper<GlobalState, State>,
        private val eventMapper: Mapper<Event, GlobalEvent>
    ) : WidgetLayer<GlobalEvent, GlobalState> {

        override val layer: Layer<GlobalEvent, GlobalState>
            get() = get(machine, stateMapper, eventMapper)
    }
}


private fun <GlobalEvent, GlobalState, State, Event> get(
    machine: WidgetMachine<State, Event>,
    stateMapper: Mapper<GlobalState, State>,
    eventMapper: Mapper<Event, GlobalEvent>
): Layer<GlobalEvent, GlobalState> = MachineLayerObject(
    machine.machine,
    stateMapper,
    eventMapper
)