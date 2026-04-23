package com.example.vkeducation.presentation.screens.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.usecase.GetAppsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AppsViewModel(
    private val getAppsUseCase: GetAppsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppsState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppsEvent>()
    val event = _event.asSharedFlow()

    init {
        getAppDetail()
    }

    private fun getAppDetail() {
        getAppsUseCase()
            .onEach { apps ->
                _state.value = _state.value.copy(apps = apps)
            }
            .launchIn(viewModelScope)
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