package jp.chocofac.charlie.data.service.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import jp.chocofac.charlie.data.model.Param
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    operator fun invoke(context: Context): Location {
        var location = Location("")

        if (context.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Param.REQUEST_CODE_LOCATION
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location = it
            }
        }
        return location
    }
}