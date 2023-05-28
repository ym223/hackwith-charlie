package jp.chocofac.charlie.ui.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.chocofac.charlie.data.model.Param
import jp.chocofac.charlie.data.model.PostData
import jp.chocofac.charlie.data.model.toGeoPoint
import jp.chocofac.charlie.data.model.toLatLng
import jp.chocofac.charlie.data.service.location.FireStoreRepository
import jp.chocofac.charlie.data.service.location.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val fireStore: FireStoreRepository,
): ViewModel() {
    private val _nowLocationState = MutableStateFlow(NowLocationState())
    var nowLocationState = _nowLocationState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState<PostData>())
    var uiState = _uiState.asStateFlow()

    fun startFetch(context: Context) {
        startFetchCollection()
        startFetchLocation(context)
    }

    private fun startFetchLocation(context: Context) {
        val location = locationRepository(context)
        _nowLocationState.value = NowLocationState(location)
    }

    private fun startFetchCollection() {
        _uiState.value = _uiState.value.copy(loading = true)
        fireStore.fetchCollection(
            onSuccess = {
                _uiState.value = _uiState.value.copy(loading = false, data = it)
            },
            onError = {
                _uiState.value = _uiState.value.copy(loading = false, error = it)
            }
        )
    }
    
    fun onDismissRequest() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun nowLocation(): LatLng {
        return _nowLocationState.value.location.toLatLng()
    }

    fun postData(first: String, second: String, third: String) {
        fireStore.postSenryuData(
            PostData(
                first = first,
                second = second,
                third = third,
                contributor = "contributor",
                geoPoint = nowLocationState.value.location.toGeoPoint()
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

data class NowLocationState(
    val location: Location = Location("")
)

data class UiState<T>(
    val loading: Boolean = false,
    val data: List<T> = emptyList(),
    val error: Exception? = null
)