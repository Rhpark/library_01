package kr.open.rhpark.library.system.service.access.telephony.data.current

import android.os.Build
import android.telephony.*
import androidx.annotation.RequiresApi
import com.example.test001.base.system.service.info.manager.telephony.data.cell.wcdma.CellSignalStrengthWcdmaData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.cdma.CellSignalStrengthCdmaData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.gsm.CellSignalStrengthGsmData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.lte.CellSignalStrengthLteData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.nr.CellSignalStrengthNrData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.tdscdma.CellSignalStrengthDataTdscdma


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
                    is CellSignalStrengthNr         -> {    cellDataNrList.add(CellSignalStrengthNrData(item)) }
                    is CellSignalStrengthLte        -> {    cellDataLteList.add(CellSignalStrengthLteData(item)) }
                    is CellSignalStrengthWcdma      -> {    cellDataWcdmaList.add(CellSignalStrengthWcdmaData(item)) }
                    is CellSignalStrengthCdma       -> {    cellDataCdmaList.add(CellSignalStrengthCdmaData(item))}
                    is CellSignalStrengthGsm        -> {    cellDataGsmList.add(CellSignalStrengthGsmData(item                     )) }
                    is CellSignalStrengthTdscdma    -> {    cellDataTdscdmaList.add(CellSignalStrengthDataTdscdma(item)) }
                }
            }
        }
    }
}