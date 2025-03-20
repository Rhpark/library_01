package kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.current

import android.telephony.*
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.cdma.CellSignalStrengthCdmaData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.gsm.CellSignalStrengthGsmData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.lte.CellSignalStrengthLteData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.nr.CellSignalStrengthNrData
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.tdscdma.CellSignalStrengthDataTdscdma
import kr.open.rhpark.library.domain.common.systemmanager.info.network.telephony.data.cell.wcdma.CellSignalStrengthWcdmaData


//@RequiresApi(Build.VERSION_CODES.Q)
public data class CurrentSignalStrength(
   public val signalStrength: SignalStrength?,
) {
    public var cellDataList: List<CellSignalStrength>? = null
    public val cellDataCdmaList : MutableList<CellSignalStrengthCdmaData> = mutableListOf()
    public val cellDataGsmList  : MutableList<CellSignalStrengthGsmData> = mutableListOf()
    public val cellDataLteList : MutableList<CellSignalStrengthLteData> = mutableListOf()
    public val cellDataNrList : MutableList<CellSignalStrengthNrData> = mutableListOf()
    public val cellDataTdscdmaList : MutableList<CellSignalStrengthDataTdscdma> = mutableListOf()
    public val cellDataWcdmaList : MutableList<CellSignalStrengthWcdmaData> = mutableListOf()

    init {
        cellDataList = signalStrength?.cellSignalStrengths as List<CellSignalStrength>
        cellDataList?.let {
            it.forEach { item->
                when(item) {
                    is CellSignalStrengthNr         -> {    cellDataNrList.add(
                        CellSignalStrengthNrData(item)
                    ) }
                    is CellSignalStrengthLte        -> {    cellDataLteList.add(
                        CellSignalStrengthLteData(item)
                    ) }
                    is CellSignalStrengthWcdma      -> {    cellDataWcdmaList.add(CellSignalStrengthWcdmaData(item)) }
                    is CellSignalStrengthCdma       -> {    cellDataCdmaList.add(
                        CellSignalStrengthCdmaData(item)
                    )}
                    is CellSignalStrengthGsm        -> {    cellDataGsmList.add(
                        CellSignalStrengthGsmData(item                     )
                    ) }
                    is CellSignalStrengthTdscdma    -> {    cellDataTdscdmaList.add(
                        CellSignalStrengthDataTdscdma(item)
                    ) }
                }
            }
        }
    }

    public fun toResString() :String {
        var res :String = ""
        res += "cellDataGsmList ${cellDataGsmList.toList()}\n" +
                "cellDataCdmaList ${cellDataCdmaList.toList()}\n" +
                "cellDataWcdmaList ${cellDataWcdmaList.toList()}\n" +
                "cellDataTdscdmaList ${cellDataTdscdmaList.toList()}\n" +
                "cellDataLteList ${cellDataLteList.toList()}\n" +
                "cellDataNrList ${cellDataNrList.toList()}\n\n"
        return res
    }
}