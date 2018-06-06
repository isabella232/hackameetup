package net.skyscanner.hackameetup.feature.flightsearch.data

import io.reactivex.Single
import net.skyscanner.hackameetup.core.data.store.ReactiveStore
import net.skyscanner.hackameetup.service.flights.FlightsService
import net.skyscanner.hackameetup.service.flights.data.SessionRequest
import okhttp3.ResponseBody
import org.amshove.kluent.When
import org.amshove.kluent.`it returns`
import org.amshove.kluent.any
import org.amshove.kluent.calling
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner.Strict::class)
class FlightsRepositoryTest {
    @Mock
    private lateinit var store: ReactiveStore<String, FlightItinerary>
    @Mock
    private lateinit var serviceFlights: FlightsService
    @Mock
    private lateinit var mapper: RawFlightDataMapper
    @Mock
    private lateinit var response: Response<ResponseBody>

    @Test
    fun `I know how to configure tests`() {
        When calling serviceFlights.postSession(any(), any(), any(), any(), any(), any(), any(), any(), any()) `it returns` Single.just(response)
        val repo = createRepo()

        val observer = repo.fetchFlights(
            SessionRequest(
                country = "UK",
                currency = "GBP",
                locale = "en-GB",
                originPlace = "EDI-sky",
                destinationPlace = "LOND-sky",
                locationSchema = "iata",
                cabinClass = "Economy",
                outboundDate = "2018-05-30",
                inboundDate = "2018-06-02",
                adults = 1
            )
        ).test()

    }

    private fun createRepo(): FlightsRepository =
        FlightsRepository(
            store,
            serviceFlights,
            mapper
        )

}
