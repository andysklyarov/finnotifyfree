package com.andysklyarov.finnotify.presentation

import androidx.fragment.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment : Fragment, addToBackstack : Boolean)
}