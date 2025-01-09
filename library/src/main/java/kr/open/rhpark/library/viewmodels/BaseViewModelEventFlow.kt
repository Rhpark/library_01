package kr.open.rhpark.library.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public abstract class BaseViewModelEventFlow<EVENT_TYPE> : BaseViewModel() {

    /****************************************************************
     *                                                              *
     * Using for ViewModel -> View event                            *
     * (Activity.class, Fragment.class, CustomView.class)           *
     *                                                              *
     ****************************************************************/
    private val eventVm = Channel<EVENT_TYPE>(Channel.BUFFERED)
    public val mEventVm: Flow<EVENT_TYPE> = eventVm.receiveAsFlow()

    protected fun sendEventVm(event: EVENT_TYPE) { viewModelScope.launch { eventVm.send(event) } }
}