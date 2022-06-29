//
//  ComponentLayer.kt
//  simprokcore
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.


package com.simprok.simprokcore.android

import com.simprok.simprokmachine.android.ComponentMachine
import com.simprok.simprokmachine.android.inward
import com.simprok.simprokmachine.android.outward
import com.simprok.simprokmachine.api.Mapper
import com.simprok.simprokmachine.api.Ward


/**
 * A general interface that describes a type that represents a layer object.
 */
sealed interface ComponentLayer<GlobalState, GlobalEvent> {

    val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
}

interface ComponentMachineLayerType<GlobalState, GlobalEvent, State, Event> :
    ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<State, Event, StateAction<GlobalState, GlobalEvent>> {
            Ward.set(
                StateAction.WillUpdate(mapEvent(it))
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<GlobalState, GlobalEvent> -> Ward.set()
                is StateAction.DidUpdate<GlobalState, GlobalEvent> -> Ward.set(
                    mapState(it.state)
                )
            }
        }

    val machine: ComponentMachine<State, Event>

    fun mapState(state: GlobalState): State

    fun mapEvent(event: Event): GlobalEvent
}

data class ComponentMachineLayerObject<GlobalState, GlobalEvent, State, Event>(
    override val machine: ComponentMachine<State, Event>,
    private val stateMapper: Mapper<GlobalState, State>,
    private val eventMapper: Mapper<Event, GlobalEvent>
) : ComponentMachineLayerType<GlobalState, GlobalEvent, State, Event> {

    override fun mapState(state: GlobalState): State = stateMapper(state)

    override fun mapEvent(event: Event): GlobalEvent = eventMapper(event)
}

interface ComponentConsumerLayerType<GlobalState, GlobalEvent, State, Event> :
    ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<State, Event, StateAction<GlobalState, GlobalEvent>> {
            Ward.set()
        }.inward {
            when (it) {
                is StateAction.WillUpdate<GlobalState, GlobalEvent> -> Ward.set()
                is StateAction.DidUpdate<GlobalState, GlobalEvent> -> Ward.set(
                    map(it.state)
                )
            }
        }

    val machine: ComponentMachine<State, Event>

    fun map(state: GlobalState): State
}

data class ComponentConsumerLayerObject<GlobalState, GlobalEvent, State, Event>(
    override val machine: ComponentMachine<State, Event>,
    private val mapper: Mapper<GlobalState, State>
) : ComponentConsumerLayerType<GlobalState, GlobalEvent, State, Event> {

    override fun map(state: GlobalState): State = mapper(state)
}


interface ComponentProducerLayerType<GlobalState, GlobalEvent, State, Event> :
    ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<State, Event, StateAction<GlobalState, GlobalEvent>> {
            Ward.set(
                StateAction.WillUpdate(map(it))
            )
        }.inward {
            Ward.set()
        }

    val machine: ComponentMachine<State, Event>

    fun map(event: Event): GlobalEvent
}


data class ComponentProducerLayerObject<GlobalState, GlobalEvent, State, Event>(
    override val machine: ComponentMachine<State, Event>,
    private val mapper: Mapper<Event, GlobalEvent>
) : ComponentProducerLayerType<GlobalState, GlobalEvent, State, Event> {

    override fun map(event: Event): GlobalEvent = mapper(event)
}


interface ComponentMapEventLayerType<GlobalState, GlobalEvent, Event> :
    ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<GlobalState, Event, StateAction<GlobalState, GlobalEvent>> {
            Ward.set(
                StateAction.WillUpdate(map(it))
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<GlobalState, GlobalEvent> -> Ward.set()
                is StateAction.DidUpdate<GlobalState, GlobalEvent> -> Ward.set(it.state)
            }
        }

    val machine: ComponentMachine<GlobalState, Event>

    fun map(event: Event): GlobalEvent
}

data class ComponentMapEventLayerObject<GlobalState, GlobalEvent, Event>(
    override val machine: ComponentMachine<GlobalState, Event>,
    private val mapper: Mapper<Event, GlobalEvent>
) : ComponentMapEventLayerType<GlobalState, GlobalEvent, Event> {

    override fun map(event: Event): GlobalEvent = mapper(event)
}

interface ComponentMapStateLayerType<GlobalState, GlobalEvent, State> :
    ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<State, GlobalEvent, StateAction<GlobalState, GlobalEvent>> {
            Ward.set(
                StateAction.WillUpdate(it)
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<GlobalState, GlobalEvent> -> Ward.set()
                is StateAction.DidUpdate<GlobalState, GlobalEvent> -> Ward.set(
                    map(it.state)
                )
            }
        }

    val machine: ComponentMachine<State, GlobalEvent>

    fun map(state: GlobalState): State
}

data class MapStateLayerObject<GlobalState, GlobalEvent, State>(
    override val machine: ComponentMachine<State, GlobalEvent>,
    private val mapper: Mapper<GlobalState, State>
) : ComponentMapStateLayerType<GlobalState, GlobalEvent, State> {

    override fun map(state: GlobalState): State = mapper(state)
}

interface NoMapLayerType<GlobalState, GlobalEvent> : ComponentLayer<GlobalState, GlobalEvent> {

    override val child: ComponentMachine<StateAction<GlobalState, GlobalEvent>, StateAction<GlobalState, GlobalEvent>>
        get() = machine.outward<GlobalState, GlobalEvent, StateAction<GlobalState, GlobalEvent>> {
            Ward.set(
                StateAction.WillUpdate(it)
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<GlobalState, GlobalEvent> -> Ward.set()
                is StateAction.DidUpdate<GlobalState, GlobalEvent> -> Ward.set(it.state)
            }
        }

    val machine: ComponentMachine<GlobalState, GlobalEvent>
}

data class ComponentNoMapLayerObject<GlobalState, GlobalEvent>(
    override val machine: ComponentMachine<GlobalState, GlobalEvent>
) : NoMapLayerType<GlobalState, GlobalEvent>