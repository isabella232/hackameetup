package net.skyscanner.hackameetup.application

import android.app.Application
import android.support.annotation.CallSuper

class SkyscannerTaskApplication : Application() {

    private var component: ApplicationComponent? = null

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        getComponent().inject(this)
    }

    fun getComponent(): ApplicationComponent = component ?: DaggerApplicationComponent.builder()
        .application(this)
        .build()
        .also { component = it }
}

fun Application.asSkyscannerApplication() = this as SkyscannerTaskApplication

fun Application.getComponent() = asSkyscannerApplication().getComponent()
