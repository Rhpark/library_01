package kr.open.rhpark.library.system.service.info.location

import android.location.Location

public sealed class LocationStateEvent {
    public data class OnGpsEnabled(val isEnabled: Boolean) : LocationStateEvent()
    public data class OnFusedEnabled(val isEnabled: Boolean) : LocationStateEvent()
    public data class OnNetworkEnabled(val isEnabled: Boolean) : LocationStateEvent()
    public data class OnPassiveEnabled(val isEnabled: Boolean) : LocationStateEvent()
    public data class OnLocationChanged(val location:Location?) : LocationStateEvent()
}