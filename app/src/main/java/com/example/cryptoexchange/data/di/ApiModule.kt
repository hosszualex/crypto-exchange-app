package com.example.cryptoexchange.data.di

import android.util.Log
import com.example.cryptoexchange.data.service.BitfinexApiService
import com.example.cryptoexchange.data.service.CryptoApiService
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        val TIME_OUT = 60_000
        return HttpClient(Android) {
            install(JsonFeature) { // todo: remove this?
                serializer =
                    KotlinxSerializer(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )

                engine {
                    connectTimeout = TIME_OUT
                    socketTimeout = TIME_OUT
                }
            }

            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Log.i("Ktor Logger", message)
                        }
                    }
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                url("https://api-pub.bitfinex.com/v2/tickers") // todo: find out why adding the path does not work
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }

    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient): CryptoApiService = BitfinexApiService(httpClient)


}
