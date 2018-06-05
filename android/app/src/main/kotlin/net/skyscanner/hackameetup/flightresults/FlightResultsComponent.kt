package net.skyscanner.hackameetup.flightresults

import dagger.Subcomponent
import net.skyscanner.hackameetup.core.injection.scope.ActivityScope
import net.skyscanner.hackameetup.injection.ActivityModule

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface FlightResultsComponent {
    fun inject(mainActivity: FlightResultsActivity)

}
