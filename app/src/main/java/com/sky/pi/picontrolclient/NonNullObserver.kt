package com.sky.pi.picontrolclient

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(fragment: Fragment, block: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer {
        it?.let { t ->
            block(t)
        }
    })
}

fun <T: Any> LiveData<T>.observe(activity: AppCompatActivity, block: (T) -> Unit) {
    observe(activity, Observer {
        it?.let { t ->
            block(t)
        }
    })
}