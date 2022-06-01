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

sealed interface WidgetLayer<State, Event> {

    val layer: Layer<State, Event>

    interface Type<GlobalState, GlobalEvent, State, Event> : WidgetLayer<GlobalState, GlobalEvent> {

        val machine: WidgetMachine<State, Event>

        fun mapState(state: GlobalState): State

        fun mapEvent(event: Event): GlobalEvent

        override val layer: Layer<GlobalState, GlobalEvent>
            get() = get(machine, ::mapState, ::mapEvent)
    }

    data class Object<GlobalState, GlobalEvent, State, Event>(
        private val machine: WidgetMachine<State, Event>,
        private val stateMapper: Mapper<GlobalState, State>,
        private val eventMapper: Mapper<Event, GlobalEvent>
    ) : WidgetLayer<GlobalState, GlobalEvent> {

        override val layer: Layer<GlobalState, GlobalEvent>
            get() = get(machine, stateMapper, eventMapper)
    }
}


private fun <GlobalState, GlobalEvent, State, Event> get(
    machine: WidgetMachine<State, Event>,
    stateMapper: Mapper<GlobalState, State>,
    eventMapper: Mapper<Event, GlobalEvent>
): Layer<GlobalState, GlobalEvent> = MachineLayerObject(
    machine.machine,
    stateMapper,
    eventMapper
)