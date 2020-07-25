package com.sky.pi.picontrolclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.adapters.PinListAdapter
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.fragment_pin_setup.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinSetupFragment : Fragment() {
    private val viewModel by sharedViewModel<PinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_setup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pinListAdapter = PinListAdapter {
            viewModel.setSelectedPinOnList(it)
            PinConfigDialogFragment().show(parentFragmentManager, "ConfigDialog")
        }
        pinListV.adapter = pinListAdapter
        pinListV.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.pinListLiveData.observe(viewLifecycleOwner, Observer {
            pinListAdapter.submitList(it)
        })

        radioGroupV.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadio = group.findViewById<RadioButton>(checkedId)
            viewModel.setRadioSelected(selectedRadio.tag.toString())
        }

        viewModel.radioGroupLD.observe(this) { radioGroupV.visibility = it }
        viewModel.blinkContainerLD.observe(this) { blinkContainerV.visibility = it }
        viewModel.switchContainerLD.observe(this) { switchContainerV.visibility = it }
        viewModel.pwmContainerLD.observe(this) { pwmContainerV.visibility = it }
        viewModel.selectedPinLD.observe(this) { selectedPinV.text = it }

        switchV.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitchState(isChecked)
        }


        pwmDutyCycleV.max = 100
        pwmDutyCycleV.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setPwm(seekBar.progress, frequencyEditV.text.toString().toInt())
            }
        })

    }

    companion object {
        fun getInstance(): PinSetupFragment {
            return PinSetupFragment()
        }
    }
}

fun <T> LiveData<T>.observe(fragment: Fragment, block: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer {
        it?.let { t ->
            block(t)
        }
    })
}