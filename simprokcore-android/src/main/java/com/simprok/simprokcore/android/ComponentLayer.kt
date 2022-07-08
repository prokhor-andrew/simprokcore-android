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
sealed interface ComponentLayer<Event> {

    val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
}

interface ComponentMachineLayerType<Event, Input, Output> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward {
            Ward.set<StateAction<Event>>(
                StateAction.WillUpdate(mapOutput(it))
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<Event> -> Ward.set()
                is StateAction.DidUpdate<Event> -> Ward.set(
                    mapEvent(it.event)
                )
            }
        }

    val machine: ComponentMachine<Input, Output>

    fun mapEvent(event: Event): Input

    fun mapOutput(output: Output): Event
}

data class ComponentMachineLayerObject<Event, Input, Output>(
    override val machine: ComponentMachine<Input, Output>,
    private val stateMapper: Mapper<Event, Input>,
    private val eventMapper: Mapper<Output, Event>,
) : ComponentMachineLayerType<Event, Input, Output> {

    override fun mapEvent(event: Event): Input = stateMapper(event)

    override fun mapOutput(output: Output): Event = eventMapper(output)
}

interface ComponentConsumerLayerType<Event, Input, Output> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward<Input, Output, StateAction<Event>> {
            Ward.set()
        }.inward {
            when (it) {
                is StateAction.WillUpdate<Event> -> Ward.set()
                is StateAction.DidUpdate<Event> -> Ward.set(
                    map(it.event)
                )
            }
        }

    val machine: ComponentMachine<Input, Output>

    fun map(event: Event): Input
}

data class ComponentConsumerLayerObject<Event, Input, Output>(
    override val machine: ComponentMachine<Input, Output>,
    private val mapper: Mapper<Event, Input>,
) : ComponentConsumerLayerType<Event, Input, Output> {

    override fun map(event: Event): Input = mapper(event)
}


interface ComponentProducerLayerType<Event, Input, Output> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward<Input, Output, StateAction<Event>> {
            Ward.set(
                StateAction.WillUpdate(map(it))
            )
        }.inward {
            Ward.set()
        }

    val machine: ComponentMachine<Input, Output>

    fun map(output: Output): Event
}


data class ComponentProducerLayerObject<Event, Input, Output>(
    override val machine: ComponentMachine<Input, Output>,
    private val mapper: Mapper<Output, Event>,
) : ComponentProducerLayerType<Event, Input, Output> {

    override fun map(output: Output): Event = mapper(output)
}


interface ComponentMapEventLayerType<Event, Output> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward<Event, Output, StateAction<Event>> {
            Ward.set(
                StateAction.WillUpdate(map(it))
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<Event> -> Ward.set()
                is StateAction.DidUpdate<Event> -> Ward.set(it.event)
            }
        }

    val machine: ComponentMachine<Event, Output>

    fun map(output: Output): Event
}

data class ComponentMapEventLayerObject<Event, Output>(
    override val machine: ComponentMachine<Event, Output>,
    private val mapper: Mapper<Output, Event>,
) : ComponentMapEventLayerType<Event, Output> {

    override fun map(output: Output): Event = mapper(output)
}

interface ComponentMapStateLayerType<Event, Input> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward<Input, Event, StateAction<Event>> {
            Ward.set(
                StateAction.WillUpdate(it)
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<Event> -> Ward.set()
                is StateAction.DidUpdate<Event> -> Ward.set(
                    map(it.event)
                )
            }
        }

    val machine: ComponentMachine<Input, Event>

    fun map(event: Event): Input
}

data class MapStateLayerObject<Event, Input>(
    override val machine: ComponentMachine<Input, Event>,
    private val mapper: Mapper<Event, Input>,
) : ComponentMapStateLayerType<Event, Input> {

    override fun map(event: Event): Input = mapper(event)
}

interface NoMapLayerType<Event> : ComponentLayer<Event> {

    override val child: ComponentMachine<StateAction<Event>, StateAction<Event>>
        get() = machine.outward<Event, Event, StateAction<Event>> {
            Ward.set(
                StateAction.WillUpdate(it)
            )
        }.inward {
            when (it) {
                is StateAction.WillUpdate<Event> -> Ward.set()
                is StateAction.DidUpdate<Event> -> Ward.set(it.event)
            }
        }

    val machine: ComponentMachine<Event, Event>
}

data class ComponentNoMapLayerObject<Event>(
    override val machine: ComponentMachine<Event, Event>,
) : NoMapLayerType<Event>