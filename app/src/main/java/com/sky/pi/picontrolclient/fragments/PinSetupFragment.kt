package com.sky.pi.picontrolclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky.pi.picontrolclient.R
import com.sky.pi.picontrolclient.adapters.AdapterViewType
import com.sky.pi.picontrolclient.adapters.PinListAdapter
import com.sky.pi.picontrolclient.observe
import com.sky.pi.picontrolclient.viewmodels.PinViewModel
import kotlinx.android.synthetic.main.fragment_selected_pin_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PinSetupFragment : Fragment() {
    private val viewModel by sharedViewModel<PinViewModel>()
    private val pinListAdapter: PinListAdapter by lazy {
        PinListAdapter(
            onConfigurePin = { PinConfigDialogFragment.showDialog(it, parentFragmentManager) },
            onUpdatePin = viewModel::updatePinData,
            onDeletePin = viewModel::deletePin,
            adapterViewType = AdapterViewType()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_selected_pin_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupList()
    }

    private fun setupList() {
        pinListV.adapter = pinListAdapter
        pinListV.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.pinListLiveData.observe(this) {
            pinListAdapter.submitList(it)
            pinListAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun getInstance() = PinSetupFragment()
    }
}