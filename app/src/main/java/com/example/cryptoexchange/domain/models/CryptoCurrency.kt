package com.example.cryptoexchange.domain.models

data class CryptoCurrency(
    val worldCurrencySymbol: String = "USD",
    val cryptoCurrencySymbol: CryptoCurrencyEnum,
    val priceOfLastTrade: Double,
    val dailyRelativeChange: Double,
)
