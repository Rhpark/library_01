package kr.open.rhpark.library.ui.view.activity

import android.os.Bundle
import androidx.annotation.LayoutRes

public abstract class BaseActivity(@LayoutRes private val layoutRes: Int) : RootActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutRes)
    }
}