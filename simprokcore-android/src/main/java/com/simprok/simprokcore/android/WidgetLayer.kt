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
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokmachine.api.BiMapper
import com.simprok.simprokmachine.api.Mapper

sealed interface WidgetLayer<State> {

    val layer: Layer<State>

    interface Type<GlobalState, State, Event> : WidgetLayer<GlobalState> {

        val machine: WidgetMachine<State, Event>

        fun map(state: GlobalState): State

        fun reduce(state: GlobalState?, event: Event): ReducerResult<GlobalState>

        override val layer: Layer<GlobalState>
            get() = get(machine, ::map, ::reduce)
    }

    data class Object<GlobalState, State, Event>(
        private val machine: WidgetMachine<State, Event>,
        private val mapper: Mapper<GlobalState, State>,
        private val reducer: BiMapper<GlobalState?, Event, ReducerResult<GlobalState>>
    ) : WidgetLayer<GlobalState> {

        override val layer: Layer<GlobalState>
            get() = get(machine, mapper, reducer)
    }
}


private fun <GlobalState, State, Event> get(
    machine: WidgetMachine<State, Event>,
    mapper: Mapper<GlobalState, State>,
    reducer: BiMapper<GlobalState?, Event, ReducerResult<GlobalState>>
): Layer<GlobalState> = MachineLayerObject(
    machine.machine,
    mapper,
    reducer
)