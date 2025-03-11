package kr.open.rhpark.library.util.extensions.ui.view


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes

/********
 * View *
 ********/
public fun View.setVisible() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

public fun View.setGone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

public fun View.setInvisible() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}

public fun View.setOnDebouncedClickListener(debounceTime: Long = 600L, action: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            action(view)
        }
    }
}

/*************
 * ViewGroup *
 *************/
public fun ViewGroup.forEachChild(action: (View) -> Unit) {
    for (i in 0 until childCount) { action(getChildAt(i)) }
}

@SuppressLint("ResourceType")
public fun ViewGroup.getLayoutInflater(@IntegerRes xmlRes: Int, attachToRoot: Boolean): View =
    LayoutInflater.from(this.context).inflate(xmlRes, this, attachToRoot)

