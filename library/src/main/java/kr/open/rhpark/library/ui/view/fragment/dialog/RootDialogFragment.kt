package kr.open.rhpark.library.ui.view.fragment.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.util.extensions.context.getDisplayInfo

public abstract class RootDialogFragment() : DialogFragment() {

    private var onPositiveClickListener: ((View) -> Unit)? = null
    private var onNegativeClickListener: ((View) -> Unit)? = null
    private var onOtherClickListener: ((View) -> Unit)? = null

    public interface OnItemClick {
        public fun onItemClickListener(v: View)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    protected fun resizeDialog(widthRatio: Float, heightRatio: Float) {
        dialog?.window?.let {
            val screenSize = requireContext().getDisplayInfo().getScreen()
            Logx.d("Screen Size $screenSize, " + requireContext().getDisplayInfo().getFullScreenSize())
            val x = (screenSize.x * widthRatio).toInt()
            val y = (screenSize.y * heightRatio).toInt()
            it.setLayout(x, y)
        }?: Logx.e("Error dialog window is null!")
    }

    public fun setOnPositiveClickListener(listener: (View) -> Unit) {
        onPositiveClickListener = listener
    }

    public fun setOnNegativeClickListener(listener: (View) -> Unit) {
        onNegativeClickListener = listener
    }

    public fun setOnOtherClickListener(listener: (View) -> Unit) {
        onOtherClickListener = listener
    }

    public fun safeDismiss() {
        try {
            dismiss()
        } catch (e: Exception) {
            Logx.e("Error $e")
        }
    }

    public fun safeShow(fragmentManager: FragmentManager, tag: String) {
        try {
            show(fragmentManager, tag)
        } catch (e:Exception) {
            Logx.e("Error $e")
        }
    }
}