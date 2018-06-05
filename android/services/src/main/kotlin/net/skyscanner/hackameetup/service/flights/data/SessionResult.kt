package net.skyscanner.hackameetup.service.flights.data

data class SessionResult(
    val Query: Query,
    val Status: String,
    val SessionKey: String,
    val Itineraries: List<Itinerary>,
    val Legs: List<Leg>,
    val Carriers: List<Carrier>,
    val Agents: List<Agent>,
    val Places: List<Place>
)

data class Query(
    val OriginPlace: Int,
    val DestinationPlace: Int,
    val Currency: String
)

data class Itinerary(
    val OutboundLegId: String,
    val InboundLegId: String,
    val PricingOptions: List<PricingOption>
)

data class PricingOption(
    val Price: Float,
    val Agents: List<Int>
)

data class Leg(
    val Id: String,
    val SegmentIds: List<Int>,
    val OriginStation: Int,
    val DestinationStation: Int,
    val Departure: String,
    val Arrival: String,
    val Duration: Int,
    val Stops: List<Int>,
    val Carriers: List<Int>,
    val OperatingCarriers: List<Int>,
    val FlightNumbers: List<FlightNumber>
)

data class FlightNumber(
    val FlightNumber: String,
    val CarrierId: Int
)

data class Carrier(
    val Id: Int,
    val Code: String
)

data class Agent(
    val Id: Int,
    val Name: String
)

data class Place(
    val Id: Int,
    val Name: String,
    val Code: String
)
