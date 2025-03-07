package kr.open.rhpark.app.activity.display

import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityDisplayBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getDisplayInfo
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort
import kr.open.rhpark.library.util.extensions.ui.display.dpToPx
import kr.open.rhpark.library.util.extensions.ui.display.dpToSp
import kr.open.rhpark.library.util.extensions.ui.display.pxToDp
import kr.open.rhpark.library.util.extensions.ui.display.pxToSp
import kr.open.rhpark.library.util.extensions.ui.display.spToDp
import kr.open.rhpark.library.util.extensions.ui.display.spToPx

class DisplayActivity : BaseBindingActivity<ActivityDisplayBinding>(R.layout.activity_display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.run {
            btnDpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "DP to SP ${number.dpToSp(this@DisplayActivity)},\nDP to PX ${number.dpToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btnPxToDp.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "PX to DP ${number.pxToDp(this@DisplayActivity)},\nPX to SP ${number.pxToSp(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbSpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "SP to DP ${number.spToDp(this@DisplayActivity)},\nSP to PX ${number.spToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbScreenInfo.setOnClickListener {
                val getFullScreen =
                    "FullScreen = ${getDisplayInfo().getFullScreenSize()}\n" +
                            "ScreenWithStatusBar = ${getDisplayInfo().getScreenWithStatusBar()}\n" +
                            "Screen = ${getDisplayInfo().getScreen()}\n" +
                            "StatusBar height = ${statusBarHeight}\n" +
                            "NavigationBar height = ${navigationBarHeight}\n" +
                            "getStatusBarHeight = ${this@DisplayActivity.statusBarHeight}\n" +
                            "getNavigationBarHeight = ${this@DisplayActivity.navigationBarHeight}\n"

                tvResult.text = getFullScreen
            }
        }
    }

    private fun editNumberIsEmpty(): Boolean = if (binding.edtNumber.text.isEmpty()) {
        toastShowShort("Input Number")
        true
    } else { false }
}