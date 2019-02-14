package ru.test.simplestopwatch.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import ru.test.simplestopwatch.mainScreen.CHANGE_TIME_ACTION
import ru.test.simplestopwatch.utils.DEFAULT_TIME_UNIT_VALUE
import ru.test.simplestopwatch.utils.MAX_MILLISECONDS
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private const val TOTAL_MILLISECONDS_ADDITION: Long = 10L
private const val PAUSE_VALUE: Long = 10L
private const val THREADS_AMOUNT = 5

class StopWatchService : Service() {
    private val binder: IBinder = CustomBinder()
    private lateinit var executorService: ScheduledExecutorService

    private var shouldUpdate = true
    var totalMilliseconds: Long = DEFAULT_TIME_UNIT_VALUE
    var currentMilliseconds: Long = DEFAULT_TIME_UNIT_VALUE

    override fun onBind(intent: Intent): IBinder = binder

    private fun startSendData() {
        executorService.scheduleAtFixedRate({
            countTime()
        }, 0, PAUSE_VALUE, TimeUnit.MILLISECONDS)
    }

    private fun countTime() {
        if(shouldUpdate) {
            totalMilliseconds += TOTAL_MILLISECONDS_ADDITION
            currentMilliseconds++
            if(currentMilliseconds >= MAX_MILLISECONDS) {
                currentMilliseconds = DEFAULT_TIME_UNIT_VALUE
            }

            val intent = Intent(CHANGE_TIME_ACTION)

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        executorService = Executors.newScheduledThreadPool(THREADS_AMOUNT)
        startSendData()
        return START_NOT_STICKY
    }

    fun reset() {
        executorService.shutdownNow()
        totalMilliseconds = DEFAULT_TIME_UNIT_VALUE
        currentMilliseconds = DEFAULT_TIME_UNIT_VALUE
        shouldUpdate = true
    }

    inner class CustomBinder : Binder() {
        fun getService(): StopWatchService = this@StopWatchService
    }

    fun continueUpdate() {
        shouldUpdate = true
    }

    fun stopUpdate() {
        shouldUpdate = false
    }
}