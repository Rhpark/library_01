package kr.open.rhpark.library.system.service.info.power

import android.content.Context
import kr.open.rhpark.library.debug.logcat.Logx
import java.lang.reflect.Method

/**
 * PowerProfile
 * Using Battery Total Capacity ( rated capacity )
 * https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/com/android/internal/os/PowerProfile.java;l=33;drc=master;bpv=1;bpt=1
 */
public class PowerProfile(context: Context) {

    private val classNamePowerProfile: String = "com.android.internal.os.PowerProfile"
    private val averagePower: String = "getAveragePower"

    private val powerProfileClass: Class<*> = Class.forName(classNamePowerProfile)

    private val getAveragePowerMethod: Method =
        powerProfileClass.getMethod(averagePower, String::class.java)

    private val getAveragePowerMethodWithInt: Method = powerProfileClass.getMethod(
        averagePower, String::class.java, Int::class.javaPrimitiveType
    )

    private val powerProfileInstance: Any =
        powerProfileClass.getConstructor(Context::class.java).newInstance(context)

    /**
     * CPU_CLUSTER_POWER_COUNT
     *
     * Retrieves the average power consumption for the specified power profile type.
     * 지정된 전력 프로필 유형에 대한 평균 전력 소비량을 검색.
     *
     * @param type The power profile type to retrieve.
     * @return The average power consumption, or null if an error occurred.
     *
     * @param type 검색할 전력 프로필 유형.
     * @return 평균 전력 소비량 오류가 발생하면 null.
     */
    public fun getAveragePower(type: PowerProfileVO): Any? {
        return try {
            getAveragePowerMethod.invoke(powerProfileInstance, type.res)
        } catch (e: Exception) {
            e.printStackTrace()
            Logx.e(e)
            null
        }
    }

    /**
     * Retrieves the average power consumption for the specified power profile type and index.
     * 지정된 전력 프로필 유형 및 인덱스에 대한 평균 전력 소비량을 검색.
     *
     * @param type The power profile type to retrieve.
     * @param i The index of the power profile value.
     *
     * @param type 검색할 전력 프로필 유형.
     * @param i 전력 프로필 값의 인덱스.
     *
     * @return The average power consumption, or null if an error occurred.
     * @return 평균 전력 소비량. 오류가 발생하면 null.
     */
    public fun getAveragePower(type: PowerProfileVO, i: Int): Any? {
        return try {
            getAveragePowerMethodWithInt.invoke(powerProfileInstance, type.res, i)
        } catch (e: Exception) {
            e.printStackTrace()
            Logx.e(e)
            null
        }
    }
}