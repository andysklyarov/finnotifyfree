package com.andysklyarov.finnotifyfree.ui

import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.BuildConfig
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.databinding.MainActivityBinding
import com.andysklyarov.finnotifyfree.ui.fragments.CurrencyFragment
import com.andysklyarov.finnotifyfree.ui.fragments.OnBackPressed
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationHost {

    @Inject
    lateinit var factory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setStrictMode(BuildConfig.DEBUG)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        AppDelegate.getAppComponent().injectActivity(this)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.viewModel = viewModel

        val toolbar = binding.root.findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        val code = (application as AppDelegate).loadPreviousChCode();
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CurrencyFragment.newInstance(code))
                .commit()
        }
    }


    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction: FragmentTransaction =
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment)

        if (addToBackstack) transaction.addToBackStack(null)

        transaction.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container) as? OnBackPressed
        fragment?.onBackPressed()
        super.onBackPressed()
    }

    private fun setStrictMode(isDebug: Boolean) {
        if (isDebug) {
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
    }
}