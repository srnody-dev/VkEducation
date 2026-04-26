package com.example.vkeducation.presentation.screens.apps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.usecase.LoadAppsUseCase
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
    private val loadAppsUseCase: LoadAppsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppsState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppsEvent>()
    val event = _event.asSharedFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            loadAppsUseCase().catch { e ->
                Log.e("AppsViewModel", "Error: ${e.message}")
                _state.value = _state.value.copy(apps = emptyList())
            }.collect { apps ->
                _state.value = _state.value.copy(apps = apps)


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
    val apps: List<App> = emptyList()
)

sealed class AppsEvent {
    data class ShowSnackBar(val message: String) : AppsEvent()
}