package com.sky.pi.picontrolclient.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.fragments.PinInfoFragment
import com.sky.pi.picontrolclient.fragments.PinSetupFragment
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<PinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchFragment(PinInfoFragment.getInstance())

        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.title) {
                "Add Pins" -> launchFragment(PinInfoFragment.getInstance())
                "Configure" -> launchFragment(PinSetupFragment.getInstance())
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewMain_fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.close()
    }
}