package com.andysklyarov.finnotify.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.andysklyarov.finnotify.R

private const val SAVED_CODE_SHARED_PREFERENCES_KEY = "SAVED_CODE_SHARED_PREFERENCES_KEY"

class MainActivity : AppCompatActivity(), NavigationHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val w = window;
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        val code = loadCode();
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CurrencyFragment.newInstance(code))
                .commit();
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    private fun loadCode(): String {
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return myPreferences.getString(
            SAVED_CODE_SHARED_PREFERENCES_KEY,
            getString(R.string.default_currency_code)
        )!!
    }

    fun safeCode(code: String) {
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putString(SAVED_CODE_SHARED_PREFERENCES_KEY, code)
        myEditor.apply()
    }
}