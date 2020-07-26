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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_setup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pinListAdapter = PinListAdapter(viewModel::setItemAction)
        pinListV.adapter = pinListAdapter
        pinListV.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.pinListLiveData.observe(this) {
            pinListAdapter.submitList(it)
            pinListAdapter.notifyDataSetChanged()
        }
        viewModel.showConfigDialog.observe(this) {
            PinConfigDialogFragment.showDialog(parentFragmentManager)
        }
    }

    companion object {
        fun getInstance(): PinSetupFragment {
            return PinSetupFragment()
        }
    }
}