package net.skyscanner.hackameetup.flightresults

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotterknife.bindView
import net.skyscanner.hackameetup.R
import net.skyscanner.hackameetup.application.getComponent
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightItinerary
import net.skyscanner.hackameetup.feature.flightsearch.data.FlightLeg
import net.skyscanner.hackameetup.flightresults.FlightResultsViewModel.FlightResults
import net.skyscanner.hackameetup.injection.AppViewModelFactory
import net.skyscanner.hackameetup.injection.InjectingActivity
import javax.inject.Inject

class FlightResultsActivity : AppCompatActivity(), InjectingActivity<FlightResultsComponent> {

    override val component: FlightResultsComponent by lazy { application.getComponent().mainActivityComponent() }

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val recycler: RecyclerView by bindView(R.id.recyclerview)
    private val paginationIndicator: TextView by bindView(R.id.results_page_indicator)
    private val adapter: FlightResultCardAdapter = FlightResultCardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_results)

        component.inject(this)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        ViewModelProviders.of(this, viewModelFactory)
            .get(FlightResultsViewModel::class.java)
            .flightResultsLiveData
            .observe(this, Observer<FlightResults>(this::bindData))
    }

    private fun bindData(itinerary: FlightResults?) {
        itinerary?.let {
            supportActionBar?.subtitle = it.title
            // no clue where to get the "x out of x results" from. fake it till you make it
            paginationIndicator.text = resources.getString(R.string.results_amount, it.itineraries.size, it.itineraries.size)
            adapter.itineraries = it.itineraries.also { adapter.notifyDataSetChanged() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_results, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

class FlightResultCardAdapter : RecyclerView.Adapter<CardHolder>() {

    var itineraries: List<FlightItinerary> = emptyList()

    override fun onBindViewHolder(holder: CardHolder, position: Int) =
        holder.bind(itineraries[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.card_flight_result, parent, false)
            .let { view -> CardHolder(view) }

    override fun getItemCount(): Int = itineraries.size
}

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val provider: TextView by bindView(R.id.cardRow_provider)
    val formatPrice: TextView by bindView(R.id.cardRow_price)
    val rating: TextView by bindView(R.id.cardRow_rating)
    val topRow: RowHolder = RowHolder(itemView.findViewById(R.id.flightResult_row1))
    val bottomRow: RowHolder = RowHolder(itemView.findViewById(R.id.flightResult_row2))
}

class RowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val carrierImage: ImageView by bindView(R.id.cardRow_carrierImage)
    val flightDuration: TextView by bindView(R.id.cardRow_flightDuration)
    val flightTimes: TextView by bindView(R.id.cardRow_flightTimes)
    val flightHops: TextView by bindView(R.id.cardRow_flightHops)
    val flightCodes: TextView by bindView(R.id.cardRow_flightCodes)
}

private fun CardHolder.bind(flightItinerary: FlightItinerary): Unit = with(flightItinerary) {
    outBoundLeg.let(topRow::bind)
    inboundLeg.let(bottomRow::bind)
    provider.text = "via ${bookingAgent.name}"
    formatPrice.text = price
    rating.text = "${score.icon} ${score.value}"
}

private fun RowHolder.bind(leg: FlightLeg): Unit = with(leg) {
    Picasso.with(itemView.context).load(leg.carriers.first().logoUrl).into(carrierImage)
    flightTimes.text = "${leg.departureTime} - ${leg.arrivalTime}"
    flightDuration.text = leg.duration
    flightCodes.text = "${leg.origin.code}-${leg.destination.code}"
    flightHops.text = when {
        leg.stops.isEmpty() -> "Direct"
        else -> "${leg.stops.size} stops"
    }
}
