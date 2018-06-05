package net.skyscanner.hackameetup.service.flights.data

data class SessionRequest(
    val country: String,
    val currency: String,
    val locale: String,
    val originPlace: String,
    val destinationPlace: String,
    val outboundDate: String,
    val inboundDate: String? = null,
    val cabinClass: String? = null,
    val adults: Int,
    val children: Int? = null,
    val infants: Int? = null, val locationSchema: String
)
