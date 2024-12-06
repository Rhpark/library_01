package kr.open.rhpark.app.activity.window

import android.Manifest
import android.os.Bundle
import android.widget.ImageView
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityWindowBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.WindowManagerFloatingView
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getWindowManagerController

public class WindowActivity : BaseBindingActivity<ActivityWindowBinding>(R.layout.activity_window) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(listOf(Manifest.permission.SYSTEM_ALERT_WINDOW)) { requestCode, deniedPermissionList ->
            Logx.d("requestCode $requestCode, deniedPermissionList $deniedPermissionList")
        }
        initListener()
    }

    private fun initListener() {
        binding.run {

            btnAddView.setOnClickListener {
                val view = ImageView(applicationContext).apply {
                    setImageResource(R.drawable.ic_launcher_foreground)
                    setOnClickListener { Logx.d("OnClick Listener") }
                }

                val floatingView = WindowManagerFloatingView(view, true).apply {
                    setStartPosition(0,0)
                }
                getWindowController().addFloatingView(floatingView)
            }

            btnRemoveView.setOnClickListener {
                getWindowController().removeAllFloatingView()
            }
        }
    }

    private fun getWindowController() = applicationContext.getWindowManagerController()
}