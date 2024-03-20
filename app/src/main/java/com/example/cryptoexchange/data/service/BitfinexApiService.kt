package com.example.cryptoexchange.data.service

import android.util.Log
import com.example.cryptoexchange.domain.toStringForSymbols
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BitfinexApiService
@Inject
constructor(
    private val ktorHttpClient: HttpClient,
) : CryptoApiService {
    override suspend fun getCryptoData(cryptoSymbols: Set<String>) =
        flow {
            Log.i("=====>", "Called")
            emit(ktorHttpClient.get<String>("/v2/tickers") {
                parameter(
                    "symbols",
                    cryptoSymbols.toStringForSymbols()
                )
            })
        }
}
