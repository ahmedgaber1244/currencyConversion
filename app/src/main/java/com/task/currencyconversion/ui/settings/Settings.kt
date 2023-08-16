package com.task.currencyconversion.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.task.currencyconversion.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Settings : Fragment() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.nightMode.setOnCheckedChangeListener { _, itemId ->
            viewModel.setMode(itemId)
        }
        lifecycleScope.launch {
            viewModel.nightMode.collect {
                binding.mode = it
            }
        }
        lifecycleScope.launch {
            viewModel.changeLanguage.collect {
                if (it) {
                    requireActivity().recreate()
                }
            }
        }
    }


}
