package com.example.cryptoexchange.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.cryptoexchange.presentation.theme.CryptoExchangeTheme
import com.example.cryptoexchange.presentation.ui.crypto.CryptoListRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Not using it due to the fact we do not have a top or bottom bar.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoExchangeTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) {
                    CryptoListRoute()
                    SnackbarInternetAvailability(snackbarHostState)
                }
            }
        }
    }

    @Composable
    private fun SnackbarInternetAvailability(snackbarHostState: SnackbarHostState) {
        val isOnline by viewModel.isOnline.collectAsState()
        LaunchedEffect(isOnline) {
            if (isOnline)
                snackbarHostState.showSnackbar(
                    message = "Network connected!",
                    duration = SnackbarDuration.Short,
                )
            else
                snackbarHostState.showSnackbar(
                    message = "Network not connected!",
                    duration = SnackbarDuration.Short,
                )
        }
    }
}
