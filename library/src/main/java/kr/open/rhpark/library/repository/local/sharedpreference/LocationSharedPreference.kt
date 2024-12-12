package kr.open.rhpark.library.repository.local.sharedpreference

import android.content.Context
import android.location.Location
import android.os.Build
import kr.open.rhpark.library.util.inline.sdk_version.checkSdkVersion

public class LocationSharedPreference(context: Context) :
    BaseSharedPreference(context, "RhParkLocation") {

    private val altitudeKey = "altitude"
    private val accuracyKey = "accuracy"
    private val bearingKey = "bearing"
    private val bearingAccuracyDegreesKey = "bearingAccuracyDegrees"
    private val isMockKey = "isMock"
    private val elapsedRealtimeUncertaintyNanosKey = "elapsedRealtimeUncertaintyNanos"
    private val mslAltitudeMetersKey = "mslAltitudeMeters"
    private val mslAltitudeAccuracyMetersKey = "mslAltitudeAccuracyMeters"
    private val latitudeKey = "latitude"
    private val longitudeKey = "longitude"
    private val providerKey = "provider"
    private val speedKey = "speed"
    private val timeKey = "time"
    private val verticalAccuracyMeterKey = "verticalAccuracyMeters"


    private fun putLocation(key: String, location: Location) {
        put(key + altitudeKey, location.altitude)
        put(key + accuracyKey, location.accuracy)
        put(key + bearingKey, location.bearing)
        put(key + bearingAccuracyDegreesKey, location.bearingAccuracyDegrees)

        checkSdkVersion(Build.VERSION_CODES.S) {
            put(key + isMockKey, location.isMock)
        }
        checkSdkVersion(Build.VERSION_CODES.Q) {
            put(key + elapsedRealtimeUncertaintyNanosKey, location.elapsedRealtimeUncertaintyNanos)
        }
        checkSdkVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            put(key + mslAltitudeMetersKey, location.mslAltitudeMeters)
            put(key + mslAltitudeAccuracyMetersKey, location.mslAltitudeAccuracyMeters)
        }
        put(key + latitudeKey, location.latitude)
        put(key + longitudeKey, location.longitude)
        put(key + providerKey, location.provider)
        put(key + speedKey, location.speed)
        put(key + timeKey, location.time)
        put(key + verticalAccuracyMeterKey, location.verticalAccuracyMeters)
    }

    public fun saveApplyLocation(key: String, location: Location) {
        putLocation(key, location)
        saveApply()
    }

    public fun saveCommitLocation(key: String, location: Location) {
        putLocation(key, location)
        saveCommit()
    }

    public fun loadLocation(key: String): Location? {
        return getString(key + providerKey, null)?.let {
            Location(it).apply {
                altitude = getDouble(key + altitudeKey, 0.0)
                accuracy = getFloat(key + accuracyKey, 0.0f)
                bearing = getFloat(key + bearingKey, 0.0f)
                bearingAccuracyDegrees = getFloat(key + bearingAccuracyDegreesKey, 0.0f)

                checkSdkVersion(Build.VERSION_CODES.S) {
                    isMock = getBoolean(key + isMockKey, false)
                }
                checkSdkVersion(Build.VERSION_CODES.Q) {
                    elapsedRealtimeUncertaintyNanos =
                        getDouble(key + elapsedRealtimeUncertaintyNanosKey, 0.0)
                }
                checkSdkVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    mslAltitudeMeters = getFloat(key + mslAltitudeMetersKey, 0.0f).toDouble()
                    mslAltitudeAccuracyMeters = getFloat(key + mslAltitudeAccuracyMetersKey, 0.0f)

                }
                latitude = getFloat(key + latitudeKey, 0.0f).toDouble()
                longitude = getFloat(key + longitudeKey, 0.0f).toDouble()

                speed = getFloat(key + speedKey, 0.0f)
                time = getLong(key + timeKey, 0)
                verticalAccuracyMeters = getFloat(key + verticalAccuracyMeterKey, 0.0f)
            }
        }
    }
}