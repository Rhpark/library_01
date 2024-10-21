package kr.open.rhpark.library.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

public abstract class BaseViewModelEventFlow<EVENT_TYPE> : BaseViewModel() {

    /****************************************************************
     *                                                              *
     * Using for ViewModel -> View event (Activity, Fragment, View) *
     *                                                              *
     ****************************************************************/
    private val _eventVm: MutableSharedFlow<EVENT_TYPE> = MutableSharedFlow<EVENT_TYPE>()
    public val eventVm: SharedFlow<EVENT_TYPE> = _eventVm.asSharedFlow()

    protected fun sendEvent(event: EVENT_TYPE) {
        viewModelScope.launch {
            _eventVm.emit(event)
        }
    }

}