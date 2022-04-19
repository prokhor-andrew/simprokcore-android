package com.simprok.simprokcore.android.sample.ui

import com.simprok.simprokandroid.WidgetMachine
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokcore.android.WidgetLayer
import com.simprok.simprokcore.android.sample.AppState

class UILayer(
    override val machine: WidgetMachine<UILayerState, UILayerEvent>
) : WidgetLayer.Type<AppState, UILayerState, UILayerEvent> {

    override fun map(state: AppState): UILayerState = UILayerState("${state.value}")

    override fun reduce(state: AppState?, event: UILayerEvent): ReducerResult<AppState> =
        if (state == null) {
            ReducerResult.Skip()
        } else {
            ReducerResult.Set(AppState(state.value + 1))
        }
}