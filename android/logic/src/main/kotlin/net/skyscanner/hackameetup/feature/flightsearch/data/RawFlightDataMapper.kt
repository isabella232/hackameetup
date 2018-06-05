package net.skyscanner.hackameetup.feature.flightsearch.data

import net.skyscanner.hackameetup.service.flights.data.SessionResult

interface RawFlightDataMapper {
    fun map(result: SessionResult): List<FlightItinerary>
}
