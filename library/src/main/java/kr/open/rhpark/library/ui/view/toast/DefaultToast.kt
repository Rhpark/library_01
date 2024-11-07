package kr.open.rhpark.library.ui.view.toast

import android.content.Context
import android.os.Build
import android.widget.Toast

public class DefaultToast(private val context: Context) {

    private var gravity: Triple<Int, Int, Int>? = null

    private var margin: Pair<Float, Float>? = null

    /**
     * Starting from Android Build. VERSION_CODES. R,
     * for apps targeting API level Build. VERSION_CODES. R or higher,
     * this method is a no-op when called on text toasts.
     *
     * Gravity set Triple
     * first is gravity
     * second is xOffSet
     * third yOffSet
     */
    public fun setGravity(defaultGravity: Triple<Int, Int, Int>?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            this.gravity = defaultGravity
        }
    }

    /**
     * Warning: Starting from Android Build. VERSION_CODES. R,
     * for apps targeting API level Build. VERSION_CODES. R or higher,
     * this method is a no-op when called on text toasts.
     *
     * first is Horizontal
     * second is Vertical
     */
    public fun setMargin(defaultMargin: Pair<Float, Float>? = null) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            this.margin = defaultMargin
        }
    }

    public fun showShort(msg: String) { make(msg, Toast.LENGTH_SHORT).show() }
    public fun showLong(msg: String) { make(msg, Toast.LENGTH_LONG).show() }
    public fun showDuration(msg: String, duration:Int) { make(msg, duration).show() }

    private fun make(msg: String, duration: Int) = Toast.makeText(context, msg, duration).apply {
        this@DefaultToast.margin?.let { this.setMargin(it.first, it.second) }
        this@DefaultToast.gravity?.let { this.setGravity(it.first, it.second, it.third) }
    }
}