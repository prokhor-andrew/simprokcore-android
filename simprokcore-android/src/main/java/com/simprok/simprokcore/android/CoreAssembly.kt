//
//  CoreAssembly.kt
//  simprokcore-android
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokcore.android

import com.simprok.simprokandroid.Assembly
import com.simprok.simprokcore.Core
import com.simprok.simprokcore.Layer
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokmachine.api.BiMapper
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.start
import kotlinx.coroutines.CoroutineScope

class CoreAssembly private constructor(
    private val starter: Handler<CoroutineScope>
) : Assembly {

    override fun start(scope: CoroutineScope) {
        starter(scope)
    }

    companion object {
        fun <State, Event> create(
            layer: WidgetLayer<State, Event>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(layer, setOf(), reducer)

        fun <State, Event> create(
            main: WidgetLayer<State, Event>,
            vararg secondary: Layer<State, Event>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(main, secondary.toSet(), reducer)

        fun <State, Event> create(
            main: WidgetLayer<State, Event>,
            secondary: List<Layer<State, Event>>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(main, secondary.toSet(), reducer)

        fun <State, Event> create(
            main: WidgetLayer<State, Event>,
            secondary: Set<Layer<State, Event>>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = CoreAssembly { scope ->
            object : Core<State, Event> {
                override val layers: Set<Layer<State, Event>>
                    get() = secondary.plus(main.layer)

                override val scope: CoroutineScope
                    get() = scope

                override fun reduce(state: State?, event: Event): ReducerResult<State> =
                    reducer(state, event)
            }.start {}
        }
    }
}

fun <State, Event> Assembly.Companion.create(
    layer: WidgetLayer<State, Event>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(layer, reducer)

fun <State, Event> Assembly.Companion.create(
    main: WidgetLayer<State, Event>,
    vararg secondary: Layer<State, Event>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)

fun <State, Event> Assembly.Companion.create(
    main: WidgetLayer<State, Event>,
    secondary: List<Layer<State, Event>>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)

fun <State, Event> Assembly.Companion.create(
    main: WidgetLayer<State, Event>,
    secondary: Set<Layer<State, Event>>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)