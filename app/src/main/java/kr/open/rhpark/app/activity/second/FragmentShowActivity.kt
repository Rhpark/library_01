package kr.open.rhpark.app.activity.second


import android.os.Bundle
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityFragmentShowBinding
import kr.open.rhpark.app.fragment.FirstFragment
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity

class FragmentShowActivity : BaseBindingActivity<ActivityFragmentShowBinding>(R.layout.activity_fragment_show) {

    private val vm: FragmentShowVm by lazy { getViewModel<FragmentShowVm>() }

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