package net.skyscanner.hackameetup.service.flights

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.reactivex.Single
import net.skyscanner.hackameetup.core.injection.qualifier.NetworkInterceptor
import net.skyscanner.hackameetup.service.flights.data.SessionResult
import net.skyscanner.hackameetup.service.network.NetworkModule
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Response
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Named
import javax.inject.Singleton

@Module
class FlightsServiceModule {

    @Provides
    fun provideService(retrofit: Retrofit): FlightsService {
        return retrofit.create(FlightsService::class.java)
    }

    @Provides
    @Named("mockService")
    fun provideMockService(gson: Gson): FlightsService {
        return MockFlightsService(gson)
    }

    @Provides
    @Named(NetworkModule.API_URL)
    fun provideBaseUrl(): String {
        return PRODUCTION_BASE_URL
    }

    @Provides
    @NetworkInterceptor
    @IntoSet
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("apiKey", API_KEY)
            .build()
            .let { httpUrl ->
                chain.request()
                    .newBuilder()
                    .url(httpUrl)
                    .build()
            }
            .let { request ->
                chain.proceed(request)
            }
    }

    companion object {
        private const val PRODUCTION_BASE_URL = "http://partners.api.skyscanner.net/apiservices/"
        private const val API_KEY = "YOUR_APIKEY"
    }
}

class MockFlightsService(private val gson: Gson) : FlightsService {
    override fun postSession(
        country: String,
        currency: String,
        locale: String,
        originPlace: String,
        destinationPlace: String,
        locationSchema: String,
        outboundDate: String,
        inboundDate: String?,
        adults: Int
    ): Single<Response<ResponseBody>> {
        val responseBody: ResponseBody = object : ResponseBody() {
            override fun contentLength(): Long = TODO()
            override fun contentType(): MediaType? = TODO()
            override fun source(): BufferedSource = TODO()
        }
        val headers = Headers.of(
            "Location",
            "http://partners.api.skyscanner.net/apiservices/pricing/uk1/v1.0/1ae30c726dad4472a2263c8fa1ae21a6_rrsqbjcb_06a13f0a788e803fcc56e78802891a26"
        )
        return Single.just(Response.success(responseBody, headers))
    }

    override fun getResults(sessionKey: String): Single<SessionResult> {
        val stream = javaClass.classLoader.getResourceAsStream("mock.json")
        val reader = BufferedReader(InputStreamReader(stream))
        return Single.just(gson.fromJson(reader, SessionResult::class.java))
    }
}
