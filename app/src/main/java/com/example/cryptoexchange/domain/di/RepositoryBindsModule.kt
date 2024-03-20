package com.example.cryptoexchange.domain.di

import com.example.cryptoexchange.domain.repositories.CryptoDataRepository
import com.example.cryptoexchange.domain.repositories.CryptoDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindsModule {
    @Binds
    fun bindsAuthenticationRepository(cryptoDataRepository: CryptoDataRepositoryImpl): CryptoDataRepository
}
