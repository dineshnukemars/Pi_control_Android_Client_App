package com.sky.pi.picontrolclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.models.Pin
import com.sky.pi.picontrolclient.observe
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.fragment_board_pin_layout.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinInfoFragment : Fragment() {
    private val viewModel by sharedViewModel<PinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_board_pin_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCheckBoxes(view)
    }

    private fun setupCheckBoxes(view: View) {
        view_pinRadioContainer1.children.forEach(::setListenerToCheckBox)
        view_pinRadioContainer2.children.forEach(::setListenerToCheckBox)
        viewModel.pinListLive.observe(this) { pinList ->
            setCheckBoxesStateFromPinList(pinList, view)
        }
    }

    private fun setCheckBoxesStateFromPinList(
        pinList: List<Pin>,
        view: View
    ): Unit = pinList.forEach { data ->
        view.findViewWithTag<CheckBox>(data.pinNo.toString())?.isChecked = true
    }

    private fun setListenerToCheckBox(containerView: View?) {
        val checkBox = containerView as CheckBox
        val pinNo = checkBox.tag.toString().toInt()
        checkBox.setOnClickListener {
            if (checkBox.isChecked) viewModel.addPin(pinNo)
            else viewModel.deletePin(pinNo)
        }
    }

    companion object {
        fun getInstance() = PinInfoFragment()
    }
}