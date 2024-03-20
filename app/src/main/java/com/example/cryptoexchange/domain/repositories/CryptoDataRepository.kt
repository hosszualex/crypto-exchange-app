package com.example.cryptoexchange.domain.repositories

import com.example.cryptoexchange.domain.models.CryptoCurrency
import kotlinx.coroutines.flow.Flow

interface CryptoDataRepository {
    suspend fun getCryptoData(): Flow<List<CryptoCurrency>>
}
