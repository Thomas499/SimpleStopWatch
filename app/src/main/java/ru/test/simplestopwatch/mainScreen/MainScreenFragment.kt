package ru.test.simplestopwatch.mainScreen

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import ru.test.simplestopwatch.R
import ru.test.simplestopwatch.service.StopWatchService
import ru.test.simplestopwatch.utils.*

const val CHANGE_TIME_ACTION: String = "ChangeTime"
private const val START_BUTTON_VISIBILITY_KEY: String = "START_BUTTON_VISIBILITY_KEY"
private const val STOP_BUTTON_VISIBILITY_KEY: String = "STOP_BUTTON_VISIBILITY_KEY"
private const val BUTTONS_LAYOUT_VISIBILITY_KEY: String = "BUTTONS_LAYOUT_VISIBILITY_KEY"

class MainScreenFragment : Fragment() {
    private lateinit var hoursField: TextView
    private lateinit var minutesField: TextView
    private lateinit var secondsField: TextView
    private lateinit var millisecondsField: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var buttonsLayout: LinearLayout
    private lateinit var continueButton: Button
    private lateinit var resetButton: Button

    private lateinit var serviceIntent: Intent
    private lateinit var broadcastManager: LocalBroadcastManager

    private lateinit var usingService: StopWatchService
    private var isBound: Boolean = false

    private val customConnection = object : ServiceConnection {
        override fun onServiceDisconnected(componentName: ComponentName?) {
            isBound = false
        }

        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            val customBinder = binder as StopWatchService.CustomBinder
            usingService = customBinder.getService()
            restoreValues()
        }
    }

    private val changeTimeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            millisecondsField.text = TimeUtil.getMillisecondsTextValue(usingService.currentMilliseconds)
            secondsField.text = TimeUtil.getTimeUnits(usingService.totalMilliseconds)
            minutesField.text = TimeUtil.getTimeUnits(totalMilliseconds = usingService.totalMilliseconds, timeUnit = MINUTES_KEY)
            hoursField.text = TimeUtil.getTimeUnits(totalMilliseconds = usingService.totalMilliseconds, timeUnit = HOURS_KEY)
        }
    }

    companion object {
        fun newInstance(): MainScreenFragment {
            val fragment = MainScreenFragment()

            val arguments = Bundle()
            fragment.arguments = arguments

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        serviceIntent = Intent(activity, StopWatchService::class.java)
        broadcastManager = LocalBroadcastManager.getInstance(activity as Context)
        broadcastManager.registerReceiver(changeTimeReceiver, IntentFilter(CHANGE_TIME_ACTION))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(START_BUTTON_VISIBILITY_KEY, startButton.visibility)
        outState.putInt(STOP_BUTTON_VISIBILITY_KEY, stopButton.visibility)
        outState.putInt(BUTTONS_LAYOUT_VISIBILITY_KEY, buttonsLayout.visibility)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            hoursField = findViewById(R.id.hours_field)
            minutesField = findViewById(R.id.minutes_field)
            secondsField = findViewById(R.id.seconds_field)
            millisecondsField = findViewById(R.id.milliseconds_field)
            startButton = findViewById(R.id.start_button)
            buttonsLayout = findViewById(R.id.buttons_layout)
            stopButton = findViewById(R.id.stop_button)
            continueButton = findViewById(R.id.continue_button)
            resetButton = findViewById(R.id.reset_button)

            if(savedInstanceState != null) {
                startButton.visibility = savedInstanceState.getInt(START_BUTTON_VISIBILITY_KEY)
                stopButton.visibility = savedInstanceState.getInt(STOP_BUTTON_VISIBILITY_KEY)
                buttonsLayout.visibility = savedInstanceState.getInt(BUTTONS_LAYOUT_VISIBILITY_KEY)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        activity!!.bindService(serviceIntent, customConnection, Context.BIND_AUTO_CREATE)

        startButton.setOnClickListener {
            stopButton.visibility = View.VISIBLE
            startButton.visibility = View.GONE

            if(!isBound) {
                activity!!.startService(serviceIntent)
                isBound = true
            }
        }

        stopButton.setOnClickListener {
            usingService.stopUpdate()

            buttonsLayout.visibility = View.VISIBLE
            stopButton.visibility = View.GONE
        }

        continueButton.setOnClickListener {
            usingService.continueUpdate()

            stopButton.visibility = View.VISIBLE
            buttonsLayout.visibility = View.GONE
        }

        resetButton.setOnClickListener {
            buttonsLayout.visibility = View.GONE
            startButton.visibility = View.VISIBLE

            usingService.reset()

            if(isBound) {
                activity?.stopService(serviceIntent)
                isBound = false
            }

            resetTimeValues()
        }
    }

    override fun onPause() {
        broadcastManager.unregisterReceiver(changeTimeReceiver)
        activity?.unbindService(customConnection)
        super.onPause()
    }

    private fun resetTimeValues() {
        hoursField.text = DEFAULT_TIME_VALUE
        minutesField.text = DEFAULT_TIME_VALUE
        secondsField.text = DEFAULT_TIME_VALUE
        millisecondsField.text = DEFAULT_TIME_VALUE
    }

    private fun restoreValues() {
        with(usingService) {
            millisecondsField.text = TimeUtil.getMillisecondsTextValue(currentMilliseconds)
            secondsField.text = TimeUtil.getTimeUnits(totalMilliseconds)
            minutesField.text = TimeUtil.getTimeUnits(totalMilliseconds = totalMilliseconds, timeUnit = MINUTES_KEY)
            hoursField.text = TimeUtil.getTimeUnits(totalMilliseconds = totalMilliseconds, timeUnit = HOURS_KEY)
        }
    }
}