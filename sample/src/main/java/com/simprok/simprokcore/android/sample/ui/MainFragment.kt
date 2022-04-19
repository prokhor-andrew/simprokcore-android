package com.simprok.simprokcore.android.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.simprok.simprokandroid.FilterMap
import com.simprok.simprokandroid.filterMapOutput
import com.simprok.simprokcore.android.sample.R

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textView)
        val button = view.findViewById<Button>(R.id.button)

        filterMapOutput<UILayerState, UILayerEvent, Unit> {
            FilterMap.Map(UILayerEvent)
        }.filterMapInput {
            FilterMap.Map(it?.text)
        }.render { input, callback ->
            textView.text = input ?: "loading"
            button.setOnClickListener { callback(Unit) }
        }
    }
}