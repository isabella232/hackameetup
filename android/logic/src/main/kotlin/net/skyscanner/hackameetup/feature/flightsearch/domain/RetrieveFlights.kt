package net.skyscanner.hackameetup.feature.flightsearch.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.skyscanner.hackameetup.core.domain.interactor.ReactiveInteractor.RetrieveInteractor
import net.skyscanner.hackameetup.core.rx.UnwrapOptionalTransformer
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightItinerary
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightsRepository
import net.skyscanner.hackameetup.feature.flightsearch.domain.RetrieveFlights.FlightParams
import net.skyscanner.hackameetup.service.flights.data.SessionRequest
import java.util.*
import javax.inject.Inject

class RetrieveFlights @Inject constructor(
    private val flightsRepository: FlightsRepository
) : RetrieveInteractor<FlightParams, List<FlightItinerary>> {

    override fun getStream(params: Optional<FlightParams>): Flowable<List<FlightItinerary>> =
        flightsRepository.getAllFlights()
            .flatMapSingle { data -> fetchWhenNoneAndThenFlights(data, params) }
            .compose(UnwrapOptionalTransformer.create())

    private fun fetchWhenNoneAndThenFlights(flights: Optional<List<FlightItinerary>>, params: Optional<FlightParams>): Single<Optional<List<FlightItinerary>>> =
        fetchWhenNone(flights, params).andThen(Single.just(flights))

    private fun fetchWhenNone(flights: Optional<List<FlightItinerary>>, params: Optional<FlightParams>): Completable =
        if (flights.isPresent) Completable.complete() else flightsRepository.fetchFlights(createRequest(params))

    private fun createRequest(params: Optional<FlightParams>): SessionRequest {
        // some hardcoded values for the test
        val fparams: FlightParams? = params.orElse(null)
        return SessionRequest(
            country = "UK",
            currency = "GBP",
            locale = "en-GB",
            originPlace = fparams?.origin ?: "EDI-sky",
            destinationPlace = fparams?.destination ?: "LOND-sky",
            locationSchema = "iata",
            cabinClass = "Economy",
            outboundDate = fparams?.outbound ?: "2018-09-30",
            inboundDate = fparams?.inbound ?: "2018-11-02",
            adults = 1
        )
    }

    data class FlightParams(
        val origin: String,
        val destination: String,
        val outbound: String,
        val inbound: String
    )
}
