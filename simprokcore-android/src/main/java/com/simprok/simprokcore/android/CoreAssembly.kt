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
        fun <State> create(
            layer: WidgetLayer<State>
        ): CoreAssembly = create(layer, setOf())

        fun <State> create(
            main: WidgetLayer<State>,
            vararg secondary: Layer<State>
        ): CoreAssembly = create(main, secondary.toSet())

        fun <State> create(
            main: WidgetLayer<State>,
            secondary: List<Layer<State>>
        ): CoreAssembly = create(main, secondary.toSet())

        fun <State> create(
            main: WidgetLayer<State>,
            secondary: Set<Layer<State>>
        ): CoreAssembly = CoreAssembly { scope ->
            object : Core<State> {
                override val layers: Set<Layer<State>>
                    get() = secondary.plus(main.layer)

                override val scope: CoroutineScope
                    get() = scope
            }.start {}
        }
    }
}

fun <State> Assembly.Companion.create(
    layer: WidgetLayer<State>
): CoreAssembly = CoreAssembly.create(layer)

fun <State> Assembly.Companion.create(
    main: WidgetLayer<State>,
    vararg secondary: Layer<State>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet()
)

fun <State> Assembly.Companion.create(
    main: WidgetLayer<State>,
    secondary: List<Layer<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet()
)

fun <State> Assembly.Companion.create(
    main: WidgetLayer<State>,
    secondary: Set<Layer<State>>
): CoreAssembly = CoreAssembly.create(
    main,
    secondary.toSet()
)