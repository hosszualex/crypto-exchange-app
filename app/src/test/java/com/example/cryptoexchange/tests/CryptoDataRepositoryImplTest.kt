package com.example.cryptoexchange.tests


import com.example.cryptoexchange.mocks.MockBitfinexApiService
import com.example.cryptoexchange.mocks.MockData
import com.example.cryptoexchange.domain.repositories.CryptoDataRepositoryImpl
import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class CryptoDataRepositoryImplTest {
    private lateinit var mockCryptoDataRepository: CryptoDataRepository

    @Before
    fun setup() {
        mockCryptoDataRepository =
            CryptoDataRepositoryImpl(MockBitfinexApiService())
    }

    @Test
    fun `Fetching Crypto Data Succesful`() {
        runTest {
            mockCryptoDataRepository.getCryptoData().collect { cryptoData ->
                assertEquals(MockData.EXPECTED_MAPPED_CRYPTO_DATA, cryptoData)
            }
        }
    }
}
