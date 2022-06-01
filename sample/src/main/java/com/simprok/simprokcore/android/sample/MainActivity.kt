package com.simprok.simprokcore.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simprok.simprokandroid.start
import com.simprok.simprokcore.ReducerResult
import com.simprok.simprokcore.android.CoreAssembly
import com.simprok.simprokcore.android.sample.logger.LoggerLayer
import com.simprok.simprokcore.android.sample.storage.StorageLayer
import com.simprok.simprokcore.android.sample.ui.MainFragment
import com.simprok.simprokcore.android.sample.ui.UILayer
import com.simprok.simprokcore.android.sample.ui.UILayerEvent
import com.simprok.simprokcore.android.sample.ui.UILayerState

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment(), "tag")
                .commit()

            start<UILayerState, UILayerEvent> {
                CoreAssembly.create(
                    UILayer(it),
                    LoggerLayer(),
                    StorageLayer(getSharedPreferences("storage", MODE_PRIVATE))
                ) { state, event ->
                    if (state != null) {
                        when (event) {
                            is AppEvent.UILayerEvent -> ReducerResult.Set(AppState(state.value + 1))
                            is AppEvent.StorageLayerEvent -> ReducerResult.Skip()
                        }
                    } else {
                        when (event) {
                            is AppEvent.UILayerEvent -> ReducerResult.Skip()
                            is AppEvent.StorageLayerEvent -> ReducerResult.Set(AppState(event.value))
                        }
                    }
                }
            }
        }
    }
}