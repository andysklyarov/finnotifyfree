package com.andysklyarov.finnotify.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.andysklyarov.finnotify.R
import com.andysklyarov.finnotify.databinding.CurrencyFragmentBinding
import com.andysklyarov.finnotify.framework.ApplicationViewModelFactory
import com.andysklyarov.finnotify.framework.MainApplication
import com.google.android.material.button.MaterialButton

class CurrencyFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    NamesListDialog.NamesListDialogListener {

    companion object {
        private const val SAVED_CODE_KEY = "SAVED_CODE_KEY"
        const val DIALOG_FRAGMENT = 1

        fun newInstance(currencyCode: String): CurrencyFragment {
            val args = Bundle()
            args.putString(SAVED_CODE_KEY, currencyCode)
            val fragment = CurrencyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var currencyCode: String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = requireActivity()
        viewModel = ViewModelProvider(activity, ApplicationViewModelFactory(activity.application))
            .get<MainViewModel>(MainViewModel::class.java)
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

        val settingsButton: MaterialButton = view.findViewById(R.id.settings_button)
        settingsButton.setOnClickListener {
            val navigationHost = activity as NavigationHost
            navigationHost.navigateTo(SettingsFragment(), true)
        }

        val textViewCode: TextView = view.findViewById(R.id.currency_code)
        textViewCode.setOnClickListener { openNamesDialog() }

        val textViewName: TextView = view.findViewById(R.id.currency_rus_name)
        textViewName.setOnClickListener { openNamesDialog() }

        val infoButton: Button = view.findViewById(R.id.info_button)
        infoButton.setOnClickListener { openInfoDialog() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val args = arguments
        currencyCode = if (args != null) {
            args.getString(SAVED_CODE_KEY)!!
        } else {
            getString(R.string.default_currency_code)
        }
        viewModel.updateData(currencyCode)
    }

    private fun openNamesDialog() {
        val namesListDialog = NamesListDialog()
        namesListDialog.setTargetFragment(this, DIALOG_FRAGMENT)
        namesListDialog.show(requireActivity().supportFragmentManager, "names dialog")
    }

    private fun openInfoDialog() {
        val infoDialog = InfoTextDialog()
        infoDialog.setTargetFragment(this, DIALOG_FRAGMENT)
        infoDialog.show(requireActivity().supportFragmentManager, "info dialog")
    }

    override fun onRefresh() {
        swipeRefreshLayout.post { viewModel.updateData(currencyCode) }
    }

    override fun applyCode(requestCode: Int, resultCode: Int, NameAndCode: String) { // todo clean code here
        if (requestCode == DIALOG_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                currencyCode = NameAndCode.substring(NameAndCode.indexOf("/") + 1).trim()
                onRefresh()

                (activity?.application as MainApplication).saveCode(currencyCode)

                val args = Bundle()
                args.putString(SAVED_CODE_KEY, currencyCode)
                arguments = args

                viewModel.disableAlarm()
            } else {
                Toast.makeText(context, "Error!!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStop(){
        super.onStop()
        (activity?.application as MainApplication).saveImgRes(viewModel.backgroundRes.get()!!)
    }
}