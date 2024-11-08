package kr.open.rhpark.app.activity.toast_snackbar

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityToastSnackbarBinding
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class ToastSnackBarActivity :
    BaseBindingActivity<ActivityToastSnackbarBinding>(R.layout.activity_toast_snackbar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        systemServiceManagerInfo.softKeyboardController.show(binding.editText,200L)

        binding.btnDefaultToast.setOnClickListener { toast.showMsgShort("Default Toast") }

        binding.btnCustomToast.visibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) View.GONE
            else View.VISIBLE

        binding.btnCustomToast.setOnClickListener {
            toast.setGravity(Triple(Gravity.CENTER_VERTICAL, 0, 0))
            toast.setBackground(Color.YELLOW)
            toast.showMsgShort("Custom Toast")
        }

        binding.btnDefaultSnackBar.setOnClickListener { v ->
            snackBar.showMsgShort(v, "Default SnackBar")
        }

        binding.btnDefaultWindowViewSnackBar.setOnClickListener {
            snackBar.showMsgShortWindowView("Default Window View SnackBar")
        }

        binding.btnCustomSnackBar.setOnClickListener { v ->
            snackBar.apply {
                setAnimation(animation = BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                setTextColor(Color.BLUE)
                setBackgroundTint(Color.WHITE)
                setActionTextColor(Color.RED)
                setAction("Action_01") { v ->
                    toast.showMsgShort("OnClick SnackBar Action_01")
                }
            }.showMsgShort(v,"Custom SnackBar")
        }
    }
}