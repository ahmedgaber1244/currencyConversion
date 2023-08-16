package com.task.currencyconversion.ui.convertCurrency


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.task.currencyconversion.R
import com.task.currencyconversion.data.bodyReq.ConversionReq
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.databinding.ConversionFragmentBinding
import com.task.currencyconversion.util.helper.NumericInputFilter
import com.task.currencyconversion.util.helper.textChangesFlow
import com.task.currencyconversion.util.snackbar.customSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConversionFragment : Fragment() {
    private lateinit var viewModel: ConversionViewModel
    private lateinit var binding: ConversionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ConversionFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[ConversionViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.req = ConversionReq(amount = 1.0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromTextVal.filters = arrayOf(NumericInputFilter())
        binding.fromVal.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as Currency
            val rateLocalModuleDb = viewModel.getRateLocalModuleDb(selectedItem.code)
            binding.req?.let { req ->
                req.from = rateLocalModuleDb
                binding.req = req
            }
        }
        binding.toVal.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as Currency
            val rateLocalModuleDb = viewModel.getRateLocalModuleDb(selectedItem.code)
            binding.req?.let { req ->
                req.to = rateLocalModuleDb
                binding.req = req
            }
        }
        lifecycleScope.launch {
            binding.fromTextVal.textChangesFlow().collectLatest { searchText ->
                binding.req?.let { req ->
                    req.amount = if (searchText.isEmpty()) 0.0 else searchText.toDouble()
                    binding.req = req
                }
            }
        }
        lifecycleScope.launch {
            viewModel.convertRes.collect {
                it?.let {
                    binding.req = it
                }
            }
        }
        lifecycleScope.launch {
            viewModel.currenciesList.observe(viewLifecycleOwner) {
                binding.currencies = it
                if (it.isNotEmpty()) {
                    binding.req?.let { req ->
                        req.from = viewModel.getRateLocalModuleDb(it[0].code)
                        req.to = viewModel.getRateLocalModuleDb(it[1].code)
                        binding.req = req
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.validation.collect {
                if (it > 0) {
                    customSnackBar(
                        requireContext(),
                        binding.root,
                        context?.resources?.getString(it) ?: "",
                        false
                    )
                }
            }
        }
        lifecycleScope.launch {
            viewModel.navToFragment.collect {
                when (it) {
                    R.id.action_conversionFragment_to_detailsFragment -> {
                        binding.req?.let { req ->
                            findNavController().navigate(
                                ConversionFragmentDirections.actionConversionFragmentToDetailsFragment(
                                    req.from?.currencyCode ?: "", req.to?.currencyCode ?: ""
                                )
                            )
                        }
                    }

                    R.id.action_conversionFragment_to_settings -> findNavController().navigate(it)
                }
                viewModel.resetNav()
            }
        }
    }
}
