package com.example.cryptoexchange.data.service

import com.example.cryptoexchange.domain.toStringForSymbols
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BitfinexApiService
    @Inject
    constructor(
        private val ktorHttpClient: HttpClient,
    ) : CryptoApiService {
        override suspend fun getCryptoData(cryptoSymbols: Set<String>): Flow<String> =
            flow {
                emit(
                    ktorHttpClient.get {
                        url("/v2/tickers")
                        parameter(
                            "symbols",
                            cryptoSymbols.toStringForSymbols(),
                        )

                    }.body()
                )
            }
    }
