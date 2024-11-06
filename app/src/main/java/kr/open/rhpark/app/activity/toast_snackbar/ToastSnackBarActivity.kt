package kr.open.rhpark.app.activity.toast_snackbar

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import com.google.android.material.snackbar.BaseTransientBottomBar
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityToastSnackbarBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class ToastSnackBarActivity :
    BaseBindingActivity<ActivityToastSnackbarBinding>(R.layout.activity_toast_snackbar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnDefaultToast.setOnClickListener {
            toast.showShort("Default Toast")
        }

        binding.btnCustomToast.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                toast.setMargin(Pair(10f, 20f))
            }
            toast.setGravity(Triple(Gravity.CENTER_VERTICAL, 0, 0))
            toast.showShort("Custom Toast")
        }

        binding.btnDefaultSnackBar.setOnClickListener { v ->
            snackBar.showShort(v, "Default SnackBar")
        }

        binding.btnDefaultWindowViewSnackBar.setOnClickListener {
            snackBar.showShortWindowView("Default Window View SnackBar")
        }

        binding.btnCustomSnackBar.setOnClickListener { v ->
            snackBar.apply {
                setAnimation(animation = BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                setTextColor(Color.BLUE)
                setBackgroundTint(Color.WHITE)
                setActionTextColor(Color.RED)
                setAction("Action_01") { v ->
                    toast.showShort("OnClick SnackBar")
                    Logx.d("OnClick SnackBar")
                }
            }
            snackBar.showShort(v, "Custom SnackBar")

        }
    }
}