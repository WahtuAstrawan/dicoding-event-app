package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.ViewModelFactory
import com.example.dicodingevent.ui.adapter.EventMiniAdapter
import com.example.dicodingevent.ui.setting.SettingViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val adapterUp = EventMiniAdapter()
    private val adapterFin = EventMiniAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }
        val settingViewModel: SettingViewModel by viewModels {
            factory
        }

        settingViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val layoutManagerUp =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingHome.layoutManager = layoutManagerUp
        binding.rvUpcomingHome.adapter = adapterUp

        val layoutManagerFin = LinearLayoutManager(requireContext())
        binding.rvFinishedHome.layoutManager = layoutManagerFin
        binding.rvFinishedHome.adapter = adapterFin

        homeViewModel.getEventsUpcoming().observe(viewLifecycleOwner) { result ->
            handleResult(result, adapterUp)
        }

        homeViewModel.getEventsFinished().observe(viewLifecycleOwner) { result ->
            handleResult(result, adapterFin)
        }
    }

    private fun handleResult(result: Result<List<ListEventsItem>>, adapter: EventMiniAdapter) {
        when (result) {
            is Result.Loading -> binding.progressBar.isVisible = true
            is Result.Success -> {
                binding.progressBar.isVisible = false
                adapter.submitList(result.data)
            }
            is Result.Error -> {
                binding.progressBar.isVisible = false
                result.message.getContentIfNotHandled()?.let { errorMessage ->
                    Snackbar.make(
                        requireActivity().window.decorView.rootView,
                        errorMessage,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}