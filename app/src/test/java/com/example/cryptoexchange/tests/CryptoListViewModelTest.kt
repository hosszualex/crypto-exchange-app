package com.example.cryptoexchange.tests

import com.example.cryptoexchange.MainDispatcherRule
import com.example.cryptoexchange.domain.ErrorUtil
import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.repositories.CryptoDataRepositoryImpl
import com.example.cryptoexchange.mocks.MockBitfinexApiService
import com.example.cryptoexchange.mocks.MockData
import com.example.cryptoexchange.mocks.MockNetworkMonitor
import com.example.cryptoexchange.presentation.ui.crypto.CryptoListViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CryptoListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var cryptoListViewModel: CryptoListViewModel
    private lateinit var networkMonitor: MockNetworkMonitor

    @Before
    fun setup() {
        networkMonitor = MockNetworkMonitor
        cryptoListViewModel =
            CryptoListViewModel(
                cryptoDataRepository =
                    CryptoDataRepositoryImpl(
                        MockBitfinexApiService(
                            networkMonitor,
                        ),
                    ),
            )
    }

    @Test
    fun `Successful view model state initialization`() {
        runTest {
            with(cryptoListViewModel.uiState.value) {
                assertEquals(false, isLoading)
                assertEquals(listOf<CryptoCurrency>(), cryptoData)
                with(searchFilteringState) {
                    assertEquals(false, isSearching)
                    assertEquals("", text)
                    assertEquals(listOf<CryptoCurrency>(), filteredCryptoData)
                }
                with(errorState) {
                    assertEquals(false, visible)
                    assertEquals(null, exception)
                }
            }
        }
    }

    @Test
    fun `Successful Fetching Crypto Data`() {
        runTest {
            networkMonitor.isOnline = true
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            assertEquals(MockData.EXPECTED_MAPPED_CRYPTO_DATA, cryptoListViewModel.uiState.value.cryptoData)
        }
    }

    @Test
    fun `Failed Fetching Crypto Data, no internet connection`() {
        runTest {
            networkMonitor.isOnline = false
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            assertEquals(
                ErrorUtil.ERROR_NO_INTERNET_EXCEPTION,
                cryptoListViewModel.uiState.value.errorState.exception,
            )
        }
    }

    @Test
    fun `Successful error dismissing`() {
        runTest {
            networkMonitor.isOnline = false
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            assertEquals(
                ErrorUtil.ERROR_NO_INTERNET_EXCEPTION,
                cryptoListViewModel.uiState.value.errorState.exception,
            )

            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnDismissError)
            assertFalse(cryptoListViewModel.uiState.value.errorState.visible)
            assertEquals(null, cryptoListViewModel.uiState.value.errorState.exception)
        }
    }

    @Test
    fun `Toggle search deleting text`() {
        runTest {
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto("BTC"))
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            assertFalse(cryptoListViewModel.uiState.value.searchFilteringState.isSearching)
            assertTrue(cryptoListViewModel.uiState.value.searchFilteringState.text.isEmpty())
        }
    }

    @Test
    fun `Working toggle search`() {
        runTest {
            assertFalse(cryptoListViewModel.uiState.value.searchFilteringState.isSearching)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            assertTrue(cryptoListViewModel.uiState.value.searchFilteringState.isSearching)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            assertFalse(cryptoListViewModel.uiState.value.searchFilteringState.isSearching)
        }
    }

    @Test
    fun `Successful crypto search with existing symbol`() {
        runTest {
            networkMonitor.isOnline = true
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto("BTC"))
            assertEquals(
                MockData.EXPECTED_FILTERED_MAPPED_CRYPTO_DATA,
                cryptoListViewModel.uiState.value.searchFilteringState.filteredCryptoData,
            )
        }
    }

    @Test
    fun `Successful crypto search with non existing symbol`() {
        runTest {
            networkMonitor.isOnline = true
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto("EEET"))
            assertEquals(listOf<CryptoCurrency>(), cryptoListViewModel.uiState.value.searchFilteringState.filteredCryptoData)
        }
    }

    @Test
    fun `Successful crypto search with empty symbol`() {
        runTest {
            networkMonitor.isOnline = true
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
            cryptoListViewModel.onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto(""))
            assertEquals(MockData.EXPECTED_MAPPED_CRYPTO_DATA, cryptoListViewModel.uiState.value.searchFilteringState.filteredCryptoData)
        }
    }
}
