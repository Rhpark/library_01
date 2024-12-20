package kr.open.rhpark.library.system.service.controller

import android.Manifest.permission.VIBRATE
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import kr.open.rhpark.library.system.service.base.BaseSystemService

/**
 *
 * manifest register & request Permission
 * <uses-permission android:name="android.permission.VIBRATE"/>
 *
 * Vibrator(SDK < 31), VibratorManager(SDK >= 31)
 *
 */
public class VibratorController(context: Context) :
    BaseSystemService(context, listOf(VIBRATE)) {

    /**
     * be used Build.VERSION.SDK_INT < Build.VERSION_CODES.S(31)
     */
    public val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        } else {
            throw IllegalStateException("Can not be used Vibrator Class, required must be lower than SDK version S(31), this SDK version ${Build.VERSION.SDK_INT}")
        }
    }


    /**
     * be used Build.VERSION.SDK_INT >= Build.VERSION_CODES.S(31)
     */
    @get:RequiresApi(Build.VERSION_CODES.S)
    public val vibratorManger: VibratorManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(AppCompatActivity.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
        } else {
            throw IllegalStateException("Can not be used VibratorManager Class, required SDK version >= Build.VERSION_CODES.S(31), this SDK version ${Build.VERSION.SDK_INT}")
        }
    }

    /**
     * effect : Int (-1 ~ 255)
     * be used Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     */
    @RequiresPermission(VIBRATE)
    public fun createOneShot(timer: Long, effect: Int = VibrationEffect.DEFAULT_AMPLITUDE) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            vibrator.vibrate(timer)
            return
        }

        val oneShort = VibrationEffect.createOneShot(timer, effect)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            vibrator.vibrate(oneShort)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibratorManger.vibrate(CombinedVibration.createParallel(oneShort))
        }
    }

    /**
     * vibrationEffectClick  ex) VibrationEffect.EFFECT_CLICK
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @RequiresPermission(VIBRATE)
    public fun createPredefined(vibrationEffectClick: Int) {

        val effect = VibrationEffect.createPredefined(vibrationEffectClick)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            vibrator.vibrate(effect)
        } else {
            //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            vibratorManger.vibrate(CombinedVibration.createParallel(effect))
        }
    }

    /**
     * repeat : -1 is oneTime
     *
     * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> can waveform vibration
     *
     * else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
     * -> just amplitude is maintained in array[0] repeat vibration,
     *
     * else -> just repeat vibration
     *
     *
     * @param times – The timing values, in milliseconds, of the timing /
     * amplitude pairs. Timing values of 0 will cause the pair to be ignored.
     *
     *
     * @param amplitudes – The amplitude values of the timing /
     * amplitude pairs. Amplitude values must be between 0 and 255,
     * or equal to DEFAULT_AMPLITUDE. An amplitude value of 0 implies the motor is off.
     *
     *
     * @param repeat – The index into the timings array at which to repeat, or -1 if
     * you don't want to repeat indefinitely.
     *
     * 즉 배열의 어디부터 시작해서 반복할 건지 설정, ex repeat 값이 1이면 배열 1번부터 무한히 반복
     */
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(VIBRATE)
    public fun createWaveform(times: LongArray, amplitudes: IntArray, repeat: Int = -1) {

        val effectWave = VibrationEffect.createWaveform(times, amplitudes, repeat)

        vibratorManger.vibrate(CombinedVibration.createParallel(effectWave))
    }

    @RequiresPermission(VIBRATE)
    public fun cancel() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            vibrator.cancel()
        } else {
            vibratorManger.cancel()
        }
    }
}