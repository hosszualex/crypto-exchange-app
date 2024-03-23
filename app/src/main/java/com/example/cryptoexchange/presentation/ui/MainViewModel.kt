package com.example.cryptoexchange.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoexchange.domain.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        networkMonitor: NetworkMonitor,
    ) : ViewModel() {
        val isOnline =
            networkMonitor.isOnline
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = false,
                )
    }
