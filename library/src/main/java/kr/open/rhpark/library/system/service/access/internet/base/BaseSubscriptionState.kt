package kr.open.rhpark.library.system.service.access.internet.base

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.base.BaseSystemService

/**
 * subscriptionManager와 telephonyManager 정볼을 활용한
 * TelephonyStateInfo, UsimStateInfo Class 의 기본 클래스
 *
 **/
public abstract class BaseSubscriptionState(
    context: Context,
    permissionList: List<String>? = null,
    public val subscriptionManager: SubscriptionManager,
    public val telephonyManager: TelephonyManager,
) : BaseSystemService(context, permissionList) {

    /** SIM 정보를 읽을 수 있는지 여부를 나타낸다.
     * Indicates whether SIM information can be read.
     **/
    private var isReadSimInfo = false

    public fun isCanReadSimInfo(): Boolean = isReadSimInfo


    /**
     * 주어진 subscription ID에 대한 SubscriptionInfo 객체를 반환.
     * Returns the SubscriptionInfo object for the given subscription ID.
     *
     * @param subId subscription ID
     * @return SubscriptionInfo object or null
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getActiveSubscriptionInfoSubId(subId: Int): SubscriptionInfo? =
        subscriptionManager.getActiveSubscriptionInfo(subId)

    init {
        getDefaultSubId()
    }

    /**
     * 기본 subscription ID를 반환.
     * Returns the default subscription ID.
     *
     * @return Default subscription ID or null
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getDefaultSubId(): Int? = try {
        isReadSimInfo = false
        val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            telephonyManager.subscriptionId
        } else {
            // 첫 번째 활성화된 subscription의 ID 반환
            getActiveSubscriptionInfoList().firstOrNull()?.subscriptionId ?: null
        }
        isReadSimInfo = if(id == null)  false
        else true

        id
    } catch (e: NoSuchMethodError) {
        Logx.e("Can not read uSim Chip, subId = -1, e = $e")
        isReadSimInfo = false
        null
    }

    /**
     * 활성화된 모든 subscription 정보 목록을 반환.
     * Returns a list of all active subscription information.
     *
     * @return 활성화된 subscription 정보 목록 /
     *
     * @return List of active subscription information
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getActiveSubscriptionInfoList(): List<SubscriptionInfo> =
        subscriptionManager.activeSubscriptionInfoList ?: emptyList()

    /**
     * 기본 subscription에 대한 SubscriptionInfo 객체를 반환.
     * Returns the SubscriptionInfo object for the default subscription.
     *
     * @return SubscriptionInfo 객체 또는 IllegalArgumentException 예외 발생
     *
     * @return SubscriptionInfo object or IllegalArgumentException exception
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getDefaultSubscriptionInfoSubId(): SubscriptionInfo? =
        getDefaultSubId()?.let { getActiveSubscriptionInfoSubId(it) }
            ?: throw IllegalArgumentException("Can not read uSim Chip")

    /**
     * 주어진 SIM 슬롯 인덱스에 대한 SubscriptionInfo 객체를 반환.
     * Returns the SubscriptionInfo object for the given SIM slot index.
     *
     * @param slotIndex SIM slot index
     *
     * @return SubscriptionInfo object or null
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getActiveSubscriptionInfoSimSlot(slotIndex: Int): SubscriptionInfo? =
        subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(slotIndex)

    /**
     * 기본 SIM 슬롯에 대한 SubscriptionInfo 객체를 반환.
     * Returns the SubscriptionInfo object for the default SIM slot.
     *
     *@return SubscriptionInfo object or NoSuchMethodError exception
     */
    @RequiresPermission(READ_PHONE_STATE)
    protected fun getDefaultSubscriptionInfoSimSlot(): SubscriptionInfo? =
        getDefaultSubId()?.let { getActiveSubscriptionInfoSimSlot(it) }
            ?: throw NoSuchMethodError("Can not read uSim Chip")

    protected fun isNrConnected(telephonyManager:TelephonyManager):Boolean {
        try {
            val obj = telephonyManager.javaClass.getDeclaredMethod("getServiceState").invoke(telephonyManager)

            val methods = Class.forName(obj.javaClass.name).declaredMethods

            for (method in methods) {
                if (method.name == "getNrStatus" || method.name == "getNrState") {
                    method.isAccessible = true
                    return method.invoke(obj) as Int == 3
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}