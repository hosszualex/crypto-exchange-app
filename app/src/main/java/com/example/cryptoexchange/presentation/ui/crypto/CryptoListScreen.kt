package com.example.cryptoexchange.presentation.ui.crypto

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.cryptoexchange.domain.formatDailyRelativeChange
import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.models.CryptoCurrencyEnum
import com.example.cryptoexchange.presentation.theme.CryptoExchangeTheme
import com.example.cryptoexchange.presentation.theme.DarkGreen
import com.example.cryptoexchange.presentation.ui.ErrorDialog
import com.example.cryptoexchange.presentation.ui.ProgressIndicator

@Composable
fun CryptoListRoute(viewModel: CryptoListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    CryptoListScreen(
        uiState,
        viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreen(
    uiState: CryptoListViewModel.CryptoListUiState,
    onEvent: (CryptoListViewModel.CryptoListUiEvent) -> Unit,
) {
    val cryptoData = uiState.cryptoData

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onEvent(CryptoListViewModel.CryptoListUiEvent.OnRepeatFetchCryptoData)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        onEvent(CryptoListViewModel.CryptoListUiEvent.OnStopFetchingCryptoData)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CryptoSearchBar(uiState, onEvent, cryptoData)
        CryptoList(cryptoData = cryptoData)
    }

    AnimatedVisibility(
        visible = uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ProgressIndicator()
    }

    with(uiState.errorState) {
        if (visible) {
            ErrorDialog(
                exception?.message.toString(),
                exception?.cause?.message.toString(),
            ) {
                onEvent(CryptoListViewModel.CryptoListUiEvent.OnDismissError)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CryptoSearchBar(
    uiState: CryptoListViewModel.CryptoListUiState,
    onEvent: (CryptoListViewModel.CryptoListUiEvent) -> Unit,
    cryptoData: List<CryptoCurrency>,
) {
    SearchBar(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(bottom = 8.dp),
        query = uiState.searchFilteringState.text,
        onQueryChange = { newText ->
            onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto(newText))
        },
        onSearch = { newText ->
            onEvent(CryptoListViewModel.CryptoListUiEvent.OnSearchCrypto(newText))
        },
        active = uiState.searchFilteringState.isSearching,
        onActiveChange = {
            onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
        },
        placeholder = {
            Text(text = "Search Symbol")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "crypto search icon")
        },
        trailingIcon = {
            if (uiState.searchFilteringState.isSearching) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "crypto close icon",
                    modifier =
                        Modifier.clickable {
                            onEvent(CryptoListViewModel.CryptoListUiEvent.OnToggleSearch)
                        },
                )
            }
        },
    ) {
        val filteredCryptoData =
            if (uiState.searchFilteringState.filteredCryptoData.isEmpty() && uiState.searchFilteringState.text.isEmpty()) {
                cryptoData
            } else {
                uiState.searchFilteringState.filteredCryptoData
            }

        if (filteredCryptoData.isEmpty()) {
            Text(
                text = "No cryptocurrency matches that symbol",
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
            )
        } else {
            AnimatedContent(
                targetState = filteredCryptoData,
                label = "search animation update",
                transitionSpec = {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                },
            ) { list ->
                CryptoList(cryptoData = list)
            }
        }
    }
}

@Composable
private fun CryptoList(cryptoData: List<CryptoCurrency>) {
    LazyColumn {
        items(
            count = cryptoData.size,
            key = {
                cryptoData[it].cryptoCurrencySymbol.symbol
            },
        ) {
            val currentCrypto = cryptoData[it]
            CryptoCurrencyItem(currentCrypto)
        }
    }
}

@Composable
private fun CryptoCurrencyItem(currentCrypto: CryptoCurrency) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(currentCrypto.cryptoCurrencySymbol.imageReource),
                contentDescription = "arrow down",
                tint = Color.Unspecified,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = currentCrypto.cryptoCurrencySymbol.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier =
                            Modifier
                                .padding(vertical = 2.dp)
                                .weight(1f),
                    )

                    AnimatedContent(
                        targetState = currentCrypto.ask,
                        label = "ask price animation update",
                        transitionSpec = {
                            slideInVertically { it } togetherWith slideOutVertically { -it }
                        },
                    ) { value ->
                        Text(
                            text = "$ $value",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier =
                                Modifier
                                    .padding(vertical = 2.dp)
                                    .graphicsLayer {
                                        alpha = 1.0f
                                    },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = currentCrypto.cryptoCurrencySymbol.symbol,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )

                    AnimatedContent(
                        targetState = currentCrypto.dailyRelativeChange,
                        label = "percent change animation update",
                        transitionSpec = {
                            slideInVertically { it } togetherWith slideOutVertically { -it }
                        },
                    ) { dailyRelativeChange ->
                        Text(
                            text = dailyRelativeChange.formatDailyRelativeChange(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (dailyRelativeChange >= 0) DarkGreen else Color.Red,
                            modifier = Modifier.padding(vertical = 2.dp),
                        )
                    }
                }
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
                cryptoData =
                    listOf<CryptoCurrency>(
                        CryptoCurrency(
                            cryptoCurrencySymbol = CryptoCurrencyEnum.BTC,
                            ask = 63888.0,
                            dailyRelativeChange = 1.39422,
                        ),
                        CryptoCurrency(
                            cryptoCurrencySymbol = CryptoCurrencyEnum.ETH,
                            ask = 3345.3,
                            dailyRelativeChange = -2.870666,
                        ),
                    ),
                errorState = CryptoListViewModel.AlertDialogState(),
            ),
        ) {
        }
    }
}
