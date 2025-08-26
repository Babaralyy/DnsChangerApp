package com.dns.dnschangerapp.utils.extensions

import android.os.Handler
import android.os.Looper
import android.view.View

fun withDelay(delay: Long = 300, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(callback, delay)
}

fun View.setDebounceClickListener(
    debounceTime: Long = 500L,
    action: () -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            action.invoke()
        }
    }
}