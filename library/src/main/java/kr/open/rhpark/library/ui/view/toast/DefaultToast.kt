package kr.open.rhpark.library.ui.view.toast

import android.content.Context
import android.os.Build
import android.widget.Toast

public class DefaultToast(private val context: Context) {

    private var gravity: Triple<Int, Int, Int>? = null

    private var margin: Pair<Float, Float>? = null

    private var background: Int? = null

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

    /**
     * Warning: Starting from Android Build. VERSION_CODES. R,
     * for apps targeting API level Build. VERSION_CODES. R or higher,
     * this method is a no-op when called on text toasts.
     */
    public fun setBackground(defaultBackground: Int? = null) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            this.background = defaultBackground
        }
    }

    public fun showMsgShort(msg: String) { msgShort(msg).show() }
    public fun showMsgLong(msg: String) { msgLong(msg).show() }
    public fun showMsgDuration(msg: String, duration:Int) { msgDuration(msg, duration).show() }

    public fun showMsgShort(context: Context, msg: String) { msgShort(context, msg).show() }
    public fun showMsgLong(context: Context, msg: String) { msgLong(context, msg).show() }
    public fun showMsgDuration(context: Context, msg: String, duration:Int) { msgDuration(context, msg, duration).show() }

    public fun msgShort(msg: String): Toast = make(context, msg, Toast.LENGTH_SHORT)
    public fun msgLong(msg: String): Toast = make(context, msg, Toast.LENGTH_LONG)
    public fun msgDuration(msg: String,duration:Int): Toast = make(context, msg, duration)

    public fun msgShort(context: Context, msg: String): Toast = make(context, msg, Toast.LENGTH_SHORT)
    public fun msgLong(context: Context, msg: String): Toast = make(context, msg, Toast.LENGTH_LONG)
    public fun msgDuration(context: Context, msg: String,duration:Int): Toast = make(context, msg, duration)

    private fun make(context: Context, msg: String, duration: Int) = Toast.makeText(context, msg, duration).apply {
        this@DefaultToast.margin?.let { this.setMargin(it.first, it.second) }
        this@DefaultToast.gravity?.let { this.setGravity(it.first, it.second, it.third) }
        this@DefaultToast.background?.let { this.view?.setBackgroundColor(it) }
    }
}