package net.skyscanner.hackameetup.application

import android.arch.lifecycle.ViewModel
import android.util.Log
import net.skyscanner.hackameetup.feature.flightsearch.data.DataModule
import net.skyscanner.hackameetup.feature.flightsearch.domain.RetrieveFlights
import net.skyscanner.hackameetup.flightresults.FlightResultsViewModel
import net.skyscanner.hackameetup.service.flights.FlightsServiceModule
import net.skyscanner.hackameetup.service.network.NetworkModule
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object DefinitelyNotDagger {

    private val networkInterceptors = setOf(
        FlightsServiceModule.provideApiKeyInterceptor(),
        provideLoggingInterceptor()
    )

    private val networkModule = NetworkModule(networkInterceptors)

    private val flightsServiceModule = FlightsServiceModule(networkModule)

    private val dataModule = DataModule(flightsServiceModule)

    private val viewModelCreators: Map<Class<out ViewModel>, () -> FlightResultsViewModel> = mapOf(
        FlightResultsViewModel::class.java to { FlightResultsViewModel(provideRetrieveFlights()) }
    )

    private fun provideRetrieveFlights() = RetrieveFlights(dataModule.provideFlightsRepository())

    private fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.setLevel(HttpLoggingInterceptor.Level.BODY)

    val appViewModelFactory by lazy { AppViewModelFactory(viewModelCreators) }
}
