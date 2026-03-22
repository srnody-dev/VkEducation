package com.example.vkeducation.presentation.screens.content

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.R
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.usecase.LoadAppByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadAppByIdUseCase: LoadAppByIdUseCase
) : ViewModel() {

    private val appId: String = savedStateHandle["id"] ?: ""
    private val _state = MutableStateFlow(AppDetailState())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppDetailEvent>()
    val event = _event.asSharedFlow()

    init {
        loadAppById(appId)
    }

    fun loadAppById(appId: String) {
        viewModelScope.launch {
            try {
                loadAppByIdUseCase(appId).collect { app ->
                    _state.value = _state.value.copy(app = app)
                }
            } catch (e: Exception) {
                Log.e("AppDetailViewModel", "Error: ${e.message}")
            }
        }
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
