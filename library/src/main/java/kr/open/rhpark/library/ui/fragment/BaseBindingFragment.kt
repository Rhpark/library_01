package kr.open.rhpark.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

public abstract class BaseBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : RootFragment() {

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        onCrateView(binding.root, savedInstanceState)
        return binding.root
    }

    protected fun onCrateView(rootView: View, savedInstanceState: Bundle?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }

    protected inline fun <reified T : ViewModel> Fragment.getViewModel(): T {
        return ViewModelProvider(this)[T::class.java]
    }
}