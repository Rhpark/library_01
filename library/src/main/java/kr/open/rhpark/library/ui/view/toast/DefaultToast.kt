package kr.open.rhpark.library.ui.view.toast

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

public class DefaultToast(private val context: Context) {

    private var gravity: Triple<Int, Int, Int>? = null

    private var margin: Pair<Float, Float>? = null

    /**
     * Gravity set Triple
     * first is gravity
     * second is xOffSet
     * third yOffSet
     */
    public fun setGravity(defaultGravity: Triple<Int, Int, Int>?) {
        this.gravity = defaultGravity
    }

    /**
     * first is Horizontal
     * second is Vertical
     */
    @RequiresApi(Build.VERSION_CODES.R)
    public fun setMargin(defaultMargin: Pair<Float, Float>? = null) {
        this.margin = defaultMargin
    }

    public fun showShort(msg: String) { make(msg, Toast.LENGTH_SHORT).show() }
    public fun showLong(msg: String) { make(msg, Toast.LENGTH_LONG).show() }
    public fun showDuration(msg: String, duration:Int) { make(msg, duration).show() }

    private fun make(msg: String, duration: Int) = Toast.makeText(context, msg, duration).apply {
        this@DefaultToast.margin?.let { this.setMargin(it.first, it.second) }
        this@DefaultToast.gravity?.let { this.setGravity(it.first, it.second, it.third) }
    }
}