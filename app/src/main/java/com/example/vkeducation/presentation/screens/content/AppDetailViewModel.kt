package com.example.vkeducation.presentation.screens.content

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.R
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.usecase.GetAppByIdUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AppDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAppByIdUseCase: GetAppByIdUseCase
) : ViewModel() {

    private val appId: Int = savedStateHandle["id"] ?: 0
    private val _state = MutableStateFlow(AppDetailState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppDetailEvent>()
    val event = _event.asSharedFlow()

    init {
        loadApp(appId)
    }
    fun loadApp(appId: Int) {
        getAppByIdUseCase(appId).onEach { app ->
            _state.value = _state.value.copy(app = app)
        }
            .launchIn(viewModelScope)
    }
    fun onShareClick() {
        viewModelScope.launch {
            _event.emit(AppDetailEvent.ShowSnackBar(R.string.under_developement))
        }
    }
    fun onInstallClick() {
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