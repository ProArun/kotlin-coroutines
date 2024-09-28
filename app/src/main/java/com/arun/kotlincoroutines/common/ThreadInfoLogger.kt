package com.arun.kotlincoroutines.common

import android.util.Log

object ThreadInfoLogger {

    private const val TAG = "ThreadInfoLogger"

    fun logThreadInfo(message: String) {
        Log.d(TAG, "$message; thread name: ${Thread.currentThread().name}; thread ID: ${Thread.currentThread().id}")
    }

}