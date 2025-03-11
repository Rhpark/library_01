package kr.open.rhpark.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

public abstract class BaseFragment(@LayoutRes private val layoutRes: Int) : RootFragment() {

    protected lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(layoutRes, container, false)
        return rootView
    }
}