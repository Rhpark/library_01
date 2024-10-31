package kr.open.rhpark.app.activity.vibrator

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityVibratorBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class VibratorActivity : BaseBindingActivity<ActivityVibratorBinding>(R.layout.activity_vibrator) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
        requestPermissions(listOf("android.permission.VIBRATE"), {
            toast.showShort("Permission Granted")
        }, { deniedPermissions->
            toast.showShort("Permission Denied, deniedPermissions $deniedPermissions")
        })
    }

    private fun initListener() {
        binding.run {
            btnOneShort.setOnClickListener {
                if (!timerIsEmpty()) {
                    systemServiceManagerInfo.vibratorController.createOneShot(edtKey.text.toString().toLong())
                }
            }

            btnWaveForm.setOnClickListener {
                val timeArray = LongArray(4).apply {
                    this[0] = 1000L
                    this[1] = 2000L
                    this[2] = 1000L
                    this[3] = 2000L
                }
                val amplitudesArray = IntArray(4).apply {
                    this[0] = 255
                    this[1] = 64
                    this[2] = 255
                    this[3] = 32
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    toast.showShort("WaveForm Click timeArray ${timeArray.toList()}, repeat -1, amplitudesArray ${amplitudesArray.toList()}")
                } else {
                    toast.showShort("WaveForm Click timeArray ${timeArray.toList()}, repeat -1")
                }
                systemServiceManagerInfo.vibratorController.createWaveform(timeArray, -1, amplitudesArray)
            }

            btnPredefined.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    toast.showShort("Predefined Click VibrationEffect.EFFECT_DOUBLE_CLICK")
                    systemServiceManagerInfo.vibratorController.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                } else {
                    toast.showShort("Predefined Requires Os Version 26(O), but your Os Version is ${Build.VERSION.SDK_INT}")
                }
            }

            btbCancel.setOnClickListener {
                systemServiceManagerInfo.vibratorController.cancel()
            }
        }
    }

    private fun timerIsEmpty(): Boolean = if (binding.edtKey.text.isEmpty()) {
        toast.showShort("Input Timer")
        true
    } else {
        false
    }



}