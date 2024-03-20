package com.example.cryptoexchange.presentation.ui.crypto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.presentation.theme.CryptoExchangeTheme

@Composable
fun CryptoListRoute(viewModel: CryptoListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    CryptoListScreen(
        uiState,
        viewModel::onEvent
    )
}

@Composable
fun CryptoListScreen(
    uiState: CryptoListViewModel.CryptoListUiState,
    onEvent: (CryptoListViewModel.CryptoListUiEvent) -> Unit
) {
    val cryptoData = uiState.cryptoData

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo: create a search bar
        CryptoList(cryptoData = cryptoData)
    }
}

@Composable
private fun CryptoList(cryptoData: List<CryptoCurrency>) {
    LazyColumn {
        items(cryptoData.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = cryptoData[it].cryptoCurrencySymbol.symbol)
                Text(text = cryptoData[it].ask.toString())
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangePreview() {
    CryptoExchangeTheme {
        CryptoListScreen(
            CryptoListViewModel.CryptoListUiState(
                isLoading = false,
                isNetworkConnectivityActive = true,
                cryptoData = listOf(),
                error = null
            )
        ) {
        }
    }
}
