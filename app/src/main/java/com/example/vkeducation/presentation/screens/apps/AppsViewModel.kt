package com.example.vkeducation.presentation.screens.apps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.usecase.GetAppsUseCase
import com.example.vkeducation.domain.usecase.RefreshAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getAppsUseCase: GetAppsUseCase,
    private val refreshAppsUseCase: RefreshAppsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppsState())
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
            try {
                getAppsUseCase().collect { apps ->
                    _state.value = _state.value.copy(appShorts = apps)
                }
            } catch (e: Exception) {
                Log.e("AppsViewModel", "Error: ${e.message}")
                _state.value = _state.value.copy(apps = emptyList())
            }.collect { apps ->
                _state.value = _state.value.copy(apps = apps)


            }
        }
    }

    fun refreshApps() {
        viewModelScope.launch {
            try {
                refreshAppsUseCase.invoke()
            } catch (e: Exception) {
                Log.e("AppsViewModel", "Error refresh: ${e.message}")

            }
        }
    }
    
    fun onLogoClick() {
        viewModelScope.launch {
            _event.emit(AppsEvent.ShowSnackBar("Snack Rustore"))
        }
    }
}

data class AppsState(
    val appShorts: List<AppShort> = emptyList()
)

sealed class AppsEvent {
    data class ShowSnackBar(val message: String) : AppsEvent()
}