package kr.open.rhpark.app.activity.util

import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityWindowBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.ui.util.dpToPx
import kr.open.rhpark.library.ui.util.getNavigationBarHeight
import kr.open.rhpark.library.ui.util.getStatusBarHeight
import kr.open.rhpark.library.ui.util.pxToDp
import kr.open.rhpark.library.ui.util.pxToSp
import kr.open.rhpark.library.ui.util.spToPx

class WindowActivity : BaseBindingActivity<ActivityWindowBinding>(R.layout.activity_window) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
            btnDpToPx.setOnClickListener {
                if(!timerIsEmpty()) {
                    tvResult.text = "Dp to Px : ${binding.edtNumber.text.toString().toInt().dpToPx(this@WindowActivity)}"
                }
            }

            btnPxToDp.setOnClickListener {
                if(!timerIsEmpty()) {
                    tvResult.text = "Px to Dp : ${binding.edtNumber.text.toString().toFloat().pxToDp(this@WindowActivity)}"
                }
            }

            btbSpToPx.setOnClickListener {
                if(!timerIsEmpty()) {
                    tvResult.text = "Sp to Px : ${binding.edtNumber.text.toString().toInt().spToPx(this@WindowActivity)}"
                }
            }

            btbPxToSp.setOnClickListener {
                if(!timerIsEmpty()) {
                    tvResult.text = "Px to Sp : ${binding.edtNumber.text.toString().toInt().pxToSp(this@WindowActivity)}"
                }
            }

            btbScreenInfo.setOnClickListener {

                systemServiceManagerInfo.windowController.run {
                    val getFullScreen = "FullScreen = ${getFullScreen()}\n" +
                            "ScreenWithStatusBar = ${getScreenWithStatusBar()}\n" +
                            "Screen = ${getScreen()}\n" +
                            "StatusBar height = ${getStatusBarHeight()}\n" +
                            "NavigationBar height = ${getNavigationBarHeight()}\n" +
                            "getStatusBarHeight = ${this@WindowActivity.getStatusBarHeight()}\n" +
                            "getNavigationBarHeight = ${this@WindowActivity.getNavigationBarHeight()}\n"

                    tvResult.text = getFullScreen
                }
            }
        }
    }

    private fun timerIsEmpty(): Boolean = if (binding.edtNumber.text.isEmpty()) {
        toast.showShort("Input Number")
        true
    } else {
        false
    }



}