package kr.open.rhpark.library.repository.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences.Editor
import android.location.Location
import android.os.Build
import kr.open.rhpark.library.util.inline.sdk_version.checkSdkVersion

public open class LocationSharedPreference(context: Context) :
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


    private fun putLocation(key: String, location: Location): Editor = getEditor().apply {
        this.putValue(key + altitudeKey, location.altitude)
        this.putValue(key + accuracyKey, location.accuracy)
        this.putValue(key + bearingKey, location.bearing)
        this.putValue(key + bearingAccuracyDegreesKey, location.bearingAccuracyDegrees)

        checkSdkVersion(Build.VERSION_CODES.S) {
            this.putValue(key + isMockKey, location.isMock)
        }
        checkSdkVersion(Build.VERSION_CODES.Q) {
            this.putValue(key + elapsedRealtimeUncertaintyNanosKey, location.elapsedRealtimeUncertaintyNanos)
        }
        checkSdkVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            this.putValue(key + mslAltitudeMetersKey, location.mslAltitudeMeters)
            this.putValue(key + mslAltitudeAccuracyMetersKey, location.mslAltitudeAccuracyMeters)
        }
        this.putValue(key + latitudeKey, location.latitude)
        this.putValue(key + longitudeKey, location.longitude)
        this.putValue(key + providerKey, location.provider)
        this.putValue(key + speedKey, location.speed)
        this.putValue(key + timeKey, location.time)
        this.putValue(key + verticalAccuracyMeterKey, location.verticalAccuracyMeters)
    }

    public fun saveApplyLocation(key: String, location: Location) {
        putLocation(key, location).apply()
    }

    public suspend fun saveCommitLocation(key: String, location: Location) {
        commitDoWork{ putLocation(key, location) }
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