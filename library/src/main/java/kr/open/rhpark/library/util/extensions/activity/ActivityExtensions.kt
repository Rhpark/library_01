package kr.open.rhpark.library.util.extensions.activity

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.WindowInsets
import kr.open.rhpark.library.debug.logcat.Logx


public fun Activity.getStatusBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.getRootWindowInsets().getInsets(WindowInsets.Type.statusBars()).top
    } else {
        val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        rectangle.top
    }



public fun Activity.getNavigationBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val res = window.decorView.getRootWindowInsets().getInsets(WindowInsets.Type.navigationBars())
        Logx.d("${res.top}, ${res.bottom}")
        res.bottom
    } else {
        val rootView = window.decorView.rootView
        val contentViewHeight = findViewById<View>(android.R.id.content).height
        val otherViewHeight = rootView.height - contentViewHeight
        val navigationBarHeight = otherViewHeight - getStatusBarHeight()
        navigationBarHeight
    }
