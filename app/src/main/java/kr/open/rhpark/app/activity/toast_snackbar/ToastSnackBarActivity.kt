package kr.open.rhpark.app.activity.toast_snackbar

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import com.google.android.material.snackbar.BaseTransientBottomBar
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityToastSnackbarBinding
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getSoftKeyboardController
import kr.open.rhpark.library.util.extensions.ui.view.setGone
import kr.open.rhpark.library.util.extensions.ui.view.setVisible
import kr.open.rhpark.library.util.extensions.ui.view.snackBarShowIndefinite
import kr.open.rhpark.library.util.extensions.ui.view.snackBarShowShort
import kr.open.rhpark.library.util.extensions.ui.view.toastShort
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion

class ToastSnackBarActivity :
    BaseBindingActivity<ActivityToastSnackbarBinding>(R.layout.activity_toast_snackbar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            getSoftKeyboardController().showDelay(editText,200L)

            btnDefaultToast.setOnClickListener {
                toastShowShort("Toast Show Short")
            }

            checkSdkVersion(
                Build.VERSION_CODES.R,
                positiveWork = { btnCustomToast.setGone() },
                negativeWork = { btnCustomToast.setVisible() }
            )

            btnCustomToast.setOnClickListener {
                toastShort("Option").apply {
                    setGravity(Gravity.CENTER_VERTICAL,0,0)
                    view?.setBackgroundColor(Color.YELLOW)
                }.show()
            }

            btnDefaultSnackBar.setOnClickListener { v -> v.snackBarShowShort("Default SnackBar") }

            btnActionSnackBar.setOnClickListener { v ->
                v.snackBarShowShort("TestMsg", actionText = "Actino_1") { toastShowShort("Click Action_1") }
            }

            btnOptionSnackBar.setOnClickListener { v->
                v.snackBarShowIndefinite(
                    "Option_Test",
                    animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE,
                    bgTint = Color.WHITE,
                    textColor = Color.RED,
                    actionTextColor = Color.BLUE,
                    actionText = "Action_01",
                    ) { toastShowShort("OnCLick Action_01") }
            }
        }
    }
}