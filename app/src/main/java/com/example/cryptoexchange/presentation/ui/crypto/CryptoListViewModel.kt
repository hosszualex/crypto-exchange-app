package com.example.cryptoexchange.presentation.ui.crypto

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
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
    var email by mutableStateOf("dragana@asd.com") // todo: delete test values
        private set
    private var fetchingCryptoDataJob: Job? = null
    fun onEvent(event: CryptoListUiEvent) {
        when (event) {
            is CryptoListUiEvent.OnRepeatFetchCryptoData -> getExchangeCrypto()
            is CryptoListUiEvent.OnStopFetchingCryptoData -> stopFetchingCryptoData()
            else -> Unit
        }
    }

    private fun onError(throwable: Throwable) {

    }

    private fun getExchangeCrypto() {
//        if (_uiState.value.isLoading)
//            return
        Log.i("=====>", "START")

        fetchingCryptoDataJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = shouldShowLoading) }
            while (true) {
                cryptoDataRepository.getCryptoData()
                    .flowOn(Dispatchers.Main)
                    .catch { exception -> setError(exception) }
                    .collect { cryptoData ->
                        Log.i("=====>", "Data Refreshed")
                        _uiState.update {
                            it.copy(cryptoData = cryptoData.sortedBy { crypto -> crypto.cryptoCurrencySymbol.symbol })
                        }
                    }
                    .also {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                delay(10000L)
            }
        }
    }

    private fun stopFetchingCryptoData() {
        Log.i("=====>", "CANCEL")
        fetchingCryptoDataJob?.cancel()
    }

    private val shouldShowLoading: Boolean
        get() = _uiState.value.cryptoData.isNullOrEmpty()

    private fun setError(throwable: Throwable) {
        _uiState.update { it.copy(error = throwable) }
        Log.i("===ERROR===>", throwable.message.toString())
    }
    sealed interface CryptoListUiEvent {
        data object OnRepeatFetchCryptoData : CryptoListUiEvent
        data object OnStopFetchingCryptoData: CryptoListUiEvent
        data class Error(val errorMessage: String) : CryptoListUiEvent
    }
    data class CryptoListUiState(
        val isLoading: Boolean = false,
        val isNetworkConnectivityActive: Boolean = true, // todo: implement this
        val cryptoData: List<CryptoCurrency> = listOf(),
        val error: Throwable? = null,
    )
}
