package kr.open.rhpark.app.activity.window

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityWindowBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.data.FloatingViewCollisionsType
import kr.open.rhpark.library.system.service.controller.windowmanager.floating.drag.FloatingDragView
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.getDisplayInfo
import kr.open.rhpark.library.util.extensions.context.getFloatingViewControllerController
import kr.open.rhpark.library.util.extensions.ui.view.setGone
import kr.open.rhpark.library.util.extensions.ui.view.setVisible

public class WindowActivity : BaseBindingActivity<ActivityWindowBinding>(R.layout.activity_window) {

    private val windowManagerController by lazy { applicationContext.getFloatingViewControllerController() }
    private val displayInfo by lazy { applicationContext.getDisplayInfo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(listOf(Manifest.permission.SYSTEM_ALERT_WINDOW)) { requestCode, deniedPermissionList ->
            Logx.d("requestCode $requestCode, deniedPermissionList $deniedPermissionList")
            if(deniedPermissionList.isEmpty()) {
                initListener()
            }
        }
    }

    private fun initListener() = binding.run {
        btnAddDragView.setOnClickListener {
            val dragView = getImageView(R.drawable.ic_launcher_foreground).apply {
                setBackgroundColor(Color.WHITE)
            }

            windowManagerController.addFloatingDragView(
                FloatingDragView(dragView, 0, 0,
                    collisionsWhileDrag = { floatingDragView, type ->
                        windowManagerController.getFloatingFixedView()?.view?.let {
                            if (type == FloatingViewCollisionsType.OCCURING) {
                                val rotationAnim = ObjectAnimator.ofFloat(it, "rotation", 0.0f, 180.0f)
                                rotationAnim.duration = 300
                                rotationAnim.start()
                            }
                        }
                    },
                    collisionsWhileTouchUp = { floatingDragView, type ->
                        windowManagerController.getFloatingFixedView()?.view?.let {
                            setAnimScale(it, object : Animator.AnimatorListener {
                                override fun onAnimationStart(animation: Animator) {}
                                override fun onAnimationRepeat(animation: Animator) {}
                                override fun onAnimationCancel(animation: Animator) {}
                                override fun onAnimationEnd(animation: Animator) {
                                    windowManagerController.getFloatingFixedView()?.let { it.view.setGone() }
                                    if (type == FloatingViewCollisionsType.OCCURING) {
                                        windowManagerController.removeFloatingDragView(floatingDragView)
                                    }
                                }
                            })
                        }
                    },
                    collisionsWhileTouchDown = { floatingDragView, type ->
                        windowManagerController.getFloatingFixedView()?.view?.let {
                            it.setVisible()
                            setAnimScale(it, null)
                        }
                    })
            )
        }

        binding.btnAddFixedView.setOnClickListener {
            val fixedView: ImageView = getImageView(R.drawable.ic_floating_fixed_close)
            windowManagerController.setFloatingFixedView(
                FloatingDragView(
                    fixedView,
                    ((displayInfo.getFullScreenSize().x / 2)),
                    (displayInfo.getFullScreenSize().y / 2),
                )
            )
            fixedView.setGone()
        }

        btnRemoveView.setOnClickListener { windowManagerController.removeAllFloatingView() }
    }

    private fun setAnimScale(view: View, listener: Animator.AnimatorListener?) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = 300
            listener?.let { addListener(it) }
            start()
        }
    }

    private fun getImageView(res: Int): ImageView = ImageView(applicationContext).apply {
        setImageResource(res)
        setOnClickListener { Logx.d("OnClick Listener") }
    }
}