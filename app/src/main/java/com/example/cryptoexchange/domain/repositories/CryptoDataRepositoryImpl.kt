package com.example.cryptoexchange.domain.repositories

import com.example.cryptoexchange.data.service.CryptoApiService
import com.example.cryptoexchange.domain.models.CryptoCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CryptoDataRepositoryImpl
@Inject
constructor(
    private val apiService: CryptoApiService,
) : CryptoDataRepository {
    override suspend fun getCryptoData(): Flow<List<CryptoCurrency>> {
        return apiService.getCryptoData().map { responseString ->
            CryptoCurrencyMapper.mapToCryptoCurrencyList(responseString)
        }
    }
}



