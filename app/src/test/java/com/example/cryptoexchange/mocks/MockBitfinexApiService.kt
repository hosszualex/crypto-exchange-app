package com.example.cryptoexchange.mocks

import com.example.cryptoexchange.data.service.CryptoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class MockBitfinexApiService(
    private val networkMonitor: MockNetworkMonitor,
) : CryptoApiService {
    override suspend fun getCryptoData(cryptoSymbols: Set<String>): Flow<String> =
        flow {
            if (!networkMonitor.isOnline) {
                throw UnknownHostException()
            } else {
                emit(MockData.RESPONSE_CRYPTO_STRING)
            }
        }
}
