package net.skyscanner.hackameetup.feature.flightsearch.domain

import io.reactivex.Flowable
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightItinerary
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightsRepository
import net.skyscanner.hackameetup.service.flights.data.SessionRequest
import org.amshove.kluent.Verify
import org.amshove.kluent.VerifyNotCalled
import org.amshove.kluent.When
import org.amshove.kluent.`it returns`
import org.amshove.kluent.called
import org.amshove.kluent.calling
import org.amshove.kluent.mock
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import java.util.Optional.empty

@RunWith(MockitoJUnitRunner.Strict::class)
class RetrieveFlightsTest {
    @Mock
    private lateinit var repo: FlightsRepository

    private val emptyData: Flowable<Optional<List<FlightItinerary>>> = Flowable.just(empty())
    private val someData = Flowable.just(Optional.of(listOf(mock<FlightItinerary>())))

    @Before
    fun before() {
    }

    @Test
    fun `when data is empty fetch is requested`() {
        When calling repo.getAllFlights() `it returns` emptyData
        val interactor = createInteractor()

        interactor.getStream(empty()).test()

        Verify on repo that repo.fetchFlights(
            SessionRequest(
                country = "UK",
                currency = "GBP",
                locale = "en-GB",
                originPlace = "EDI-sky",
                destinationPlace = "LOND-sky",
                locationSchema = "iata",
                cabinClass = "Economy",
                outboundDate = "2018-09-30",
                inboundDate = "2018-11-02",
                adults = 1
            )
        ) was called
    }

    @Test
    fun `when there is data in repo no fetch is requested`() {
        When calling repo.getAllFlights() `it returns` someData
        val interactor = createInteractor()

        interactor.getStream(empty()).test()


        VerifyNotCalled on repo that repo.fetchFlights(
            SessionRequest(
                country = "UK",
                currency = "GBP",
                locale = "en-GB",
                originPlace = "EDI-sky",
                destinationPlace = "LOND-sky",
                locationSchema = "iata",
                cabinClass = "Economy",
                outboundDate = "2018-09-30",
                inboundDate = "2018-11-02",
                adults = 1
            )
        ) was called
    }

    private fun createInteractor() = RetrieveFlights(repo)
}
