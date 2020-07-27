package com.sky.pi.picontrolclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.fragment_pin_info.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinInfoFragment : Fragment() {

    private val viewModel by sharedViewModel<PinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pinRadioContainer1.children.forEach(::setListenerToCheckBox)
        view_pinRadioContainer2.children.forEach(::setListenerToCheckBox)

        viewModel.pinListLiveData.observe(viewLifecycleOwner, Observer {
            it.forEach { data ->
                view.findViewWithTag<CheckBox>(data.pinNo.toString())?.isChecked = true
            }
        })

        stopServerBtn.setOnClickListener {
            viewModel.shutdownServer()
        }
    }

    private fun setListenerToCheckBox(containerView: View?) {
        val checkBox = containerView as CheckBox
        checkBox.setOnClickListener {
            val pinNo = checkBox.tag.toString().toInt()
            when {
                checkBox.isChecked -> viewModel.addPin(pinNo)
                else -> viewModel.deletePin(pinNo)
            }
        }
    }

    companion object {
        fun getInstance(): PinInfoFragment {
            return PinInfoFragment()
        }
    }
}