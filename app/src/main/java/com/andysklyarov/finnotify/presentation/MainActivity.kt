package com.andysklyarov.finnotify.presentation

import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.andysklyarov.finnotify.BuildConfig
import com.andysklyarov.finnotify.R
import com.andysklyarov.finnotify.framework.MainApplication

class MainActivity : AppCompatActivity(), NavigationHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }

        val w = window;
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val code = (application as MainApplication).loadCode();
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

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? OnBackPressed)?.onBackPressed()?.takeIf { !it }.let {
            super.onBackPressed()
        }
    }
}