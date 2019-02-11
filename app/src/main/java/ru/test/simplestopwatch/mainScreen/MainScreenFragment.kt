package ru.test.simplestopwatch.mainScreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import ru.test.simplestopwatch.R

class MainScreenFragment : Fragment() {
    private lateinit var hoursField: TextView
    private lateinit var minutesField: TextView
    private lateinit var secondsField: TextView
    private lateinit var millisecondsField: TextView
    private lateinit var launchButton: Button
    private lateinit var buttonsLayout: LinearLayout
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    companion object {
        fun newInstance(): MainScreenFragment {
            val fragment = MainScreenFragment()

            val arguments = Bundle()
            fragment.arguments = arguments

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hoursField = view.findViewById(R.id.hours_field)
        minutesField = view.findViewById(R.id.minutes_field)
        secondsField = view.findViewById(R.id.seconds_field)
        millisecondsField = view.findViewById(R.id.milliseconds_field)
        launchButton = view.findViewById(R.id.launch_button)
        buttonsLayout = view.findViewById(R.id.buttons_layout)
        stopButton = view.findViewById(R.id.stop_button)
        resetButton = view.findViewById(R.id.reset_button)
    }

    override fun onResume() {
        super.onResume()
        launchButton.setOnClickListener {
            buttonsLayout.visibility = View.VISIBLE
            launchButton.visibility = View.GONE
        }
        stopButton.setOnClickListener {}
        resetButton.setOnClickListener {
            buttonsLayout.visibility = View.GONE
            launchButton.visibility = View.VISIBLE
        }
    }
}