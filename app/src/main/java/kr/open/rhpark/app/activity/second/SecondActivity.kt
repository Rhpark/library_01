package kr.open.rhpark.app.activity.second


import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivitySecondBinding
import kr.open.rhpark.app.fragment.FirstFragment
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class SecondActivity : BaseBindingActivity<ActivitySecondBinding>(R.layout.activity_second) {

    private val vm: SecondActivityVm by lazy { getViewModel<SecondActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val myFragment = FirstFragment()
        fragmentTransaction.add(R.id.fgFirst, myFragment, "FirstFragment")
        fragmentTransaction.commit()
    }
}