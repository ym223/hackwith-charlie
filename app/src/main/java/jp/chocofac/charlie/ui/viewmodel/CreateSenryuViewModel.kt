package jp.chocofac.charlie.ui.viewmodel

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.chocofac.charlie.data.model.PostData
import jp.chocofac.charlie.data.model.toGeoPoint
import jp.chocofac.charlie.data.service.location.FireStoreRepository
import jp.chocofac.charlie.data.service.location.LocationRepository
import jp.chocofac.charlie.data.service.senryu.Senryu
import jp.chocofac.charlie.data.service.senryu.SenryuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateSenryuViewModel @Inject constructor(
    private val senryuRepository: SenryuRepository,
    private val fireStore: FireStoreRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locationState = MutableStateFlow(NowLocationState())
    var locationState = _locationState.asStateFlow()

    private var _senryuViewState = MutableStateFlow<SenryuViewState>(SenryuViewState.Initial)
    val senryuViewState: StateFlow<SenryuViewState> = _senryuViewState

    fun nowLocation(context: Context) {
        _locationState.value = _locationState.value.copy(location = locationRepository(context))
    }

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

    fun postData(first: String, second: String, third: String) {
        fireStore.postSenryuData(
            PostData(
                first = first,
                second = second,
                third = third,
                contributor = "contributor",
                geoPoint = GeoPoint(41.8420509, 140.7673008)
            ),
            onSuccess = {
                Timber.d("$it")
            },
            onFailure = {
                Timber.d("$it")
            }
        )
    }
}

sealed class SenryuViewState {
    object Initial : SenryuViewState()
    object Loading : SenryuViewState()
    data class Success(val senryu: Senryu) : SenryuViewState()
    data class Error(val exception: Throwable) : SenryuViewState()
}