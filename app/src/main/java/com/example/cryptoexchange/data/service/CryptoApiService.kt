package com.example.cryptoexchange.data.service

import kotlinx.coroutines.flow.Flow

interface CryptoApiService {
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
    ): Flow<String>
}
