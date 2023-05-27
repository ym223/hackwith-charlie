package jp.chocofac.charlie.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.chocofac.charlie.data.service.senryu.Senryu
import jp.chocofac.charlie.data.service.senryu.SenryuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateSenryuViewModel @Inject constructor(
    private val senryuRepository: SenryuRepository
) : ViewModel() {

    private var _senryuViewState = MutableStateFlow<SenryuViewState>(SenryuViewState.Initial)
    val senryuViewState: StateFlow<SenryuViewState> = _senryuViewState

    fun postImpressions(impressions: String) {
        viewModelScope.launch {
            Timber.d(impressions)
            runCatching {
                _senryuViewState.value = SenryuViewState.Loading
                senryuRepository.postImpressions(impressions).body()
            }.onSuccess {
                if (it != null) {
                    _senryuViewState.value = SenryuViewState.Success(it)
                } else {
                    _senryuViewState.value = SenryuViewState.Error(Throwable())
                }
                Timber.tag("success").d(it.toString())
            }.onFailure {
                _senryuViewState.value = SenryuViewState.Error(it)
                Timber.tag("failure")
            }
        }
    }
}

sealed class SenryuViewState {
    object Initial : SenryuViewState()
    object Loading : SenryuViewState()
    data class Success(val senryu: Senryu) : SenryuViewState()
    data class Error(val exception: Throwable) : SenryuViewState()
}