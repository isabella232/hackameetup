package net.skyscanner.hackameetup.application

import dagger.BindsInstance
import dagger.Component
import net.skyscanner.hackameetup.feature.flightsearch.data.DataModule
import net.skyscanner.hackameetup.flightresults.FlightResultsComponent
import net.skyscanner.hackameetup.service.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        DataModule::class,
        NetworkModule::class,
        ViewModelModule::class
    )
)
interface ApplicationComponent {
    fun inject(app: SkyscannerTaskApplication)

    fun mainActivityComponent(): FlightResultsComponent

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: SkyscannerTaskApplication): Builder

        fun build(): ApplicationComponent
    }
}
