package com.example.dicodingevent.ui.upcoming

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
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!
    private lateinit var upcomingViewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        upcomingViewModel =
            ViewModelProvider(this)[UpcomingViewModel::class.java]

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.layoutManager = layoutManager

        upcomingViewModel.searchText.observe(viewLifecycleOwner) { searchQuery ->
            binding.searchBar.setQuery(searchQuery, false)
        }

        upcomingViewModel.errorMsg.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMsg ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    errorMsg,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        upcomingViewModel.listEvent.observe(viewLifecycleOwner) { listEvent ->
            val adapter = EventAdapter()
            adapter.submitList(listEvent)
            binding.rvUpcoming.adapter = adapter
            if(listEvent.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        with(binding) {
            searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    upcomingViewModel.setSearchText(query.toString())
                    upcomingViewModel.searchEvents()

                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    upcomingViewModel.setSearchText(newText.toString())
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