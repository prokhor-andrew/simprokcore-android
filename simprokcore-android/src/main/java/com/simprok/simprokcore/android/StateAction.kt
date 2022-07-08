//
//  StateAction.kt
//  simprokcore
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.


package com.simprok.simprokcore.android

/**
 * This class is public for implementation reasons only. It should not be used directly.
 */
sealed interface StateAction<Event> {

    data class WillUpdate<Event>(val event: Event) : StateAction<Event>

    data class DidUpdate<Event>(val event: Event) : StateAction<Event>
}