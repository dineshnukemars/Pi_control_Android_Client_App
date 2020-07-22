package com.sky.pi.picontrolclient.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

        viewMainAct_PinInfo.setOnClickListener {
            launchFragment(PinInfoFragment.getInstance())
        }
        viewMainAct_PinSetup.setOnClickListener {
            launchFragment(PinSetupFragment.getInstance())
        }
        viewModel.errorMessageLD.observe(this, Observer {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
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