package com.example.cryptoexchange.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptoexchange.presentation.theme.CryptoExchangeTheme
import com.example.cryptoexchange.presentation.ui.crypto.CryptoListRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoExchangeTheme {
                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = "cryptoExchangeScreen") {
                    composable("setupScreen") {
                        // PhoneRegisterRoute()
                    }
                    composable("cryptoExchangeScreen") {
                        CryptoListRoute()
                    }
                }
            }
        }
    }
}
