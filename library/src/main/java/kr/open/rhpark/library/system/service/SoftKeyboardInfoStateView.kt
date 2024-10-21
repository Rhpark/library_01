package kr.open.rhpark.library.system.service

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import kr.open.rhpark.library.debug.logcat.Logx

/**
 * Using InputMethodManager
 * Screen Soft Keyboard control class
 * search windowSoftInputMode in https://blog.naver.com/il7942li/222671675950
 */
public class SoftKeyboardInfoStateView(private val imm: InputMethodManager) {


    /**
     * can config in manifest
     * android:windowSoftInputMode="SOFT_INPUT_ADJUST_PAN"
     */
    public fun setAdjustPan(window: Window): Unit =
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    /**
     * can config in manifest
     * android:windowSoftInputMode=""
     *
     * ex) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE or
     */
    public fun setSoftInputMode(window: Window, softInputTypes: Int) {
        window.setSoftInputMode(softInputTypes)
    }

    /**
     * View that can show input text)  EditText, SearchView ... etc.
     * default flag 0, select 0, SHOW_IMPLICIT, SHOW_FORCED(API 33 Deprecated)
     */
    public fun show(v: View, flag: Int = InputMethodManager.SHOW_IMPLICIT) {
        if(v.requestFocus()) { imm.showSoftInput(v, flag) }
        else { Logx.e("view requestFocus() is false!!") }
    }

    /**
     * View that can show input text delay(ms))  EditText, SearchView ... etc.
     * default flag 0, select 0, SHOW_IMPLICIT, SHOW_FORCED(API 33 Deprecated)
     */
    public fun show(v: View, delay: Long, flag: Int = InputMethodManager.SHOW_IMPLICIT): Boolean =
        v.postDelayed(Runnable { show(v, flag) }, delay)

    /**
     * View that can hide input text)  EditText, SearchView ... etc.
     * default flag 0,select flat 0, HIDE_IMPLICIT_ONLY, HIDE_NOT_ALWAYS
     */
    public fun hide(v: View, flag: Int = 0) {
        Logx.d(flag)
        if(v.requestFocus()) { imm.hideSoftInputFromWindow(v.windowToken, flag) }
        else { Logx.e("view requestFocus() is false!!")      }
    }

    /**
     * View that can hide input text delay(ms))  EditText, SearchView ... etc.
     * default flag 0,select flat 0, HIDE_IMPLICIT_ONLY, HIDE_NOT_ALWAYS
     */
    public fun hide(v: View, delay: Long, flag: Int = 0): Boolean =
        v.postDelayed(Runnable { hide(v, flag) }, delay)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public fun startStylusHandwriting(v: View) {
        Logx.d(v.requestFocus())
        if (v.requestFocus()) { imm.startStylusHandwriting(v) }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public fun startStylusHandwriting(v: View, delay:Long): Boolean = v.postDelayed(Runnable {
        startStylusHandwriting(v)
    },delay)
}