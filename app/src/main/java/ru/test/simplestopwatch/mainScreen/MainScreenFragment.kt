package ru.test.simplestopwatch.mainScreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.test.simplestopwatch.R

class MainScreenFragment : Fragment() {

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
}