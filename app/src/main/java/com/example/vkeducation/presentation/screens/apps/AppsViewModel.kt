package com.example.vkeducation.presentation.screens.apps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.R
import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.usecase.GetAppsUseCase
import com.example.vkeducation.domain.usecase.RefreshAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getAppsUseCase: GetAppsUseCase, private val refreshAppsUseCase: RefreshAppsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<AppsState>(AppsState.Initial)
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppsEvent>()
    val event = _event.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        getApps()
    }

    private fun getApps() {
        viewModelScope.launch {
            getAppsUseCase().catch { e ->
                Log.e("AppsViewModel", "Error: ${e.message}")
            }.onEach { appShorts ->
                _state.value = AppsState.Success(appShorts)
            }.launchIn(viewModelScope)
        }
    }
    fun refreshApps() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                refreshAppsUseCase.invoke()
                _event.emit(AppsEvent.ShowSnackBar(R.string.apps_update))
            } catch (e: Exception) {
                Log.e("AppsViewModel", "Error refresh: ${e.message}")
                _event.emit(
                    AppsEvent.ShowSnackBar((R.string.error_update))
                )
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun onLogoClick() {
        viewModelScope.launch {
            _event.emit(AppsEvent.ShowSnackBar(R.string.snack_rustore))
        }
    }

    fun processCommand(command: AppsCommand) {
        when (command) {
            AppsCommand.RefreshApps -> refreshApps()

            AppsCommand.LogoClick -> onLogoClick()
        }
    }
}

sealed interface AppsState {
    data object Initial : AppsState
    data class Success(val appShorts: List<AppShort> = emptyList()) : AppsState
    data class Error(val message: String) : AppsState
}

sealed interface AppsCommand {
    data object RefreshApps : AppsCommand

    data object LogoClick : AppsCommand
}

sealed interface AppsEvent {
    data class ShowSnackBar(val message: Int) : AppsEvent
}