package net.skyscanner.hackameetup.flightresults

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightItinerary
import net.skyscanner.hackameetup.feature.flightsearch.domain.RetrieveFlights
import net.skyscanner.hackameetup.feature.flightsearch.domain.RetrieveFlights.FlightParams
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject

class FlightResultsViewModel @Inject constructor(
    private val retrieveFlights: RetrieveFlights
) : ViewModel() {
    val flightResultsLiveData: MutableLiveData<FlightResults> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    private val nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    private val dayAfterNextMonday = nextMonday.plusDays(1)

    private val mapper: (List<FlightItinerary>) -> FlightResults = {
        val dateFormat = DateTimeFormatter.ofPattern("dd MMM")
        FlightResults(
            title = "${dateFormat.format(nextMonday)} – ${dateFormat.format(dayAfterNextMonday)}, 1 adult, economy",
            resultCount = it.size,
            itineraries = it
        )
    }

    init {
        compositeDisposable.add(bindFlights())
    }

    private fun bindFlights(): Disposable =
        retrieveFlights.getStream(Optional.of(createParams()))
            .observeOn(Schedulers.computation())
            .map(mapper)
            .subscribe(flightResultsLiveData::postValue)

    private fun createParams(): FlightParams {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return FlightParams(
            origin = "EDI-sky",
            destination = "LHR-sky",
            outbound = dateFormat.format(nextMonday),
            inbound = dateFormat.format(dayAfterNextMonday)
        )
    }

    data class FlightResults(
        val title: String,
        val itineraries: List<FlightItinerary>,
        val resultCount: Int
    )
}
