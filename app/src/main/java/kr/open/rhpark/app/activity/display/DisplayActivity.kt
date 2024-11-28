package kr.open.rhpark.app.activity.display

import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityDisplayBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.activity.getNavigationBarHeight
import kr.open.rhpark.library.util.extensions.activity.getStatusBarHeight
import kr.open.rhpark.library.util.extensions.unit_convert.dpToPx
import kr.open.rhpark.library.util.extensions.unit_convert.dpToSp
import kr.open.rhpark.library.util.extensions.unit_convert.pxToDp
import kr.open.rhpark.library.util.extensions.unit_convert.pxToSp
import kr.open.rhpark.library.util.extensions.unit_convert.spToDp
import kr.open.rhpark.library.util.extensions.unit_convert.spToPx

class DisplayActivity : BaseBindingActivity<ActivityDisplayBinding>(R.layout.activity_display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.run {
            btnDpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "DP to SP ${number.dpToSp(this@DisplayActivity)},\nDP to PX ${number.dpToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btnPxToDp.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "PX to DP ${number.pxToDp(this@DisplayActivity)},\nPX to SP ${number.pxToSp(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbSpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "SP to DP ${number.spToDp(this@DisplayActivity)},\nSP to PX ${number.spToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbScreenInfo.setOnClickListener {
                val getFullScreen =
                    "FullScreen = ${getDisplayInfo().getFullScreen()}\n" +
                            "ScreenWithStatusBar = ${getDisplayInfo().getScreenWithStatusBar()}\n" +
                            "Screen = ${getDisplayInfo().getScreen()}\n" +
                            "StatusBar height = ${getStatusBarHeight()}\n" +
                            "NavigationBar height = ${getNavigationBarHeight()}\n" +
                            "getStatusBarHeight = ${this@DisplayActivity.getStatusBarHeight()}\n" +
                            "getNavigationBarHeight = ${this@DisplayActivity.getNavigationBarHeight()}\n"

                tvResult.text = getFullScreen
            }
        }
    }

    private fun getDisplayInfo() = systemServiceManagerInfo.displayInfo

    private fun editNumberIsEmpty(): Boolean = if (binding.edtNumber.text.isEmpty()) {
        toast.showMsgShort("Input Number")
        true
    } else { false }
}