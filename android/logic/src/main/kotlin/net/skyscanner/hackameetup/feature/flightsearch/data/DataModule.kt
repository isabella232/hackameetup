package net.skyscanner.hackameetup.feature.flightsearch.data

import dagger.Module
import dagger.Provides
import net.skyscanner.hackameetup.core.data.cache.Cache
import net.skyscanner.hackameetup.core.data.store.MemoryReactiveStore
import net.skyscanner.hackameetup.core.data.store.MemoryStore
import net.skyscanner.hackameetup.core.data.store.ReactiveStore
import net.skyscanner.hackameetup.core.provider.TimestampProvider
import net.skyscanner.hackameetup.service.flights.FlightsServiceModule
import javax.inject.Singleton

@Module(includes = arrayOf(FlightsServiceModule::class))
class DataModule {
    private val extractKeyFromModel: (FlightItinerary) -> String = { value -> value.toString() }

    @Provides
    @Singleton
    fun provideCache(timestampProvider: TimestampProvider): MemoryStore<String, FlightItinerary> =
        Cache(extractKeyFromModel, timestampProvider)

    @Provides
    @Singleton
    fun provideStore(cache: MemoryStore<@JvmSuppressWildcards String, FlightItinerary>): ReactiveStore<String, FlightItinerary> =
        MemoryReactiveStore(extractKeyFromModel, cache)

    @Provides
    fun provideMarketDataMapper(): RawFlightDataMapper = FlightDataMapper()
}

