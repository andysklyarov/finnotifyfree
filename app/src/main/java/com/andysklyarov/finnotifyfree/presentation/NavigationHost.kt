package com.andysklyarov.finnotifyfree.presentation

import androidx.fragment.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment : Fragment, addToBackstack : Boolean)
}