package com.example.cryptoexchange.domain.di

import com.example.cryptoexchange.domain.network.ConnectivityManagerNetworkMonitor
import com.example.cryptoexchange.domain.network.NetworkMonitor
import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
import com.example.cryptoexchange.domain.repositories.CryptoDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    fun bindsAuthenticationRepository(cryptoDataRepository: CryptoDataRepositoryImpl): CryptoDataRepository

    @Binds
    fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}
