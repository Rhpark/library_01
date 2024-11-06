package kr.open.rhpark.app.activity.display

import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityDisplayBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.ui.util.dpToPx
import kr.open.rhpark.library.ui.util.dpToSp
import kr.open.rhpark.library.ui.util.getNavigationBarHeight
import kr.open.rhpark.library.ui.util.getStatusBarHeight
import kr.open.rhpark.library.ui.util.pxToDp
import kr.open.rhpark.library.ui.util.pxToSp
import kr.open.rhpark.library.ui.util.spToDp
import kr.open.rhpark.library.ui.util.spToPx

public class DisplayActivity : BaseBindingActivity<ActivityDisplayBinding>(R.layout.activity_display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initListener()
    }

    private fun initListener() {
        binding.run {
            btnDpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "DP to SP ${number.dpToSp(this@DisplayActivity)},\n " +
                            "DP to PX ${number.dpToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btnPxToDp.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "PX to DP ${number.pxToDp(this@DisplayActivity)},\n " +
                            "PX to SP ${number.pxToSp(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbSpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toInt()
                    val dpToPx = "SP to DP ${number.spToDp(this@DisplayActivity)},\n " +
                            "SP to PX ${number.spToPx(this@DisplayActivity)}\n"
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

    private fun editNumberIsEmpty(): Boolean = if (binding.edtNumber.text.isEmpty()) {
        toast.showShort("Input Number")
        true
    } else {
        false
    }

    private fun getDisplayInfo() = systemServiceManagerInfo.displayInfo
}