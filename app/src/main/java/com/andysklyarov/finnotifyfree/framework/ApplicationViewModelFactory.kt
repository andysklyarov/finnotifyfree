package com.andysklyarov.finnotifyfree.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andysklyarov.finnotifyfree.interactors.Interactors
import com.andysklyarov.finnotifyfree.presentation.MainViewModel
import java.lang.reflect.InvocationTargetException

class ApplicationViewModelFactory(private var application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        private lateinit var useCase: Interactors
        fun inject(newUseCase: Interactors) {
            useCase = newUseCase
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == MainViewModel::class.java) {
            try {
                return modelClass.getConstructor(Application::class.java, Interactors::class.java)
                    .newInstance(application, useCase) as T
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        throw IllegalStateException("ViewModel must extend MainActivityViewModel")
    }
}