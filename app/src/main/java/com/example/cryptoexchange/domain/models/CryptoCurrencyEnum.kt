package com.example.cryptoexchange.domain.models

import com.example.cryptoexchange.R

enum class CryptoCurrencyEnum(val symbol: String, val displayName: String, val imageReource: Int) {
    BTC("BTC", "Bitcoin", R.drawable.ic_bitcoin),
    ETH("ETH", "Ethereum", R.drawable.ic_ethereum),
    BORG("BORG", "SwissBorg", R.drawable.ic_genericcoin),
    LTC("LTC", "Litecoin", R.drawable.ic_genericcoin),
    XRP("XRP", "XRP", R.drawable.ic_xrp),
    DSH("DSH", "Dashcoin", R.drawable.ic_dashcoin),
    RRT("RRT", "Recovery Right Token", R.drawable.ic_genericcoin),
    EOS("EOS", "EOS", R.drawable.ic_genericcoin),
    SAN("SAN", "Santiment Network Token", R.drawable.ic_genericcoin),
    DAT("DAT", "Datum", R.drawable.ic_genericcoin),
    SNT("SNT", "Status", R.drawable.ic_genericcoin),
    DOGE("DOGE","Dogecoin",  R.drawable.ic_genericcoin),
    LUNA("LUNA", "Terra", R.drawable.ic_genericcoin),
    MATIC("MATIC", "Polygon", R.drawable.ic_genericcoin),
    NEXO("NEXO", "Nexo", R.drawable.ic_genericcoin),
    OCEAN("OCEAN", "Ocean Protocol", R.drawable.ic_genericcoin),
    BEST("BEST", "Bitpanda Ecosystem Token", R.drawable.ic_genericcoin),
    AAVE("AAVE", "Aave", R.drawable.ic_genericcoin),
    PLU("PLU", "Pluton", R.drawable.ic_genericcoin),
    FIL("FIL", "Filecoin", R.drawable.ic_genericcoin),
    UNKNOWN("UNKNOWN", "UNKNOWN",-1)
}