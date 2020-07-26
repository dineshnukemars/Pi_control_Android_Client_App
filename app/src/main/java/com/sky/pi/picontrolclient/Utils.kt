package com.sky.pi.picontrolclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.LayoutRes

const val serverIp = "192.168.0.16"
const val serverPort = 50053

fun ViewGroup.inflateLayout(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun doNothing() {
    // nothing, just to satisfy the when statement
}

fun SeekBar.setSeekBarListener(onStopTrack: (seekBar: SeekBar) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar,
            progress: Int,
            fromUser: Boolean
        ) = Unit

        override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            onStopTrack(seekBar)
        }
    })
}