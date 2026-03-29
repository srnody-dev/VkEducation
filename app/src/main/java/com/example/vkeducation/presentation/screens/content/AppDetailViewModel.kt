package com.example.vkeducation.presentation.screens.content

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.R
import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.repository.AppRepository
import com.example.vkeducation.domain.usecase.GetAppDetailsByIdUseCase
import com.example.vkeducation.domain.usecase.ToggleWishlistUseCase
import com.example.vkeducation.presentation.screens.content.AppDetailCommand.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAppDetailsByIdUseCase: GetAppDetailsByIdUseCase,
    private val toggleWishlistUseCase: ToggleWishlistUseCase,
    private val repository: AppRepository
) : ViewModel() {

    private val appId: String = savedStateHandle["id"] ?: ""
    private val _state = MutableStateFlow<AppDetailState>(AppDetailState.Initial)
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<AppDetailCommand>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.observeAppDetails(appId)
                .catch { e ->
                    Log.e("AppDetailViewModel", "Error observing", e)
                    _state.value = AppDetailState.Details(appDetails = null)
                    _event.emit(AppDetailCommand.ShowSnackBar(R.string.error_loading_details))
                }
                .collect { appDetails ->
                    if (appDetails != null) {
                        val currentCollapsed = (_state.value as? AppDetailState.Details)?.descriptionCollapsed ?: true
                        _state.value = AppDetailState.Details(
                            appDetails = appDetails,
                            descriptionCollapsed = currentCollapsed
                        )
                    } else if (_state.value is AppDetailState.Initial) {
                        loadAppDetails()
                    }
                }
        }
    }

    private fun loadAppDetails() {
        viewModelScope.launch {
            try {
                val appDetails = getAppDetailsByIdUseCase(appId)
                if (appDetails != null) {
                    _state.value = AppDetailState.Details(
                        appDetails = appDetails,
                        descriptionCollapsed = true
                    )
                }
            } catch (e: Exception) {
                Log.e("AppDetailViewModel", "Error loading: ${e.message}")
                _event.emit(AppDetailCommand.ShowSnackBar(R.string.error_loading_details))
            }
        }
    }

    fun processCommand(command: AppDetailCommand){
        when(command){
            Back -> {
                _state.update { AppDetailState.Finished }
            }
            is ShowSnackBar -> {
                viewModelScope.launch {
                    _event.emit(command)
                }
            }

            Install -> {
                viewModelScope.launch {
                    _event.emit(ShowSnackBar(R.string.under_developement))
                }
            }
            Share -> {
                viewModelScope.launch {
                    _event.emit(ShowSnackBar(R.string.under_developement))
                }
            }
            DeveloperClick -> {
                viewModelScope.launch {
                    _event.emit(ShowSnackBar(R.string.under_developement))
                }
            }

            is ToggleWishlist -> {
                viewModelScope.launch {
                    try {
                        toggleWishlistUseCase(command.id)
                    }
                    catch (e: Exception){
                        Log.e("AppDetailViewModel", "Error toggling wishlist", e)
                        _event.emit(ShowSnackBar(R.string.error_toggling_wishlist))
                    }
                }
            }

            ToggleDescription -> {
                val currentState = _state.value
                if (currentState is AppDetailState.Details && currentState.appDetails != null) {
                    _state.value = currentState.copy(
                        descriptionCollapsed = !currentState.descriptionCollapsed
                    )
                }
            }
        }
    }
}

sealed interface AppDetailState {
    data class Details(
        val appDetails: AppDetails? = null,
        val descriptionCollapsed: Boolean = true
    ) : AppDetailState

    data object Initial : AppDetailState

    data object Finished: AppDetailState


}

sealed interface AppDetailCommand {
    data class ShowSnackBar(@StringRes val resId: Int) : AppDetailCommand
    data class ToggleWishlist(val id: String) : AppDetailCommand

    data object Back : AppDetailCommand

    data object Install:AppDetailCommand

    data object Share :AppDetailCommand

    data object DeveloperClick:AppDetailCommand

    data object ToggleDescription : AppDetailCommand
}
