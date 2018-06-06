package net.skyscanner.hackameetup.application

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class AppViewModelFactory(private val creators: Map<Class<out ViewModel>, () -> ViewModel>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: (() -> ViewModel)? = creators[modelClass]
        if (creator == null) {
            for ((key: Class<out ViewModel>, value: (() -> ViewModel)?) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
