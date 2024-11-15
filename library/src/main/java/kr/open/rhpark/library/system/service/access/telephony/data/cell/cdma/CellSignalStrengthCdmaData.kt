package kr.open.rhpark.library.system.service.access.telephony.data.cell.cdma

import android.telephony.CellSignalStrengthCdma

public data class CellSignalStrengthCdmaData(
    private var cellSignalStrength: CellSignalStrengthCdma? = null
) {
    public fun update(cellSignalStrength: CellSignalStrengthCdma) { this.cellSignalStrength = cellSignalStrength }

    /******************************
     * get CellSignalStrengthLte  *
     ******************************/
    public fun getDbm(): Int? = cellSignalStrength?.dbm
    public fun getLevel(): Int? = cellSignalStrength?.level
    public fun getAsuLevel(): Int? = cellSignalStrength?.asuLevel
    public fun getEvdoLevel(): Int? = cellSignalStrength?.evdoLevel
    public fun getEvdoDbm(): Int? = cellSignalStrength?.evdoDbm
    public fun getEvdoEcio(): Int? = cellSignalStrength?.evdoEcio
    public fun getEvdoSnr(): Int? = cellSignalStrength?.evdoSnr
    public fun getCdmaEcio(): Int? = cellSignalStrength?.cdmaEcio
    public fun getCdmaLevel(): Int? = cellSignalStrength?.cdmaLevel
    /** get Rssi */
    public fun getCdmaDbm(): Int? = cellSignalStrength?.cdmaDbm
}