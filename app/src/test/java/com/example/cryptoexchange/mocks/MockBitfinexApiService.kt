package com.example.cryptoexchange.mocks

import com.example.cryptoexchange.data.service.CryptoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockBitfinexApiService: CryptoApiService {
    override suspend fun getCryptoData(cryptoSymbols: Set<String>): Flow<String> = flow {
        MockData.RESPONSE_CRYPTO_STRING
    }
}