package com.example.vkeducation.presentation.screens.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.data.MockApps
import com.example.vkeducation.domain.entity.App
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class AppsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AppsState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppsEvent>()
    val event = _event.asSharedFlow()

    init {
        getAppDetail()
    }
    fun getAppDetail() {
        viewModelScope.launch {
            _state.value = AppsState(apps = MockApps.apps)
        }
    }
    fun onLogoClick() {
        viewModelScope.launch {
            _event.emit(AppsEvent.ShowSnackbar("Snack Rustore"))
        }
    }
}


data class AppsState(
    val apps: List<App> = emptyList()
)

sealed class AppsEvent {
    data class ShowSnackbar(val message: String) : AppsEvent()
}