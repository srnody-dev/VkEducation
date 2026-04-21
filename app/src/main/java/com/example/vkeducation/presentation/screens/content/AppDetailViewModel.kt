package com.example.vkeducation.presentation.screens.content

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.R
import com.example.vkeducation.data.MockApps
import com.example.vkeducation.domain.entity.App
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppDetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(AppDetailState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppDetailEvent>()
    val event = _event.asSharedFlow()

    fun loadApp(appId: Int) {
        viewModelScope.launch {
            val app = MockApps.apps.find { it.id == appId } ?: MockApps.apps.first()
            _state.value= AppDetailState(app)
        }
    }

    fun onShareClick() {
        viewModelScope.launch {
            _event.emit(AppDetailEvent.ShowSnackBar(R.string.under_developement))
        }
    }

    fun onInstallClick(){
        viewModelScope.launch {
            _event.emit(AppDetailEvent.ShowSnackBar(R.string.under_developement))
        }
    }

    fun onDeveloperClick() {
        viewModelScope.launch {
            _event.emit(AppDetailEvent.ShowSnackBar(R.string.under_developement))
        }
    }
}

data class AppDetailState(
    val app: App? = null
)

sealed class AppDetailEvent {
    data class ShowSnackBar(@StringRes val resId: Int) : AppDetailEvent()
}