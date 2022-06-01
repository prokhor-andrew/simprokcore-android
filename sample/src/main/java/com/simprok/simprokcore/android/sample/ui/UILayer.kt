package com.simprok.simprokcore.android.sample.ui

import com.simprok.simprokandroid.WidgetMachine
import com.simprok.simprokcore.android.WidgetLayer
import com.simprok.simprokcore.android.sample.AppEvent
import com.simprok.simprokcore.android.sample.AppState

class UILayer(
    override val machine: WidgetMachine<UILayerState, UILayerEvent>
) : WidgetLayer.Type<AppState, AppEvent, UILayerState, UILayerEvent> {

    override fun mapState(state: AppState): UILayerState = UILayerState("${state.value}")

    override fun mapEvent(event: UILayerEvent): AppEvent = AppEvent.UILayerEvent
}