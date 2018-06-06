package net.skyscanner.hackameetup.feature.flightsearch.data

data class FlightItinerary(
    val outBoundLeg: FlightLeg,
    val inboundLeg: FlightLeg,
    val bookingAgent: FlightAgent,
    val price: String,
    val score: FlightScore
)

data class FlightLeg(
    val origin: FlightPlace,
    val destination: FlightPlace,
    val stops: List<FlightPlace>,
    val duration: String,
    val departureTime: String,
    val arrivalTime: String,
    val carriers: List<FlightCarrier>
)

data class FlightPlace(
    val name: String,
    val code: String
)

data class FlightCarrier(
    val code: String,
    val logoUrl: String
)

data class FlightAgent(
    val name: String
)

data class FlightScore(
    val value: String,
    val icon: String
)
