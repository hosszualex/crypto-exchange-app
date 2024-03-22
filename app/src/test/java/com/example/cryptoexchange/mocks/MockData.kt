package com.example.cryptoexchange.mocks

import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.models.CryptoCurrencyEnum

object MockData {
    val EXPECTED_MAPPED_CRYPTO_DATA =
        listOf<CryptoCurrency>(
            CryptoCurrency(
                cryptoCurrencySymbol = CryptoCurrencyEnum.BTC,
                priceOfLastTrade = 63888.0,
                dailyRelativeChange = 1.39422,
            ),
            CryptoCurrency(
                cryptoCurrencySymbol = CryptoCurrencyEnum.ETH,
                priceOfLastTrade = 3345.3,
                dailyRelativeChange = 2.870666,
            ),
        )

    val EXPECTED_FILTERED_MAPPED_CRYPTO_DATA =
        listOf<CryptoCurrency>(
            CryptoCurrency(
                cryptoCurrencySymbol = CryptoCurrencyEnum.BTC,
                priceOfLastTrade = 63888.0,
                dailyRelativeChange = 1.39422,
            ),
        )

    const val RESPONSE_CRYPTO_STRING = "[[tBTCUSD,63887,13.51369442,63888,5.26274073,879,0.0139422,63925,5297.97066607,65870,60870],[tETHUSD,3344.5,148.08215215,3345.3, 114.04175367, 93.4, 0.02870666, 3347, 16136.00789751, 3374.4, 3063.1]]"
}
