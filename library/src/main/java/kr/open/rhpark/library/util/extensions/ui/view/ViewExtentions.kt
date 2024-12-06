package kr.open.rhpark.library.util.extensions.ui.view

import android.view.View

public fun View.setVisible() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

public fun View.setGone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

public fun View.setInvisible() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}