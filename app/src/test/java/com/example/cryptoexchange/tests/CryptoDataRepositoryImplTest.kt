package com.example.cryptoexchange.tests

import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
import com.example.cryptoexchange.domain.repositories.CryptoDataRepositoryImpl
import com.example.cryptoexchange.mocks.MockBitfinexApiService
import com.example.cryptoexchange.mocks.MockData
import com.example.cryptoexchange.mocks.MockNetworkMonitor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class CryptoDataRepositoryImplTest {
    private lateinit var mockCryptoDataRepository: CryptoDataRepository
    private lateinit var networkMonitor: MockNetworkMonitor

    @Before
    fun setup() {
        networkMonitor = MockNetworkMonitor
        mockCryptoDataRepository =
            CryptoDataRepositoryImpl(MockBitfinexApiService(networkMonitor))
    }

    @Test
    fun `Fetching Crypto Data Successful`() {
        runTest {
            networkMonitor.isOnline = true
            mockCryptoDataRepository.getCryptoData().collect { cryptoData ->
                assertEquals(MockData.EXPECTED_MAPPED_CRYPTO_DATA, cryptoData)
            }
        }
    }

    @Test
    fun `Fetching Crypto has failed, unknown host`() {
        runTest {
            networkMonitor.isOnline = false
            mockCryptoDataRepository.getCryptoData().catch { throwable ->
                assertEquals(UnknownHostException(), throwable)
            }
        }
    }
}
