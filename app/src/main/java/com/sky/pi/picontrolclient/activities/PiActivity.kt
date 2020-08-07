package com.sky.pi.picontrolclient.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.fragments.PinInfoFragment
import com.sky.pi.picontrolclient.fragments.PinSetupFragment
import com.sky.pi.picontrolclient.observe
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.activity_pi.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PiActivity : AppCompatActivity() {
    private val viewModel by viewModel<PinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pi)
        setupUI()
        viewModel.toastLD.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        showFragment(PinInfoFragment.getInstance())
        nav_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> showFragment(PinInfoFragment.getInstance())
                R.id.navigation_dashboard -> showFragment(PinSetupFragment.getInstance())
                else -> println("nothing to show")
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnectServer()
    }
}