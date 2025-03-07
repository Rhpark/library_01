package kr.open.rhpark.app.activity.vibrator

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityVibratorBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getVibratorController
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort
import kr.open.rhpark.library.util.extensions.sdk_version.checkSdkVersion

class VibratorActivity : BaseBindingActivity<ActivityVibratorBinding>(R.layout.activity_vibrator) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(listOf(android.Manifest.permission.VIBRATE)){
            requestCode, deniedPermissions->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            initListener()
        }
    }

    private fun initListener() {
        binding.run {
            btnOneShort.setOnClickListener {
                if (edtKey.text.isEmpty()) {
                    toastShowShort("Input Timer")
                } else {
                    getVibratorController().createOneShot(edtKey.text.toString().toLong())
                }
            }

            btnWaveForm.setOnClickListener {
                val times = LongArray(3).apply { this[0] = 1000L; this[1] = 1000L; this[2] = 1000L }
                val amplitudes = IntArray(3).apply { this[0] = 64; this[1] = 255; this[2] = 128 }
                checkSdkVersion(Build.VERSION_CODES.S,
                    positiveWork = {
                        getVibratorController().createWaveform(times, amplitudes)
                    },
                    negativeWork = {
                        toastShowShort("Requires Os Version 31(S), but your Os Version is ${Build.VERSION.SDK_INT}")
                    }
                )
            }

            btnPredefined.setOnClickListener {
                checkSdkVersion(Build.VERSION_CODES.Q,
                    positiveWork = {
                        getVibratorController().createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                    },
                    negativeWork = {
                        toastShowShort("Requires Os Version 26(O), but your Os Version is ${Build.VERSION.SDK_INT}")
                    }
                )
            }

            btbCancel.setOnClickListener { getVibratorController().cancel() }
        }
    }
}