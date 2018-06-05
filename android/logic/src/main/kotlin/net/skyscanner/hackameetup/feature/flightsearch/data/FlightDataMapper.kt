package net.skyscanner.hackameetup.feature.flightsearch.data

import net.skyscanner.hackameetup.service.flights.data.Agent
import net.skyscanner.hackameetup.service.flights.data.Carrier
import net.skyscanner.hackameetup.service.flights.data.Itinerary
import net.skyscanner.hackameetup.service.flights.data.Leg
import net.skyscanner.hackameetup.service.flights.data.Place
import net.skyscanner.hackameetup.service.flights.data.SessionResult
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class FlightDataMapper : RawFlightDataMapper {
    private val flightTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    private val dateTimeParser = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss")
    private val moneyFormat: NumberFormat = NumberFormat.getCurrencyInstance().apply { maximumFractionDigits = 0 }
    private val random = Random()

    override fun map(result: SessionResult): List<FlightItinerary> =
        result.Itineraries.map { itinerary ->
            moneyFormat.currency = Currency.getInstance(result.Query.Currency)
            FlightItinerary(
                inboundLeg = result.Legs.find { leg -> leg.Id == itinerary.InboundLegId }.leg(result),
                outBoundLeg = result.Legs.find { it.Id == itinerary.OutboundLegId }.leg(result),
                bookingAgent = result.Agents.find { itinerary.pricingOptionWithAgent().Agents.first() == it.Id }.agent(),
                price = moneyFormat.format(itinerary.pricingOptionWithAgent().Price),
                score = randomScoreBecauseIcantSeeItInTheJson()
            )
        }

    private fun Leg?.leg(result: SessionResult): FlightLeg = with(this!!) {
        FlightLeg(
            origin = findPlace(result, OriginStation),
            destination = findPlace(result, DestinationStation),
            stops = this.Stops.map { findPlace(result, it) },
            duration = duration(Duration.toLong()),
            departureTime = flightTimeFormat.format(dateTimeParser.parse(Departure)),
            arrivalTime = flightTimeFormat.format(dateTimeParser.parse(Arrival)),
            carriers = result.Carriers.filter { carr -> carr.Id in this.Carriers }.map(Carrier::carrier)
        )
    }

    private fun duration(duration: Long): String = when {
        TimeUnit.MINUTES.toHours(duration) > 0 -> String.format(
            "%dh %dm",
            TimeUnit.MINUTES.toHours(duration),
            duration % 60
        )
        else -> String.format("%dm", duration)
    }

    private fun randomScoreBecauseIcantSeeItInTheJson(): FlightScore = with(random) {
        val score = random.nextFloat() * 7.0f + 3.0f
        FlightScore(
            value = String.format("%.1f", score),
            icon = when (score) {
                in 7.0..10.0 -> "\uD83D\uDE0A"
                in 5.0..7.0 -> "\uD83D\uDE10"
                else -> "☹️"
            }
        )
    }

}

private fun Agent?.agent(): FlightAgent = with(this!!) {
    FlightAgent(name = Name)
}

private fun Carrier.carrier(): FlightCarrier =
    FlightCarrier(code = Code, logoUrl = "https://logos.skyscnr.com/images/airlines/favicon/$Code.png")

private fun Place.place(): FlightPlace =
    FlightPlace(name = Name, code = Code)

private fun findPlace(result: SessionResult, placeId: Int) =
    result.Places.find { it.Id == placeId }!!.place()

private fun Itinerary.pricingOptionWithAgent() = PricingOptions.first { it.Agents.isNotEmpty() }