package com.arun.kotlincoroutines.demonstrations.backgroundthread

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arun.kotlincoroutines.common.ThreadInfoLogger

@Composable
fun BackgroundThreadDemoFragment(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val txtRemainingTime = remember {
        mutableStateOf("")
    }
    val isButtonEnabled = remember { mutableStateOf(true) }
    fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    fun updateRemainingTime(remainingTimeSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingTimeSeconds seconds")

        if (remainingTimeSeconds > 0) {
            txtRemainingTime.value = "$remainingTimeSeconds seconds remaining"
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            txtRemainingTime.value = "done!"
        }

    }

    fun executeBenchmark() {
        val benchmarkDurationSeconds = 5

        updateRemainingTime(benchmarkDurationSeconds)
        Thread {
            logThreadInfo("benchmark started")

            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("benchmark completed")
            Handler(Looper.getMainLooper()).post {
                logThreadInfo("benchmark for toast")
                Toast.makeText(context, "$iterationsCount", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

        Column(modifier = Modifier.padding(10.dp)) {
            Button(
                enabled = isButtonEnabled.value,
                onClick = {
                    Log.d("TAG", "onCreate: button start")
                    logThreadInfo("button callback")
                    isButtonEnabled.value = false
                    executeBenchmark()
                    isButtonEnabled.value = true
                    Log.d("TAG", "onCreate: button end")
                }) {
                Text(text = "Start BenchMark bg (${if (isButtonEnabled.value) "enabled" else "disabled"})")
            }
            Text(text = "Remaining Time: ${txtRemainingTime.value}")
        }

}