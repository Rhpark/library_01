package kr.open.rhpark.app.activity.vibrator

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.view.View
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityVibratorBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class VibratorActivity : BaseBindingActivity<ActivityVibratorBinding>(R.layout.activity_vibrator) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
        requestPermissions(listOf(android.Manifest.permission.VIBRATE), {
            toast.showShort("Permission Granted")
        }, { deniedPermissions->
            toast.showShort("Permission Denied, deniedPermissions $deniedPermissions")
        })
    }

    private fun initListener() {
        binding.run {
            btnOneShort.setOnClickListener {
                if (edtKey.text.isEmpty()) {
                    toast.showShort("Input Timer")
                } else {
                    systemServiceManagerInfo.vibratorController.createOneShot(edtKey.text.toString().toLong())
                }
            }

            btnWaveForm.visibility =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) View.VISIBLE else View.GONE

            btnWaveForm.setOnClickListener {
                val times = LongArray(3).apply { this[0] = 1000L; this[1] = 1000L; this[2] = 1000L }
                val amplitudes = IntArray(3).apply { this[0] = 64; this[1] = 255; this[2] = 128 }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    systemServiceManagerInfo.vibratorController.createWaveform(times, amplitudes)
                } else {
                    toast.showShort("Requires Os Version 31(S), but your Os Version is ${Build.VERSION.SDK_INT}")
                }
            }

            btnPredefined.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    systemServiceManagerInfo.vibratorController.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                } else {
                    toast.showShort("Requires Os Version 26(O), but your Os Version is ${Build.VERSION.SDK_INT}")
                }
            }

            btbCancel.setOnClickListener { systemServiceManagerInfo.vibratorController.cancel() }
        }
    }
}