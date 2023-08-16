package com.task.currencyconversion.ui.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.task.currencyconversion.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val from = DetailsFragmentArgs.fromBundle(requireArguments()).from
        val to = DetailsFragmentArgs.fromBundle(requireArguments()).to
        viewModel.setCurrenciesCodes(from, to)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currencyHistoryAdapter =CurrencyRatesAdapter()
        val currencyRatesAdapter =CurrencyRatesAdapter()
        binding.historyRecycler.adapter=currencyHistoryAdapter
        binding.ratesRecycler.adapter=currencyRatesAdapter
        lifecycleScope.launch {
            viewModel.currencyRatesList.observe(viewLifecycleOwner) {
                currencyRatesAdapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            viewModel.currencyHistoricalDataList.observe(viewLifecycleOwner) {
                currencyHistoryAdapter.submitList(it)
                binding.currencyHistoryList=it
            }
        }
    }

}
