package com.example.cryptoexchange.domain.models

data class CryptoCurrency(
    val worldCurrencySymbol: String = "USD",
    val cryptoCurrencySymbol: CryptoCurrencyEnum,
    val ask: Double,
    val dailyRelativeChange: Double,
)
