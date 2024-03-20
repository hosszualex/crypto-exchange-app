package com.example.cryptoexchange.tests

import com.example.cryptoexchange.mocks.MockData
import com.example.cryptoexchange.domain.repositories.CryptoCurrencyMapper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CryptoCurrencyMapperTest {
    private val mockCryptoDataRepository = CryptoCurrencyMapper


    @Test
    fun `Map data Successfully`() {
        runTest {
            val value = mockCryptoDataRepository.mapToCryptoCurrencyList(MockData.RESPONSE_CRYPTO_STRING)
            assertEquals(MockData.EXPECTED_MAPPED_CRYPTO_DATA, value)
        }
    }
}