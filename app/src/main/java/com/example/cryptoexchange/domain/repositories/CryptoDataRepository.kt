package com.example.cryptoexchange.domain.repositories

import com.example.cryptoexchange.domain.models.CryptoCurrency
import kotlinx.coroutines.flow.Flow

interface CryptoDataRepository {
    suspend fun getCryptoData(
        cryptoSymbols: Set<String> =
            setOf(
                "tBTCUSD",
                "tETHUSD",
                "tBORG:USD",
                "tLTCUSD",
                "tXRPUSD",
                "tDSHUSD",
                "tRRTUSD",
                "tEOSUSD",
                "tSANUSD",
                "tDATUSD",
                "tSNTUSD",
                "tDOGE:USD",
                "tLUNA:USD",
                "tMATIC:USD",
                "tNEXO:USD",
                "tOCEAN:USD",
                "tBEST:USD",
                "tAAVE:USD",
                "tPLUUSD",
                "tFILUSD",
            ),
    ): Flow<List<CryptoCurrency>>
}
