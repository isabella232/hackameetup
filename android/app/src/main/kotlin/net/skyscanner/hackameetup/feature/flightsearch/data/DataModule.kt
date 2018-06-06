package net.skyscanner.hackameetup.feature.flightsearch.data

import net.skyscanner.hackameetup.core.data.cache.Cache
import net.skyscanner.hackameetup.core.data.store.MemoryReactiveStore
import net.skyscanner.hackameetup.core.data.store.MemoryStore
import net.skyscanner.hackameetup.core.data.store.ReactiveStore
import net.skyscanner.hackameetup.core.provider.TimestampProvider
import net.skyscanner.hackameetup.service.flights.FlightsServiceModule

class DataModule(
    private val flightsServiceModule: FlightsServiceModule
) {

    private val extractKeyFromModel: (FlightItinerary) -> String = { value -> value.toString() }

    private fun provideCache(timestampProvider: TimestampProvider = TimestampProvider()): MemoryStore<String, FlightItinerary> =
        Cache(extractKeyFromModel, timestampProvider)

    private fun provideStore(cache: MemoryStore<String, FlightItinerary> = provideCache()): ReactiveStore<String, FlightItinerary> =
        MemoryReactiveStore(extractKeyFromModel, cache)

    private fun provideMarketDataMapper(): RawFlightDataMapper = FlightDataMapper()

    fun provideFlightsRepository() = FlightsRepository(
        provideStore(),
        flightsServiceModule.provideService(),
        provideMarketDataMapper()
    )
}

