package jp.chocofac.charlie.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> get() = _state

    fun onAuthError(e: Exception) {
        _state.value = _state.value.copy(error = e)
    }

    fun onAlertDismiss() {
        _state.value = _state.value.copy(error = null)
    }
}

data class LoginViewState(
    val loading: Boolean = false,
    val data: String = "",
    val error: Exception? = null
)