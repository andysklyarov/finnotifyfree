package com.andysklyarov.finnotifyfree.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.databinding.CurrencyFragmentBinding
import com.andysklyarov.finnotifyfree.ui.MainViewModel
import com.andysklyarov.finnotifyfree.ui.NavigationHost
import com.andysklyarov.finnotifyfree.ui.dialogs.InfoTextDialog
import com.google.android.material.button.MaterialButton

class CurrencyFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var viewModel: MainViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        const val DIALOG_FRAGMENT = 1

        fun newInstance(): CurrencyFragment {
            return CurrencyFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CurrencyFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout = view.findViewById(R.id.refresher)
        swipeRefreshLayout.setOnRefreshListener(this)

        val appCompatActivity = (activity as AppCompatActivity)

        val settingsButton: MaterialButton = appCompatActivity.findViewById(R.id.settings_button)
        settingsButton.icon =
            AppCompatResources.getDrawable(appCompatActivity, R.drawable.ic_settings)
        settingsButton.setOnClickListener {
            val navigationHost = activity as NavigationHost
            navigationHost.navigateTo(SettingsFragment(), true)
        }

        val infoButton: MaterialButton = appCompatActivity.findViewById(R.id.info_button)
        infoButton.visibility = View.VISIBLE
        infoButton.setOnClickListener { openInfoDialog() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.updateSettings()
        viewModel.updateData()
    }

    override fun onRefresh() {
        swipeRefreshLayout.post {
            viewModel.updateData()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity?.application as AppDelegate).saveImgRes(viewModel.backgroundRes.get())
    }

    override fun onDetach() {
        viewModel.dispatchDetach()
        super.onDetach()
    }

    private fun openInfoDialog() {
        val infoDialog = InfoTextDialog()
        infoDialog.setTargetFragment(this, DIALOG_FRAGMENT)
        infoDialog.show(requireActivity().supportFragmentManager, "info dialog")
    }
}