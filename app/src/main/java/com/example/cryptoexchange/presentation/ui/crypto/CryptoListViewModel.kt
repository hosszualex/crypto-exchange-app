package com.example.cryptoexchange.presentation.ui.crypto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
import com.example.cryptoexchange.domain.ErrorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CryptoListViewModel
@Inject
constructor(
    private val cryptoDataRepository: CryptoDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CryptoListUiState())
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    private var fetchingCryptoDataJob: Job? = null

    fun onEvent(event: CryptoListUiEvent) {
        when (event) {
            is CryptoListUiEvent.OnRepeatFetchCryptoData -> getExchangeCrypto()
            is CryptoListUiEvent.OnStopFetchingCryptoData -> stopFetchingCryptoData()
            is CryptoListUiEvent.OnDismissError -> dismissErrorDialog()
            is CryptoListUiEvent.OnToggleSearch -> toggleSearch()
            is CryptoListUiEvent.OnSearchCrypto -> searchQuery(event.query)
        }
    }

    private fun getExchangeCrypto() {
        Log.i("=====>", "START")
        fetchingCryptoDataJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = shouldShowLoading) }
            while (true) {
                cryptoDataRepository.getCryptoData()
                    .flowOn(Dispatchers.Main)
                    .catch { exception -> setError(exception) }
                    .collect { cryptoData ->
                        Log.i("=====>", "Data Refreshed")
                        val data = cryptoData.sortedBy { crypto -> crypto.cryptoCurrencySymbol.symbol }
                        _uiState.update {
                            it.copy(cryptoData = data)
                        }
                    }
                    .also {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                delay(5000L)
            }
        }
    }

    private fun toggleSearch() {
        val oldState = _uiState.value.searchFilteringState
        _uiState.update {
            it.copy(
                searchFilteringState = SearchCryptoState(
                    text= if (oldState.isSearching) "" else oldState.text,
                    isSearching = !oldState.isSearching
                )
            )
        }
    }

    private fun searchQuery(query: String) {
        val oldState = _uiState.value.searchFilteringState
        val fullData = _uiState.value.cryptoData
        val filteredData =
            if (query.isNotEmpty())
                fullData.filter { it.cryptoCurrencySymbol.symbol.contains(query.uppercase()) }
            else
                fullData
        _uiState.update {
            it.copy(
                searchFilteringState = SearchCryptoState(
                    text= query,
                    isSearching = oldState.isSearching,
                    filteredCryptoData = filteredData
                )
            )
        }
    }

    private fun stopFetchingCryptoData() {
        Log.i("=====>", "CANCEL")
        fetchingCryptoDataJob?.cancel()
    }

    private val shouldShowLoading: Boolean
        get() = _uiState.value.cryptoData.isEmpty()

    private fun setError(throwable: Throwable) {
        _uiState.update { it.copy(errorState = AlertDialogState(true, ErrorUtil.parsedErrorThrowable(throwable))) }
    }

    private fun dismissErrorDialog() {
        _uiState.update { it.copy(errorState = AlertDialogState(false, null)) }
    }

    sealed interface CryptoListUiEvent {
        data object OnRepeatFetchCryptoData : CryptoListUiEvent
        data object OnStopFetchingCryptoData : CryptoListUiEvent
        data object OnDismissError : CryptoListUiEvent
        data object OnToggleSearch : CryptoListUiEvent
        data class OnSearchCrypto(val query: String) : CryptoListUiEvent


    }

    data class CryptoListUiState(
        val isLoading: Boolean = false,
        val isNetworkConnectivityActive: Boolean = true, // todo: implement this
        val cryptoData: List<CryptoCurrency> = listOf(),
        val searchFilteringState: SearchCryptoState = SearchCryptoState(),
        var errorState: AlertDialogState = AlertDialogState()
    )

    data class AlertDialogState(
        val visible: Boolean = false,
        val exception: Throwable? = null
    )
    data class SearchCryptoState(
        val text: String = "",
        val isSearching: Boolean = false,
        val filteredCryptoData: List<CryptoCurrency> = listOf()
    )
}
