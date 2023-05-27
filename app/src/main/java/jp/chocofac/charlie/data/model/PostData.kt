package jp.chocofac.charlie.data.model

import com.google.firebase.firestore.GeoPoint

data class PostData(
    val first: String = "",
    val second: String = "",
    val third: String = "",
    val contributorId: String = "",
    val contributor: String = "Unknown",
    val geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
)

fun Map<String, Any?>.fromMapToPostData(): PostData {
    return PostData(
        first = this["first"] as String,
        second = this["second"] as String,
        third = this["third"] as String,
        contributorId = this["contributorId"] as String,
        contributor = this["contributor"] as String,
        geoPoint = (this["geoPoint"] ?: GeoPoint(0.0, 0.0)) as GeoPoint
    )
}
