package net.skyscanner.hackameetup.application

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.skyscanner.hackameetup.flightresults.FlightResultsViewModel
import net.skyscanner.hackameetup.injection.AppViewModelFactory
import net.skyscanner.hackameetup.injection.ViewModelKey

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FlightResultsViewModel::class)
    abstract fun bindMarketChartViewModel(flightResultsViewModel: FlightResultsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelProviderFactory(appViewModelFactory: AppViewModelFactory): ViewModelProvider.Factory
}
