package kr.open.rhpark.library.system.service

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

public class SystemServiceManagerInfo(context: Context) {

    /***************************
     *  System Service Manager *
     ***************************/
    public val inputMethodManager: InputMethodManager by lazy { context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager }


    /************************
     *  SoftKeyBoardView    *
     *  InputMethodManager  *
     ************************/
    public val softKeyboardInfoView: SoftKeyboardInfoStateView by lazy { SoftKeyboardInfoStateView(inputMethodManager) }
}