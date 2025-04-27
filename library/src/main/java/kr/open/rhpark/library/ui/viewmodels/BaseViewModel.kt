package kr.open.rhpark.library.ui.viewmodels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel

/**
 * Base ViewModel class that implements DefaultLifecycleObserver
 * for handling lifecycle events directly in ViewModel
 */
public abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    // Override lifecycle methods as needed
}