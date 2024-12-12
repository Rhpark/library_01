package kr.open.rhpark.library.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

public abstract class BaseViewModelEventFlow<EVENT_TYPE> : BaseViewModel() {

    /****************************************************************
     *                                                              *
     * Using for ViewModel -> View event                            *
     * (Activity.class, Fragment.class, CustomView.class)           *
     *                                                              *
     ****************************************************************/
    private val mSharedFlowEventVm: MutableSharedFlow<EVENT_TYPE> = MutableSharedFlow<EVENT_TYPE>()
    public val sharedFlowEventVm: SharedFlow<EVENT_TYPE> = mSharedFlowEventVm.asSharedFlow()

    private val mStateFlowEventVm: MutableStateFlow<EVENT_TYPE?> = MutableStateFlow(null)
    public val stateFlowEventVm: StateFlow<EVENT_TYPE?> = mStateFlowEventVm.asStateFlow()

    /**
     * You can receive the value again when updating the screen.
     *  중복값 처리 가능
     *  ex toast msg 값 전달.
     */
    protected fun sendSharedFlowEvent(event: EVENT_TYPE) { viewModelScope.launch { mSharedFlowEventVm.emit(event) } }


    /**
     * 중복값 제거 처리 값전달
     */
    protected fun sendStateFlowEvent(event: EVENT_TYPE) { viewModelScope.launch { mStateFlowEventVm.emit(event) } }
}