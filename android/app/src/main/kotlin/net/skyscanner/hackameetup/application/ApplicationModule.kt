package net.skyscanner.hackameetup.application

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import net.skyscanner.hackameetup.core.injection.qualifier.ForApplication
import net.skyscanner.hackameetup.core.injection.qualifier.NetworkInterceptor
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class ApplicationModule {
    @ForApplication
    @Provides
    fun provideApplicationContext(app: SkyscannerTaskApplication): Context = app.applicationContext

    @Provides
    @NetworkInterceptor
    @IntoSet
    @Singleton
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.setLevel(HttpLoggingInterceptor.Level.HEADERS)
}
