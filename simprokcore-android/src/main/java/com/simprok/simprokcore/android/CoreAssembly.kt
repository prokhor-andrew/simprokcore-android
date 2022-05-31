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
        fun <Event, State> create(
            layer: WidgetLayer<Event, State>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(layer, setOf(), reducer)

        fun <Event, State> create(
            main: WidgetLayer<Event, State>,
            vararg secondary: Layer<Event, State>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(main, secondary.toSet(), reducer)

        fun <Event, State> create(
            main: WidgetLayer<Event, State>,
            secondary: List<Layer<Event, State>>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = create(main, secondary.toSet(), reducer)

        fun <Event, State> create(
            main: WidgetLayer<Event, State>,
            secondary: Set<Layer<Event, State>>,
            reducer: BiMapper<State?, Event, ReducerResult<State>>
        ): CoreAssembly = CoreAssembly { scope ->
            object : Core<Event, State> {
                override val layers: Set<Layer<Event, State>>
                    get() = secondary.plus(main.layer)

                override val scope: CoroutineScope
                    get() = scope

                override fun reduce(state: State?, event: Event): ReducerResult<State> = reducer(state, event)
            }.start {}
        }
    }
}

fun <Event, State> Assembly.Companion.create(
    layer: WidgetLayer<Event, State>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(layer, reducer)

fun <Event, State> Assembly.Companion.create(
    main: WidgetLayer<Event, State>,
    vararg secondary: Layer<Event, State>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)

fun <Event, State> Assembly.Companion.create(
    main: WidgetLayer<Event, State>,
    secondary: List<Layer<Event, State>>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)

fun <Event, State> Assembly.Companion.create(
    main: WidgetLayer<Event, State>,
    secondary: Set<Layer<Event, State>>,
    reducer: BiMapper<State?, Event, ReducerResult<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet(),
    reducer
)