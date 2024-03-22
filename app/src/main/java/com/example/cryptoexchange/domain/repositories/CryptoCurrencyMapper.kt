package com.example.cryptoexchange.domain.repositories

import com.example.cryptoexchange.domain.models.CryptoCurrency
import com.example.cryptoexchange.domain.models.CryptoCurrencyEnum
import com.example.cryptoexchange.domain.removeStrings
import org.json.JSONArray

object CryptoCurrencyMapper {
    fun mapToCryptoCurrencyList(jsonString: String): List<CryptoCurrency> {
        val cryptoCurrencyList = mutableListOf<CryptoCurrency>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val innerArray = jsonArray.getJSONArray(i)
            val cryptoSymbol = mapCryptoCurrencySymbol(innerArray.getString(0))
            if (cryptoSymbol != CryptoCurrencyEnum.UNKNOWN) {
                cryptoCurrencyList.add(
                    CryptoCurrency(
                        cryptoCurrencySymbol = cryptoSymbol,
                        ask = innerArray.getDouble(3),
                        dailyRelativeChange = innerArray.getDouble(6) * 100,
                    ),
                )
            }
        }
        return cryptoCurrencyList
    }

    private fun mapCryptoCurrencySymbol(value: String): CryptoCurrencyEnum {
        return when (value.removeStrings(setOf("t", "USD", ":"))) {
            CryptoCurrencyEnum.BTC.symbol -> CryptoCurrencyEnum.BTC
            CryptoCurrencyEnum.ETH.symbol -> CryptoCurrencyEnum.ETH
            CryptoCurrencyEnum.BORG.symbol -> CryptoCurrencyEnum.BORG
            CryptoCurrencyEnum.LTC.symbol -> CryptoCurrencyEnum.LTC
            CryptoCurrencyEnum.XRP.symbol -> CryptoCurrencyEnum.XRP
            CryptoCurrencyEnum.DSH.symbol -> CryptoCurrencyEnum.DSH
            CryptoCurrencyEnum.RRT.symbol -> CryptoCurrencyEnum.RRT
            CryptoCurrencyEnum.SAN.symbol -> CryptoCurrencyEnum.SAN
            CryptoCurrencyEnum.EOS.symbol -> CryptoCurrencyEnum.EOS
            CryptoCurrencyEnum.DAT.symbol -> CryptoCurrencyEnum.DAT
            CryptoCurrencyEnum.SNT.symbol -> CryptoCurrencyEnum.SNT
            CryptoCurrencyEnum.DOGE.symbol -> CryptoCurrencyEnum.DOGE
            CryptoCurrencyEnum.LUNA.symbol -> CryptoCurrencyEnum.LUNA
            CryptoCurrencyEnum.MATIC.symbol -> CryptoCurrencyEnum.MATIC
            CryptoCurrencyEnum.NEXO.symbol -> CryptoCurrencyEnum.NEXO
            CryptoCurrencyEnum.OCEAN.symbol -> CryptoCurrencyEnum.OCEAN
            CryptoCurrencyEnum.BEST.symbol -> CryptoCurrencyEnum.BEST
            CryptoCurrencyEnum.AAVE.symbol -> CryptoCurrencyEnum.AAVE
            CryptoCurrencyEnum.PLU.symbol -> CryptoCurrencyEnum.PLU
            CryptoCurrencyEnum.FIL.symbol -> CryptoCurrencyEnum.FIL
            else -> CryptoCurrencyEnum.UNKNOWN
        }
    }
}
