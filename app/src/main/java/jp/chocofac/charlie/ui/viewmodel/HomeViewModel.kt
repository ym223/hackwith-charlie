package jp.chocofac.charlie.ui.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.chocofac.charlie.data.model.Param
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
): ViewModel() {
    private val _nowLocationState = MutableStateFlow(NowLocationState())
    var nowLocationState = _nowLocationState.asStateFlow()

    fun startFetch(context: Context) {
        startFetchLocation(context)
    }

    private fun startFetchLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Param.REQUEST_CODE_LOCATION
            )
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            _nowLocationState.value = NowLocationState(location = it)
            Timber.d("lat: ${it.latitude}, lng: ${it.longitude}")
        }
    }
}

data class NowLocationState(
    val location: Location = Location("")
)