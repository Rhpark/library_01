package kr.open.rhpark.library.system.service.controller

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
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
    BaseSystemService(context, arrayOf(android.Manifest.permission.VIBRATE)) {

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
    public fun createOneShot(timer: Long, effect: Int = VibrationEffect.DEFAULT_AMPLITUDE) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
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
     * amplitudes : available Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     */
    public fun createWaveform(times: LongArray, repeat: Int, amplitudes: IntArray? = null) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            vibrator.vibrate(times, repeat)
            return
        }

        val effectWave = if (amplitudes != null) {
            VibrationEffect.createWaveform(times, amplitudes, repeat)
        } else {
            VibrationEffect.createWaveform(times, repeat)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            vibrator.vibrate(effectWave)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibratorManger.vibrate(CombinedVibration.createParallel(effectWave))
        }
    }

    public fun cancel() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            vibrator.cancel()
        } else {
            vibratorManger.cancel()
        }
    }
}