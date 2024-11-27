package kr.open.rhpark.library.system.service.access.network.telephony.data.state

public data class TelephonyNetworkState(
    public val networkTypeState: TelephonyNetworkType,
    public val networkTypeDetailState: TelephonyNetworkDetailType
) {
}