package net.skyscanner.hackameetup.service.flights

import com.google.gson.Gson
import io.reactivex.Single
import net.skyscanner.hackameetup.BuildConfig
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

private const val PRODUCTION_BASE_URL = "http://partners.api.skyscanner.net/apiservices/"

class FlightsServiceModule(private val networkModule: NetworkModule) {

    fun provideService(retrofit: Retrofit = networkModule.provideRetrofit(provideBaseUrl())): FlightsService =
        retrofit.create(FlightsService::class.java)

    fun provideMockService(gson: Gson): FlightsService = MockFlightsService(gson)

    private fun provideBaseUrl(): String = PRODUCTION_BASE_URL

    companion object {
        fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
            chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("apiKey", BuildConfig.SKYSCANNER_API_KEY)
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
