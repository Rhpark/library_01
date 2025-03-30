package kr.open.rhpark.app.fragment.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.FragmentDialogBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.view.fragment.dialog.BaseBindingDialogFragment
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort

class FragmentDialogType(val title:String) :
    BaseBindingDialogFragment<FragmentDialogBinding>(R.layout.fragment_dialog) {

    private val vm: FragmentDialogTypeVm by lazy { getViewModel<FragmentDialogTypeVm>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logx.d()
        resizeDialog(0.7f, 0.7f)
        binding.vm = vm
        binding.tvDlgTitle.text = title
        lifecycleScope.launch {
            vm.mEventVm.collect {
                when(it) {
                    is FragmentDialogTypeVmEvent.OnDismiss -> close(it.isSave)
                }
            }
        }
    }

    private fun close(isSave:Boolean) {

        if(isSave) {
            toastShowShort("Save and dismiss")
        } else {
            toastShowShort("dismiss")
        }
        dismiss()

    }


}