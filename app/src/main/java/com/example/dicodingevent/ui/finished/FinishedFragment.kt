package com.example.dicodingevent.ui.finished

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.adapter.EventMiniAdapter
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!
    private lateinit var finishedViewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        finishedViewModel =
            ViewModelProvider(this)[FinishedViewModel::class.java]

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.layoutManager = layoutManager

        finishedViewModel.searchText.observe(viewLifecycleOwner) { searchQuery ->
            binding.searchBar.setQuery(searchQuery, false)
        }

        finishedViewModel.listEvent.observe(viewLifecycleOwner) { listEvent ->
            val adapter = EventMiniAdapter()
            adapter.submitList(listEvent)
            binding.rvFinished.adapter = adapter
            if(listEvent.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }

        finishedViewModel.errorMsg.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMsg ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    errorMsg,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        with(binding) {
            searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    finishedViewModel.setSearchText(query.toString())
                    finishedViewModel.searchEvents()

                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    finishedViewModel.setSearchText(newText.toString())
                    return false
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}