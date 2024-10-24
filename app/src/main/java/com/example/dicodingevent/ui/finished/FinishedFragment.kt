package com.example.dicodingevent.ui.finished

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.ViewModelFactory
import com.example.dicodingevent.ui.adapter.EventMiniAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!
    private val adapter = EventMiniAdapter()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.layoutManager = layoutManager
        binding.rvFinished.adapter = adapter

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val finishedViewModel: FinishedViewModel by viewModels {
            factory
        }

        finishedViewModel.events.observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }

        finishedViewModel.searchText.observe(viewLifecycleOwner) { query ->
            binding.searchBar.setQuery(query, false)
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                finishedViewModel.setSearchText(query ?: "")
                finishedViewModel.searchEvents()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(200)
                    finishedViewModel.setSearchText(newText ?: "")
                }
                return false
            }
        })
    }

    private fun handleResult(result: Result<List<ListEventsItem>>) {
        when (result) {
            is Result.Loading -> binding.progressBar.isVisible = true
            is Result.Success -> {
                binding.progressBar.isVisible = false
                adapter.submitList(result.data)
                binding.tvEmpty.isVisible = result.data.isEmpty()
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