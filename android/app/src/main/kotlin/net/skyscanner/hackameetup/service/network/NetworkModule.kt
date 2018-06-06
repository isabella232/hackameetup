package net.skyscanner.hackameetup.service.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule(
    private val networkInterceptors: Set<Interceptor>
) {

    private fun provideApiOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().also { it.networkInterceptors().addAll(networkInterceptors) }.build()

    private fun provideGson(): Gson = GsonBuilder().create()

    fun provideRetrofit(baseUrl: String, gson: Gson = provideGson(), client: OkHttpClient = provideApiOkHttpClient()): Retrofit =
        Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .baseUrl(baseUrl)
            .build()
}