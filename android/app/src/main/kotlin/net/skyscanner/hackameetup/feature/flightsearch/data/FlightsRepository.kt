package net.skyscanner.hackameetup.feature.flightsearch.data

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import net.skyscanner.hackameetup.core.data.store.ReactiveStore
import net.skyscanner.hackameetup.service.flights.FlightsService
import net.skyscanner.hackameetup.service.flights.data.SessionRequest
import net.skyscanner.hackameetup.service.flights.data.SessionResult
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class FlightsRepository(
    private val store: ReactiveStore<String, FlightItinerary>,
    private val flightsService: FlightsService,
    private val mapper: RawFlightDataMapper
) {
    private val sessionRegex = Regex(pattern = "http://partners\\.api\\.skyscanner\\.net/apiservices/pricing/.*/([a-z0-9_]+)")

    fun getAllFlights(): Flowable<Optional<List<FlightItinerary>>> = store.getAll()

    fun fetchFlights(request: SessionRequest): Completable =
        applyRequest(request)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap(this::createResultsRequest)
            .repeatWhen { guard -> guard.delay(1, TimeUnit.SECONDS) }
            .takeUntil({ response -> response.Status == "UpdatesComplete" })
            .retryWhen { guard -> guard.delay(1, TimeUnit.SECONDS) }
            .map(mapper::map)
            .doOnNext({ store.replaceAll(it) })
            // TODO signal completable without breaking the chain
            .ignoreElements()

    private fun applyRequest(request: SessionRequest) = with(request) {
        flightsService.postSession(
            country = country,
            currency = currency,
            locale = locale,
            originPlace = originPlace,
            destinationPlace = destinationPlace,
            locationSchema = locationSchema,
            outboundDate = outboundDate,
            inboundDate = inboundDate,
            adults = adults
        )
    }

    private fun createResultsRequest(response: Response<ResponseBody>): Single<SessionResult> =
        sessionRegex.find(response.headers()["Location"].toString())?.groups?.get(1)?.value
            .let { sessionKey -> flightsService.getResults(sessionKey.toString()) }
}