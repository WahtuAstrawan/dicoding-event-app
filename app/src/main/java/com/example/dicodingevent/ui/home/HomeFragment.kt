package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.adapter.EventMiniAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManagerUp = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingHome.layoutManager = layoutManagerUp

        val layoutManagerFin = LinearLayoutManager(requireContext())
        binding.rvFinishedHome.layoutManager = layoutManagerFin

        homeViewModel.listEventUp.observe(viewLifecycleOwner) { listEventUp ->
            val adapterUp = EventMiniAdapter()
            adapterUp.submitList(listEventUp)
            binding.rvUpcomingHome.adapter = adapterUp
        }

        homeViewModel.listEventFin.observe(viewLifecycleOwner) { listEventFin ->
            val adapterFin = EventMiniAdapter()
            adapterFin.submitList(listEventFin)
            binding.rvFinishedHome.adapter = adapterFin
        }

        homeViewModel.errorMsg.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMsg ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    errorMsg,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}