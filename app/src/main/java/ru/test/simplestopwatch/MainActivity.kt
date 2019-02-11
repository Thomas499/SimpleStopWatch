package ru.test.simplestopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.test.simplestopwatch.mainScreen.MainScreenFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            changeFragment(MainScreenFragment.newInstance())
        }
    }

    private fun changeFragment(newFragment: Fragment) {
        val tag = newFragment.javaClass.simpleName
        val isInBackStack: Boolean = supportFragmentManager.findFragmentById(R.id.fragment_container) != null

        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.fragment_container, newFragment, tag)
            if(isInBackStack) {
                addToBackStack(tag)
            }
            commit()
        }
    }
}
