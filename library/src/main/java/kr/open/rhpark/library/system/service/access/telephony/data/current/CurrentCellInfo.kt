package kr.open.rhpark.library.system.service.access.telephony.data.current

import android.os.Build
import android.telephony.*
import com.example.test001.base.system.service.info.manager.telephony.data.cell.wcdma.CellInfoWcdmaData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.cdma.CellInfoCdmaData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.gsm.CellInfoGsmData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.lte.CellInfoLteData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.nr.CellInfoNrData
import kr.open.rhpark.library.system.service.access.telephony.data.cell.tdscdma.CellInfoTdscdmaData


public data class CurrentCellInfo(val cellInfo: List<CellInfo>) {
    public val cellDataCdmaList: MutableList<CellInfoCdmaData> = mutableListOf()
    public val cellDataGsmList  : MutableList<CellInfoGsmData> = mutableListOf()
    public val cellDataLteList : MutableList<CellInfoLteData> = mutableListOf()
    public val cellDataNrList : MutableList<CellInfoNrData> = mutableListOf()
    public val cellDataTdscdmaList : MutableList<CellInfoTdscdmaData> = mutableListOf()
    public val cellDataWcdmaList : MutableList<CellInfoWcdmaData> = mutableListOf()

    init {
        cellInfo.forEach { item ->
            when(item) {
                is CellInfoLte      -> {   cellDataLteList.add(CellInfoLteData(item))  }
                is CellInfoWcdma    ->  {   cellDataWcdmaList.add(CellInfoWcdmaData(item))   }
                is CellInfoCdma     ->  {   cellDataCdmaList.add(CellInfoCdmaData(item))   }
                is CellInfoGsm      ->  {   cellDataGsmList.add(CellInfoGsmData(item))    }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                when(item) {
                    is CellInfoNr       ->  {   cellDataNrList.add(CellInfoNrData(item))   }
                    is CellInfoTdscdma  ->  {   cellDataTdscdmaList.add(CellInfoTdscdmaData(item))   }
                }
            }
        }
    }
}