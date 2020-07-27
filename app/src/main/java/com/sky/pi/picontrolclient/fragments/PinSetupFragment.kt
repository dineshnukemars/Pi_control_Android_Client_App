package com.sky.pi.picontrolclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.adapters.PinListAdapter
import com.sky.pi.picontrolclient.observe
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.fragment_pin_setup.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinSetupFragment : Fragment() {
    private val viewModel by sharedViewModel<PinViewModel>()

    private val pinListAdapter: PinListAdapter by lazy {
        PinListAdapter(
            onConfigurePin = ::openConfigDialog,
            onUpdatePin = viewModel::updatePinData,
            onDeletePin = viewModel::deletePin
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_setup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pinListAdapter = pinListAdapter
        pinListV.adapter = pinListAdapter
        pinListV.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.pinListLiveData.observe(this) {
            pinListAdapter.submitList(it)
            pinListAdapter.notifyDataSetChanged()
        }
    }

    private fun openConfigDialog(pinNo: Int): Unit =
        PinConfigDialogFragment.showDialog(pinNo, parentFragmentManager)

    companion object {
        fun getInstance(): PinSetupFragment {
            return PinSetupFragment()
        }
    }
}