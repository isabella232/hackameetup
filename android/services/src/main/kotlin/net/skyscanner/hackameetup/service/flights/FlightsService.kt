package net.skyscanner.hackameetup.service.flights

import io.reactivex.Single
import net.skyscanner.hackameetup.service.flights.data.SessionResult
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FlightsService {

    @FormUrlEncoded
    @POST("pricing/v1.0")
    fun postSession(
        @Field("country") country: String,
        @Field("currency") currency: String,
        @Field("locale") locale: String,
        @Field("originplace") originPlace: String,
        @Field("destinationplace") destinationPlace: String,
        @Field("locationschema") locationSchema: String,
        @Field("outbounddate") outboundDate: String,
        @Field("inbounddate") inboundDate: String?,
        @Field("adults") adults: Int
    ): Single<Response<ResponseBody>>

    @GET("pricing/v1.0/{sessionKey}")
    fun getResults(@Path("sessionKey") sessionKey: String): Single<SessionResult>
}
